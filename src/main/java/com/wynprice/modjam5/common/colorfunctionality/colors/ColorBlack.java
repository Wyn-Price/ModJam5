package com.wynprice.modjam5.common.colorfunctionality.colors;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;

public class ColorBlack extends ColorFunction {

	public ColorBlack() {
		super(0f, 0f, RangeType.HUE);
	}
	
	@Override
	public void onBlockTick(World world, BlockPos pos) {
		Random random = new Random();
		if(random.nextFloat() < 0.005f) {//TODO config
			Biome biome = world.getBiome(pos);
			List<SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.MONSTER);
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
	
	@Override
	public boolean shouldApply(float[] hsb) {
		Color color = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
		return Math.max(color.getRed(), Math.max(color.getGreen(), color.getBlue())) < 55;
	}

}
