package com.wynprice.modjam5.common.registries;

import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.WorldPaintConfig;
import com.wynprice.modjam5.common.entities.EntityPaintThrown;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@EventBusSubscriber(modid=WorldPaint.MODID)
public class WorldPaintEntityRegistry {

	@SubscribeEvent
	public static void onEntityRegister(RegistryEvent.Register<EntityEntry> event) {
		event.getRegistry().registerAll(
				EntityEntryBuilder.create()
				.entity(EntityPaintThrown.class)
				.id(new ResourceLocation(WorldPaint.MODID, "thrown_paint"), WorldPaintConfig.entityThrownID)
				.name("thrown_paint")
				.factory(EntityPaintThrown::new)
				.tracker(128, 20, true)
				.build());
	}
}