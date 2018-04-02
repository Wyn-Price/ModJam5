package com.wynprice.modjam5.common.items;

import java.util.List;

import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.WorldColorsHandler.CapabilityHandler.IDataInfomationProvider;
import com.wynprice.modjam5.common.WorldColorsHandler.DataInfomation;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPaintScrubber extends Item {
	public ItemPaintScrubber() {
		setRegistryName("paint_scrubber");
		setUnlocalizedName("paint_scrubber");
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		DataInfomation info = WorldColorsHandler.getInfo(worldIn, pos);
		IDataInfomationProvider provider = worldIn.getChunkFromBlockCoords(pos).getCapability(WorldColorsHandler.CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP);
		if(!info.isDefault() && provider != null) {
			List<BlockPos> list = provider.getPositionFromOrigin(info.getOrigin());
			for(int i = 0; i < list.size(); i++) {
				WorldColorsHandler.putInfo(worldIn, list.get(i), null, false);
			}
			if(!list.isEmpty() && worldIn.isRemote) {
				refreshChunks();
			}
		}
		
		return EnumActionResult.SUCCESS;
	}
	
	@SideOnly(Side.CLIENT)
	public void refreshChunks() {
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
}
