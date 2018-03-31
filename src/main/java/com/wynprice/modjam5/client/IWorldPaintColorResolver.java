package com.wynprice.modjam5.client;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public interface IWorldPaintColorResolver {
    int getColorAtPos(Biome biome, BlockPos blockPosition);
}
