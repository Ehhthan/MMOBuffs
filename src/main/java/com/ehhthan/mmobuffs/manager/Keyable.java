package com.ehhthan.mmobuffs.manager;

import org.apache.commons.lang.Validate;

public interface Keyable {
    static String format(String key) {
        Validate.notNull(key, "Key is null.");
        return key.replace("-", "_").replace(" ", "_").toLowerCase();
    }

    String getKey();
}
