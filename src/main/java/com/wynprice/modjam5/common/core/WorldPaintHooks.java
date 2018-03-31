package com.wynprice.modjam5.common.core;

import java.awt.Color;
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
		
		
		int blend = 1; //TODO config
		int defaultcol = 0x2D3FF4;
		
		boolean foundInfo = false;
		int times = 0;
		for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-blend, -blend, -blend), pos.add(blend, blend, blend)))
		{
			times++;
			int l = resolver.getColorAtPos(blockAccess.getBiome(blockpos$mutableblockpos), blockpos$mutableblockpos);
			WorldColorsHandler.DataInfomation info = WorldColorsHandler.getInfo(blockpos$mutableblockpos);
			if(info != WorldColorsHandler.DataInfomation.DEFAULT) {
				l = info.getColor();
			} else if(type == 3) {
				if(l != 0xFFFFFF) {
					float r1 = ((l >> 16) & 0xFF) / 255f;
					float g1 = ((l >> 8) & 0xFF) / 255f;
					float b1 = (l & 0xFF) / 255f;
					
					float r2 = ((defaultcol >> 16) & 0xFF) / 255f;
					float g2 = ((defaultcol >> 8) & 0xFF) / 255f;
					float b2 = (defaultcol & 0xFF) / 255f;
					
					
					l = new Color(r1 * r2, g1 * g2, b1 * b2).getRGB();
				} else {
					l = defaultcol;
				}
				
			}
			i += (l & 16711680) >> 16;
			j += (l & 65280) >> 8;
			k += l & 255;
		}

		return (i / times & 255) << 16 | (j / times & 255) << 8 | k / times & 255;
	}
}
