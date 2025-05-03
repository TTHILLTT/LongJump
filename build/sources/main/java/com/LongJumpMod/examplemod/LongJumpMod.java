package com.example.longjumpmod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import java.util.Random;

@Mod(modid = "longjumpmod", version = "1.1")
public class LongJumpMod {
    private static final Random RANDOM = new Random();
    private boolean isJumping = false;
    private int accelerationTicks = 0;
    private double baseHorizontalMultiplier = 0.0;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerJump(LivingJumpEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            
            // 初始化渐进加速参数
            isJumping = true;
            accelerationTicks = 0;
            
            // 随机化基础加速倍数 (1.5~2.5)
            baseHorizontalMultiplier = 1.5 + RANDOM.nextDouble() * 1.0;
            
            // 微调垂直速度 (±10%)
            double verticalMultiplier = 1.0 + (RANDOM.nextDouble() - 0.5) * 0.2;
            player.motionY *= verticalMultiplier;
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && isJumping) {
            EntityPlayer player = event.player;
            
            // 仅在玩家滞空时逐步加速
            if (!player.onGround && accelerationTicks < 10) {
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