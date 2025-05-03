// ModConfig.java
package com.LongJumpMod.examplemod;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class ModConfig {
    public static Configuration config;
    
    // 可配置参数
    public static double baseMultiplier = 1.5;
    public static double randomRange = 1.0;
    public static int accelerationTicks = 10;
    public static boolean enableVerticalRandom = true;

    public static void init(File configFile) {
        config = new Configuration(configFile);
        loadConfig();
    }

    public static void loadConfig() {
        baseMultiplier = config.getFloat("baseMultiplier", "settings", 1.5f, 0.5f, 5.0f, "基础加速倍率");
        randomRange = config.getFloat("randomRange", "settings", 1.0f, 0.0f, 2.0f, "随机范围");
        accelerationTicks = config.getInt("accelerationTicks", "settings", 10, 5, 20, "加速持续时间（tick）");
        enableVerticalRandom = config.getBoolean("enableVerticalRandom", "settings", true, "启用垂直随机扰动");
        
        if (config.hasChanged()) {
            config.save();
        }
    }
}