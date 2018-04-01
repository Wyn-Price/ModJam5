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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid=WorldPaint.MODID)
public class ColorBehaviourEventDispatcher {
	
	private static HashMap<Entity, EnumColorBehaviour> previousTickMap = new HashMap<>();
	
	@SubscribeEvent
	public static void onLivingTick(LivingUpdateEvent event) {
		BlockPos position = event.getEntity().getPosition();
		DataInfomation info = WorldColorsHandler.getInfo(event.getEntity().world, position);
		if(info.isDefault() || event.getEntity().world.getBlockState(position).getMaterial() == Material.AIR) {
			position = position.down();
			info = WorldColorsHandler.getInfo(event.getEntity().world, position);
		}
				
		if(WorldPaintHooks.allowedBlocks.contains(event.getEntity().world.getBlockState(position).getBlock()) && !info.isDefault()) {
			EnumColorBehaviour behaviour = ColorUtils.findClosestPaletteColorTo(info.getColor());
			previousTickMap.put(event.getEntity(), behaviour);
			if(new Random().nextFloat() < behaviour.getChance()) {
				behaviour.onMobTick(event.getEntityLiving());
			}
		}
	}
	
	public static void onRandomBlockTick(World world, BlockPos pos, DataInfomation info) {
		if(!info.isDefault()) {
			EnumColorBehaviour behaviour = ColorUtils.findClosestPaletteColorTo(info.getColor());
			if(new Random().nextFloat() < behaviour.getChance()) {
				behaviour.onBlockTick(world, pos);
			}
		}
	}
}
