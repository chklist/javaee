package com.mega.core.util;

import java.io.IOException;
import java.util.Properties;

public class PropertyMgr {
    private static final String DEFAULT_NAME = "config.properties";
    private static volatile PropertyMgr INSTANCE;
    private static Properties properties = new Properties();

    private String configName = DEFAULT_NAME;

    private PropertyMgr() {
        try {
            properties.load(PropertyMgr.class.getClassLoader().getResourceAsStream(DEFAULT_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertyMgr getInstance() {
        if (INSTANCE == null) {
            synchronized (PropertyMgr.class) {
                if (INSTANCE == null) {
                    INSTANCE = new PropertyMgr();
                }
            }
        }
        return INSTANCE;
    }

    public Object get(String key) {
        return properties.get(key);
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}
