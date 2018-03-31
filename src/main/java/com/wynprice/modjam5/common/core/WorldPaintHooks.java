package com.wynprice.modjam5.common.core;

import java.lang.reflect.Method;

import com.wynprice.modjam5.client.IWorldPaintColorResolver;
import com.wynprice.modjam5.common.WorldColorsHandler;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class WorldPaintHooks {
		
	public static int getProperBiomeColor(int color, IBlockAccess blockAccess, BlockPos pos, IWorldPaintColorResolver resolver) { //Needed colorResolver to be string as the class is inaccessable
		int type = Integer.valueOf(String.valueOf(resolver.toString().toCharArray()[43])); //1 = grass, 2 = foliage, 3 = water
		int i = 0;
		int j = 0;
		int k = 0;
		
		int blend = 10; //TODO config
		int times = 0;
		for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-blend, 0, -blend), pos.add(blend, 0, blend)))
		{
			times++;
			int l = resolver.getColorAtPos(blockAccess.getBiome(blockpos$mutableblockpos), blockpos$mutableblockpos);
			WorldColorsHandler.DataInfomation info = WorldColorsHandler.getInfo(blockpos$mutableblockpos);
			if(info != WorldColorsHandler.DataInfomation.DEFAULT) {
				l = info.getColor();
			}
			i += (l & 16711680) >> 16;
			j += (l & 65280) >> 8;
			k += l & 255;
		}

		return (i / times & 255) << 16 | (j / times & 255) << 8 | k / times & 255;
	}
}
