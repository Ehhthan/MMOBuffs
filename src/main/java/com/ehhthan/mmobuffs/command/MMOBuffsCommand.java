package com.ehhthan.mmobuffs.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.EffectHolder;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.api.effect.StatusEffect;
import com.ehhthan.mmobuffs.api.modifier.Modifier;
import com.ehhthan.mmobuffs.manager.type.LanguageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

@CommandAlias("mmobuffs|mmobuff|buffs|buff")
@Description("Main mmobuffs command.")
public class MMOBuffsCommand extends BaseCommand {
    private final MMOBuffs plugin;
    private final LanguageManager language;

    public MMOBuffsCommand(MMOBuffs plugin, LanguageManager language) {
        this.plugin = plugin;
        this.language = language;
    }

    @Subcommand("reload")
    @CommandPermission("mmobuffs.reload")
    @Description("Reload the entire plugin.")
    public void onReloadCommand(CommandSender sender) {
        plugin.reload();
        Component message = language.getMessage("reload-command");

        if (message != null)
            sender.sendMessage(message);
    }

    @Subcommand("give|add")
    @CommandPermission("mmobuffs.give")
    @Description("Give an effect to a player.")
    @CommandCompletion("@players @effects @range:1-9 * @range:1-9 * -s")
    @Syntax("<player> <effect> <duration> [duration-modifier] [stacks] [stack-modifier] [-s]")
    public void onGiveCommand(CommandSender sender, EffectHolder holder, StatusEffect effect, Integer duration, @Default("SET") Modifier durationModifier,
                              @Default("1") Integer stacks, @Default("KEEP") Modifier stackModifier, @Default String silent) {
        ActiveStatusEffect activeEffect = ActiveStatusEffect.builder(effect).startDuration(duration).startStacks(stacks).build();
        holder.addEffect(activeEffect, durationModifier, stackModifier);

        TagResolver resolver = TagResolver.builder().resolvers(activeEffect.getResolver()).resolver(Placeholder.component("player", holder.getPlayer().displayName())).build();
        Component message = language.getMessage("give-effect", true, resolver);

        if (message != null && !silent.equalsIgnoreCase("-s"))
            sender.sendMessage(message);
    }

    @Subcommand("permanent|perm")
    @CommandPermission("mmobuffs.permanent")
    @Description("Give a permanent effect to a player.")
    @CommandCompletion("@players @effects * @range:1-9 * -s")
    @Syntax("<player> <effect> [duration-modifier] [stacks] [stack-modifier] [-s]")
    public void onPermanentCommand(CommandSender sender, EffectHolder holder, StatusEffect effect, @Default("REPLACE") Modifier durationModifier,
                                   @Default("1") Integer stacks, @Default("KEEP") Modifier stackModifier, @Default String silent) {
        holder.addEffect(ActiveStatusEffect.builder(effect).permanent(true).startStacks(stacks).build(), durationModifier, stackModifier);

        TagResolver resolver = TagResolver.builder().resolvers(effect.getResolver()).resolver(Placeholder.component("player", holder.getPlayer().displayName())).build();
        Component message = language.getMessage("give-effect-permanent", true, resolver);

        if (message != null && !silent.equalsIgnoreCase("-s"))
            sender.sendMessage(message);
    }

    @Subcommand("clear|remove")
    @CommandPermission("mmobuffs.clear")
    @Description("Remove a single effect, all non permanent effects, or every effect from a player.")
    @CommandCompletion("@players @effects|all|permanent -s")
    @Syntax("<player> <effect|all|permanent> [-s]")
    public void onClearCommand(CommandSender sender, EffectHolder holder, String choice, @Default String silent) {
        Component message;
        TagResolver.Single resolver = Placeholder.component("player", holder.getPlayer().displayName());

        switch (choice) {
            case "all" -> {
                holder.removeEffects(false);
                message = language.getMessage("clear-all-effects", true, resolver);
            }
            case "permanent" -> {
                holder.removeEffects(true);
                message = language.getMessage("clear-permanent-effects", true, resolver);
            }
            default -> {
                NamespacedKey key = NamespacedKey.fromString(choice, plugin);
                if (holder.hasEffect(key)) {
                    holder.removeEffect(key);
                }
                else
                    throw new InvalidCommandArgument("Invalid effect option specified.");

                message = language.getMessage("clear-effect", true, TagResolver.builder().resolver(resolver).resolver(holder.getEffect(key).getResolver()).build());
            }
        }
        if (message != null && !silent.equalsIgnoreCase("-s"))
            sender.sendMessage(message);
    }

    @Subcommand("time|duration")
    @CommandPermission("mmobuffs.time")
    @Description("Alter the duration of an effect.")
    @CommandCompletion("@players @effects * @range:1-9 -s")
    @Syntax("<player> <effect> <set|add|subtract|multiply|divide> <duration> [-s]")
    public void onTimeCommand(CommandSender sender, EffectHolder holder, StatusEffect effect, Operation operation, int duration, @Default String silent) {
        if (holder.hasEffect(effect.getKey())) {
            ActiveStatusEffect activeEffect = holder.getEffect(effect.getKey());
            int newDuration;
            switch (operation) {
                case SET -> newDuration = duration;
                case ADD -> newDuration = activeEffect.getDuration() + duration;
                case SUBTRACT -> newDuration = activeEffect.getDuration() - duration;
                case MULTIPLY -> newDuration = activeEffect.getDuration() * duration;
                case DIVIDE -> {
                    if (duration == 0)
                        throw new InvalidCommandArgument("Cannot divide by zero.");
                    newDuration = activeEffect.getDuration() / duration;
                }
                default -> newDuration = activeEffect.getStacks();
            }

            activeEffect.setDuration(newDuration);

            TagResolver resolver = TagResolver.builder().resolvers(activeEffect.getResolver()).resolver(Placeholder.component("player", holder.getPlayer().displayName())).build();
            Component message = language.getMessage("time-effect", true, resolver);
            if (message != null && !silent.equalsIgnoreCase("-s"))
                sender.sendMessage(message);
        } else {
            throw new InvalidCommandArgument("Player does not have that effect.");
        }
    }

    @Subcommand("stack|stacks")
    @CommandPermission("mmobuffs.stack")
    @Description("Alter the stacks of an effect.")
    @CommandCompletion("@players @effects * @range:1-9 -s")
    @Syntax("<player> <effect> <set|add|subtract|multiply|divide> <stacks> [-s]")
    public void onStackCommand(CommandSender sender, EffectHolder holder, StatusEffect effect, Operation operation, int stacks, @Default String silent) {
        if (holder.hasEffect(effect.getKey())) {
            ActiveStatusEffect activeEffect = holder.getEffect(effect.getKey());
            int newStacks;
            switch (operation) {
                case SET -> newStacks = stacks;
                case ADD -> newStacks = activeEffect.getStacks() + stacks;
                case SUBTRACT -> newStacks = activeEffect.getStacks() - stacks;
                case MULTIPLY -> newStacks = activeEffect.getStacks() * stacks;
                case DIVIDE -> {
                    if (stacks == 0)
                        throw new InvalidCommandArgument("Cannot divide by zero.");
                    newStacks = activeEffect.getStacks() / stacks;
                }
                default -> newStacks = activeEffect.getStacks();
            }

            activeEffect.setStacks(newStacks);
            holder.updateEffect(effect.getKey());

            TagResolver resolver = TagResolver.builder().resolvers(activeEffect.getResolver()).resolver(Placeholder.component("player", holder.getPlayer().displayName())).build();

            Component message = language.getMessage("stack-effect", true, resolver);
            if (message != null && !silent.equalsIgnoreCase("-s"))
                sender.sendMessage(message);
        } else {
            throw new InvalidCommandArgument("Player does not have that effect.");
        }
    }

    @Subcommand("list")
    @CommandPermission("mmobuffs.list")
    @Description("List the current effects on a player.")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onListCommand(CommandSender sender, @Optional EffectHolder holder) {
        if (holder == null)
            if (sender instanceof Player player && EffectHolder.has(player))
                holder = EffectHolder.get(player);
            else
                throw new InvalidCommandArgument("No player specified.");

        List<Component> components = new LinkedList<>();
        components.add(MMOBuffs.getInst().getLanguageManager().getMessage("list-display.header", false));

        String text = MMOBuffs.getInst().getLanguageManager().getString("list-display.effect-element");

        for (ActiveStatusEffect activeEffect : holder.getEffects(true)) {
            components.add(MiniMessage.miniMessage().deserialize((MMOBuffs.getInst().getParserManager().parse(holder.getPlayer(), text)), activeEffect.getResolver()));
        }

        TextComponent.Builder builder = Component.text();
        for (int i = 0; i < components.size(); i++) {
            builder.append(components.get(i));
            if (i != components.size()-1)
                builder.append(Component.newline());
        }

        sender.sendMessage(builder.build());
    }

    @CatchUnknown
    @Description("Catches unknown commands.")
    public void onUnknownCommand(CommandSender sender) {
        Component message = MMOBuffs.getInst().getLanguageManager().getMessage("unknown-command");
        if (message != null)
            sender.sendMessage(message);
    }

    enum Operation {
        SET,
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE
    }
}
