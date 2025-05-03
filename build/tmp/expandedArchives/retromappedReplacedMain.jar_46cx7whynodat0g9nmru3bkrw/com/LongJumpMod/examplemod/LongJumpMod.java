package com.example.longjumpmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;

@Mod(modid = "longjumpmod", version = "1.0")
public class LongJumpMod {
    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerJump(LivingJumpEvent event) {
        if (event.entityLiving instanceof net.minecraft.entity.player.EntityPlayer) {
            double horizontalMultiplier = 4.0; // Experimental value, adjust as needed
            event.entityLiving.field_70159_w *= horizontalMultiplier;
            event.entityLiving.field_70179_y *= horizontalMultiplier;
        }
    }
}