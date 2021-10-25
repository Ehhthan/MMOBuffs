package com.ehhthan.mmobuffs.command;

import com.ehhthan.mmobuffs.MMOBuffs;
import io.lumine.mythic.utils.commands.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class MMOBuffsCommand extends Command<MMOBuffs> {
    public MMOBuffsCommand() {
        super(MMOBuffs.getInst());

        this.addSubCommands(new ReloadCommand(this));
        this.addSubCommands(new AddEffectCommand(this));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] strings) {
        commandSender.sendMessage(getPlugin().getLanguageManager().getMessage("unknown-command"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] strings) {
        if (strings.length == 0) {
            ArrayList<String> list = new ArrayList<>();
            list.add("reload");
            list.add("add");
            return list;
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public boolean isConsoleFriendly() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
