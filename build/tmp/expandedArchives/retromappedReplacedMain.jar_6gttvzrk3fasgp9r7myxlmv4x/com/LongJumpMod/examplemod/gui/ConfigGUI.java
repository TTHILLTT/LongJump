// ConfigGUI.java
package com.LongJumpMod.examplemod.gui;

import com.LongJumpMod.examplemod.ModConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.common.config.Configuration;
import com.LongJumpMod.examplemod.ModConfig;
import java.io.IOException;

public class ConfigGUI extends GuiScreen {
    private static final int BUTTON_BASE_MULTIPLIER = 0;
    private static final int BUTTON_RANDOM_RANGE = 1;
    private static final int BUTTON_ACCEL_TICKS = 2;
    private static final int BUTTON_VERTICAL_RANDOM = 3;

    @Override
    public void func_73866_w_() {
        this.field_146292_n.clear();
        // 添加参数调整按钮
        this.field_146292_n.add(new GuiButton(BUTTON_BASE_MULTIPLIER, field_146294_l/2 - 100, 60, 200, 20, 
            "基础倍率: " + String.format("%.1f", ModConfig.baseMultiplier)));
        this.field_146292_n.add(new GuiButton(BUTTON_RANDOM_RANGE, field_146294_l/2 - 100, 90, 200, 20,
            "随机范围: " + String.format("%.1f", ModConfig.randomRange)));
        this.field_146292_n.add(new GuiButton(BUTTON_ACCEL_TICKS, field_146294_l/2 - 100, 120, 200, 20,
            "加速时长: " + ModConfig.accelerationTicks + " tick"));
        this.field_146292_n.add(new GuiButton(BUTTON_VERTICAL_RANDOM, field_146294_l/2 - 100, 150, 200, 20,
            "垂直扰动: " + (ModConfig.enableVerticalRandom ? "启用" : "禁用")));
    }

    @Override
    protected void func_146284_a(GuiButton button) {
        switch (button.field_146127_k) {
            case BUTTON_BASE_MULTIPLIER:
                ModConfig.baseMultiplier = (ModConfig.baseMultiplier >= 5.0) ? 0.5 : ModConfig.baseMultiplier + 0.5;
                break;
            case BUTTON_RANDOM_RANGE:
                ModConfig.randomRange = (ModConfig.randomRange >= 2.0) ? 0.0 : ModConfig.randomRange + 0.5;
                break;
            case BUTTON_ACCEL_TICKS:
                ModConfig.accelerationTicks = (ModConfig.accelerationTicks >= 20) ? 5 : ModConfig.accelerationTicks + 1;
                break;
            case BUTTON_VERTICAL_RANDOM:
                ModConfig.enableVerticalRandom = !ModConfig.enableVerticalRandom;
                break;
        }
        if (ModConfig.config.hasChanged()) {
            ModConfig.config.save();
        }
        func_73866_w_(); // 刷新界面
    }

    @Override
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146276_q_();
        super.func_73863_a(mouseX, mouseY, partialTicks);
        this.func_73732_a(field_146289_q, "LongJump Mod 配置", field_146294_l/2, 30, 0xFFFFFF);
    }
}