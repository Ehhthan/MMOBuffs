package com.ehhthan.mmobuffs.command;

import com.ehhthan.mmobuffs.MMOBuffs;
import io.lumine.mythic.utils.commands.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends Command<MMOBuffs> {
    public ReloadCommand(Command<MMOBuffs> parent) {
        super(parent);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] strings) {
        getPlugin().reload();
        commandSender.sendMessage(getPlugin().getLanguageManager().getMessage("reload-command"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "mmobuffs.reload";
    }

    @Override
    public boolean isConsoleFriendly() {
        return true;
    }

    @Override
    public String getName() {
        return "reload";
    }
}
