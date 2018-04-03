package com.wynprice.modjam5.common.colorfunctionality.colors;

import java.util.List;
import java.util.Random;

import com.wynprice.modjam5.common.WorldPaintConfig;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunction.RangeType;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;

public class ColorPink extends ColorFunction {

	public ColorPink() {
		super("pink", WorldPaintConfig.COLOR_VALUES.pinkMin, WorldPaintConfig.COLOR_VALUES.pinkMax, RangeType.HUE);
	}
	
	@Override
	public void onBlockTick(World world, BlockPos pos) {
		Random random = new Random();
		if(random.nextFloat() < WorldPaintConfig.COLOR_FUNCTIONS.pinkSpawnPeacfull) {
			Biome biome = world.getBiome(pos);
			List<SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.CREATURE);
			SpawnListEntry entry = spawnList.get(random.nextInt(spawnList.size()));
			try {
				Entity entity = entry.newInstance(world);
				pos = world.getTopSolidOrLiquidBlock(pos);
				entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
				world.spawnEntity(entity);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
}
