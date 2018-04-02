package com.wynprice.modjam5.common.colorfunctionality;

import java.util.HashMap;
import java.util.Random;

import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.WorldColorsHandler.DataInfomation;
import com.wynprice.modjam5.common.core.WorldPaintHooks;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

@EventBusSubscriber(modid=WorldPaint.MODID)
public class ColorBehaviourEventDispatcher {
	
	@SubscribeEvent
	public static void onLivingTick(LivingUpdateEvent event) {
		BlockPos position = event.getEntity().getPosition();
		DataInfomation info = WorldColorsHandler.getInfo(event.getEntity().world, position);
		for(int i = 0; i < 3; i++) {
			if(info.isDefault() || event.getEntity().world.getBlockState(position).getMaterial() == Material.AIR) {
				position = position.down();
				info = WorldColorsHandler.getInfo(event.getEntity().world, position);
			}
		}
		
		ColorFunction inFunction = null;
		if(event.getEntity() instanceof EntityPlayer)
		if(WorldPaintHooks.allowedBlocks.contains(event.getEntity().world.getBlockState(position).getBlock()) && !info.isDefault()) {
			inFunction = ColorUtils.findClosestPaletteColorTo(info.getColor());
			inFunction.onMobTick(event.getEntityLiving());
		}
		
		for(ColorFunction function : ColorFunctions.AWAY_FUNCTIONS) {
			if(function != inFunction) {
				function.onAwayMobTick(event.getEntityLiving());
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityDamaged(LivingDamageEvent event) {
		BlockPos position = event.getEntity().getPosition();
		DataInfomation info = WorldColorsHandler.getInfo(event.getEntity().world, position);
		for(int i = 0; i < 10; i++) {
			if(info.isDefault() || event.getEntity().world.getBlockState(position).getMaterial() == Material.AIR) {
				position = position.down();
				info = WorldColorsHandler.getInfo(event.getEntity().world, position);
			}
		}
		
		ColorFunction inFunction = null;
		if(WorldPaintHooks.allowedBlocks.contains(event.getEntity().world.getBlockState(position).getBlock()) && !info.isDefault()) {
			inFunction = ColorUtils.findClosestPaletteColorTo(info.getColor());
			event.setCanceled(inFunction.onEntityDamaged(event.getEntityLiving(), event.getSource(), event.getAmount()));
		}
	}
	
	@SubscribeEvent
	public static void onWorldTick(WorldTickEvent event) {
		for(ColorFunction function : ColorFunctions.ALL_FUNCTIONS) {
			function.onWorldTick(event.world);
		}
	}
	
	public static void onRandomBlockTick(World world, BlockPos pos, DataInfomation info) {
		if(!info.isDefault()) {
			ColorUtils.findClosestPaletteColorTo(info.getColor()).onBlockTick(world, pos);
		}
	}
}
