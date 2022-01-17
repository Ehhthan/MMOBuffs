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
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

// TODO: 1/6/2022 Test if permission mmobuffs.* works and implement if it doesn't

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
    @CommandCompletion("@players @effects @range:1-9 * @range:1-9 true|false")
    @Syntax("<player> <effect> <duration> [modifier] [stacks]")
    public void onGiveCommand(CommandSender sender, EffectHolder holder, StatusEffect effect, Integer duration, @Default("REPLACE") Modifier modifier,
                              @Default("1") Integer stacks) {
        ActiveStatusEffect activeEffect = ActiveStatusEffect.builder(effect).startDuration(duration).startStacks(stacks).build();
        holder.addEffect(modifier, activeEffect);

        Collection<Template> templates = activeEffect.getTemplates();
        templates.add(Template.of("player", holder.getPlayer().getName()));
        Component message = language.getMessage("give-effect", true, templates);

        if (message != null)
            sender.sendMessage(message);
    }

    @Subcommand("permanent|perm")
    @CommandPermission("mmobuffs.permanent")
    @Description("Give a permanent effect to a player.")
    @CommandCompletion("@players @effects * @range:1-9 true|false")
    @Syntax("<player> <effect> [modifier] [stacks]")
    public void onPermanentCommand(CommandSender sender, EffectHolder holder, StatusEffect effect, @Default("REPLACE") Modifier modifier,
                                   @Default("1") Integer stacks) {
        holder.addEffect(modifier, ActiveStatusEffect.builder(effect).permanent(true).startStacks(stacks).build());

        Collection<Template> templates = effect.getTemplates();
        templates.add(Template.of("player", holder.getPlayer().getName()));
        Component message = language.getMessage("give-effect-permanent", true, templates);

        if (message != null)
            sender.sendMessage(message);
    }

    @Subcommand("clear|remove")
    @CommandPermission("mmobuffs.clear")
    @Description("Remove a single effect, all non permanent effects, or every effect from a player.")
    @CommandCompletion("@players @effects|all|permanent true|false")
    @Syntax("<player> <effect|all|permanent>")
    public void onClearCommand(CommandSender sender, EffectHolder holder, String choice) {
        Component message;
        List<Template> templates = new ArrayList<>();
        templates.add(Template.of("player", holder.getPlayer().getName()));

        switch (choice) {
            case "all" -> {
                holder.removeAllEffects(false);
                message = language.getMessage("clear-all-effects", true, templates);
            }
            case "permanent" -> {
                holder.removeAllEffects(true);
                message = language.getMessage("clear-permanent-effects", true, templates);
            }
            default -> {
                NamespacedKey key = NamespacedKey.fromString(choice, plugin);
                if (holder.hasEffect(key)) {
                    templates.addAll(holder.getEffect(key).getTemplates());
                    holder.removeEffect(key);
                }
                else
                    throw new InvalidCommandArgument("Invalid effect option specified.");

                message = language.getMessage("clear-effect", true, templates);
            }
        }
        if (message != null)
            sender.sendMessage(message);
    }

    @Subcommand("time|duration")
    @CommandPermission("mmobuffs.time")
    @Description("Alter the duration of an effect.")
    @CommandCompletion("@players @effects * @range:1-9")
    @Syntax("<player> <effect> <set|add|subtract|multiply|divide> <duration>")
    public void onTimeCommand(CommandSender sender, EffectHolder holder, StatusEffect effect, Operation operation, int duration) {
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

            Collection<Template> templates = activeEffect.getTemplates();
            templates.add(Template.of("player", holder.getPlayer().getName()));

            Component message = language.getMessage("time-effect", true, templates);
            if (message != null)
                sender.sendMessage(message);
        } else {
            throw new InvalidCommandArgument("Player does not have that effect.");
        }
    }

    // TODO: 1/6/2022 Add stack command that functions like time command.
    @Subcommand("stack|amount")
    @CommandPermission("mmobuffs.stack")
    @Description("Alter the stacks of an effect.")
    @CommandCompletion("@players @effects * @range:1-9")
    @Syntax("<player> <effect> <set|add|subtract|multiply|divide> <stacks>")
    public void onStackCommand(CommandSender sender, EffectHolder holder, StatusEffect effect, Operation operation, int stacks) {
        if (holder.hasEffect(effect.getKey())) {
            ActiveStatusEffect activeEffect = holder.getEffect(effect.getKey());
            int newStacks;
            switch (operation) {
                case SET -> newStacks = stacks;
                case ADD -> newStacks = activeEffect.getDuration() + stacks;
                case SUBTRACT -> newStacks = activeEffect.getDuration() - stacks;
                case MULTIPLY -> newStacks = activeEffect.getDuration() * stacks;
                case DIVIDE -> {
                    if (stacks == 0)
                        throw new InvalidCommandArgument("Cannot divide by zero.");
                    newStacks = activeEffect.getDuration() / stacks;
                }
                default -> newStacks = activeEffect.getStacks();
            }

            activeEffect.setStacks(newStacks);

            Collection<Template> templates = activeEffect.getTemplates();
            templates.add(Template.of("player", holder.getPlayer().getName()));

            Component message = language.getMessage("stack-effect", true, templates);
            if (message != null)
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
            components.add(MiniMessage.get().parse(MMOBuffs.getInst().getParserManager().parse(holder.getPlayer(), text), activeEffect.getTemplates()));
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
