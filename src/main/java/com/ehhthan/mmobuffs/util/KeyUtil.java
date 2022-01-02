package com.ehhthan.mmobuffs.util;

import com.ehhthan.mmobuffs.MMOBuffs;
import org.bukkit.NamespacedKey;

public class KeyUtil {
    public static NamespacedKey key(String key) {
        return new NamespacedKey(MMOBuffs.getInst(), key);
    }
}
