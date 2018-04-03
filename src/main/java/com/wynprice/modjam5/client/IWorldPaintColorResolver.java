package com.wynprice.modjam5.client;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public interface IWorldPaintColorResolver {
    default int getColorAtPos(Biome biome, BlockPos blockPosition) {
    	return -1;
    }
    
    default int func_180283_a(Biome biome, BlockPos blockPosition) {
    	return -1;
    }
}
