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
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid=WorldPaint.MODID)
public class ColorBehaviourEventDispatcher {
	
	
	@SubscribeEvent
	public static void onLivingTick(LivingUpdateEvent event) {
		BlockPos position = event.getEntity().getPosition();
		DataInfomation info = WorldColorsHandler.getInfo(event.getEntity().world, position);
		for(int i = 0; i < 2; i++) {
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
	
	public static void onRandomBlockTick(World world, BlockPos pos, DataInfomation info) {
		if(!info.isDefault()) {
//			ColorUtils.findClosestPaletteColorTo(info.getColor()).onBlockTick(world, pos);
		}
	}
}
