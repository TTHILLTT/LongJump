package com.LongJumpMod.examplemod;

import com.LongJumpMod.examplemod.gui.ConfigGUI;
import com.LongJumpMod.examplemod.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;

import java.io.File;
import java.util.Random;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import com.LongJumpMod.examplemod.gui.ConfigGUI;

@Mod(modid = "longjumpmod", version = "1.2", name = "LongJumpMod")
public class LongJumpMod {
    private static final Random RANDOM = new Random();
    private boolean isJumping = false;
    private int accelerationTicks = 0;
    private double baseHorizontalMultiplier = 0.0;

    private static final KeyBinding openGuiKey = new KeyBinding("打开配置", Keyboard.KEY_L, "LongJump Mod");
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientRegistry.registerKeyBinding(openGuiKey); // 注册快捷键
        ModConfig.init(new File("./config/longjumpmod.cfg")); // 初始化配置
    }

    // 监听按键事件
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (openGuiKey.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new ConfigGUI());
        }
    }

    // 修改原有跳跃逻辑以使用配置参数
    @SubscribeEvent
    public void onPlayerJump(LivingJumpEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            isJumping = true;
            accelerationTicks = 0;
            
            // 使用配置参数
            baseHorizontalMultiplier = ModConfig.baseMultiplier + (RANDOM.nextDouble() * ModConfig.randomRange);
            if (ModConfig.enableVerticalRandom) {
                double verticalMultiplier = 1.0 + (RANDOM.nextDouble() - 0.5) * 0.2;
                player.motionY *= verticalMultiplier;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && isJumping) {
            EntityPlayer player = event.player;
            // 使用配置的加速时长
            if (!player.onGround && accelerationTicks < ModConfig.accelerationTicks) {
                // 动态计算当前加速倍率（递减）
                double currentMultiplier = baseHorizontalMultiplier * (1.0 - accelerationTicks / 15.0);
                
                // 获取玩家实际移动方向（基于视角）
                float yaw = (float) Math.toRadians(player.rotationYaw);
                double motionX = -Math.sin(yaw) * 0.1 * currentMultiplier;
                double motionZ = Math.cos(yaw) * 0.1 * currentMultiplier;
                
                // 应用速度（模拟自然移动输入）
                player.motionX += motionX;
                player.motionZ += motionZ;
                
                accelerationTicks++;
            } else {
                isJumping = false;
            }
        }
    }
}