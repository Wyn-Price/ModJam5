package com.wynprice.modjam5.common.utils;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.util.math.BlockPos;

public class BlockPosHelper {
	public static Pair<BlockPos, BlockPos> getRange(ArrayList<BlockPos> posList) {
		if(posList.isEmpty()) {
			return Pair.of(BlockPos.ORIGIN, BlockPos.ORIGIN);		
		}
		int minX = posList.get(0).getX();
		int minY = posList.get(0).getY();
		int minZ = posList.get(0).getZ();
		int maxX = posList.get(0).getX();
		int maxY = posList.get(0).getY();
		int maxZ = posList.get(0).getZ();
		
		for(BlockPos pos : posList) {
			minX = Math.min(minX, pos.getX());
			minY = Math.min(minY, pos.getY());
			minZ = Math.min(minZ, pos.getZ());
			
			maxX = Math.max(maxX, pos.getX());
			maxY = Math.max(maxY, pos.getY());
			maxZ = Math.max(maxZ, pos.getZ());
		}
		return Pair.of(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ));
	}
}
