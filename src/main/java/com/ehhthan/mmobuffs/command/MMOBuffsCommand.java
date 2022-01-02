package com.ehhthan.mmobuffs.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.EffectHolder;
import com.ehhthan.mmobuffs.api.effect.ActiveStatusEffect;
import com.ehhthan.mmobuffs.api.effect.StatusEffect;
import com.ehhthan.mmobuffs.api.modifier.Modifier;
import com.ehhthan.mmobuffs.manager.type.LanguageManager;
import io.lumine.mythic.utils.numbers.RangedInt;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;

@CommandAlias("mmobuffs|mmobuff|buffs|buff")
@Description("Main mmobars command.")
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
    @Description("Give a status effect to a player.")
    @CommandCompletion("@players @effects @range:1-9 * @range:1-9 true|false")
    @Syntax("<player> <effect> <duration> [modifier] [stacks] [silent]")
    public void onGiveCommand(CommandSender sender, EffectHolder holder, StatusEffect effect, Integer duration, @Default("REPLACE") Modifier modifier,
                              @Default("1") Integer stacks, @Default("false") Boolean silent) {
        holder.addEffect(modifier, ActiveStatusEffect.builder(effect).startDuration(duration).startStacks(stacks).build());

    }

    @Subcommand("permanent|perm")
    @CommandPermission("mmobuffs.permanent")
    @Description("Give a permanent status effect to a player.")
    @CommandCompletion("@players @effects * @range:1-9 true|false")
    @Syntax("<player> <effect> [modifier] [stacks] [silent]")
    public void onPermanentCommand(CommandSender sender, EffectHolder holder, StatusEffect effect, @Default("REPLACE") Modifier modifier,
                                   @Default("1") Integer stacks, @Default("false") Boolean silent) {
        holder.addEffect(modifier, ActiveStatusEffect.builder(effect).permanent(true).startStacks(stacks).build());
    }

    @Subcommand("clear|remove")
    @CommandPermission("mmobuffs.clear")
    @Description("Clear a status effect from a player.")
    @CommandCompletion("@players @effects|all|permanent")
    @Syntax("<player> <effect|all|permanent>")
    public void onClearCommand(CommandSender sender, EffectHolder holder, String choice) {
        if (choice.equalsIgnoreCase("all"))
            for (NamespacedKey key : holder.getEffects(false)) {
                holder.removeEffect(key);
            }
        else if (choice.equalsIgnoreCase("permanent"))
            for (NamespacedKey key : holder.getEffects(true)) {
                holder.removeEffect(key);
            }
        else if (holder.hasEffect(NamespacedKey.fromString(choice, plugin)))
            holder.removeEffect(NamespacedKey.fromString(choice, plugin));
        else
            throw new InvalidCommandArgument("Invalid effect specified.");
    }

    @Subcommand("time|duration")
    @CommandPermission("mmobuffs.time")
    @Description("Alter the duration of a status effect")
    @CommandCompletion("@players @effects * @range:1-9")
    @Syntax("<player> <effect> <set|add|subtract|multiply|divide> <duration>")
    public void onTimeCommand(CommandSender sender, EffectHolder holder, StatusEffect effect, Operation operation, int duration) {
        if (holder.hasEffect(effect.getKey())) {
            ActiveStatusEffect activeEffect = holder.getEffect(effect.getKey());
            switch (operation) {
                case SET -> activeEffect.setDuration(duration);
                case ADD -> activeEffect.setDuration(activeEffect.getDuration() + duration);
                case SUBTRACT -> activeEffect.setDuration(activeEffect.getDuration() - duration);
                case MULTIPLY -> activeEffect.setDuration(activeEffect.getDuration() * duration);
                case DIVIDE -> {
                    if (duration == 0)
                        throw new InvalidCommandArgument("Cannot divide by zero.");
                    activeEffect.setDuration(activeEffect.getDuration() / duration);
                }
            }
        }

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
