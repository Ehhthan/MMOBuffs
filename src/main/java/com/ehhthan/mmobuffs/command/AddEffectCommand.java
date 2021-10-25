package com.ehhthan.mmobuffs.command;

import com.ehhthan.mmobuffs.MMOBuffs;
import com.ehhthan.mmobuffs.api.modifier.Modifier;
import io.lumine.mythic.utils.commands.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

// /mmobuff add <player> <effect> <time> <modifier = replace> <stack = 1>
public class AddEffectCommand extends Command<MMOBuffs> {
    public AddEffectCommand(MMOBuffsCommand command) {
        super(command);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] strings) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] strings) {
        if (strings.length == 1)
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        if (strings.length == 2)
            return MMOBuffs.getInst().getEffectManager().keys().stream().toList();
        if (strings.length == 3)
            return getNumbersBetween(1, 10);
        if (strings.length == 4)
            return Arrays.stream(Modifier.values()).map(Modifier::name).collect(Collectors.toList());
        if (strings.length == 5)
            return getNumbersBetween(1, 10);
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
        return "add";
    }

    private List<String> getNumbersBetween(int i1, int i2) {
        List<Integer> range = IntStream.range(i1, i2).boxed().collect(toList());
        List<String> list = new ArrayList<>(range.size());
        for (Integer i : range) {
            list.add(String.valueOf(i));
        }
        return list;
    }
}
