package com.wynprice.modjam5.common.colorfunctionality;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ColorBehaviourManager {
	
	public static interface IAll extends ILivingMobTick, IRandomBlockTick {
		
	}
	
	public static interface ILivingMobTick {
		static ILivingMobTick NULL = (entity) -> {};
		public void onMobTick(EntityLivingBase entity);
	}
	
	public static interface IRandomBlockTick {
		static IRandomBlockTick NULL = (world, pos) -> {};
		public void onBlockTick(World world, BlockPos pos);
	}
}
