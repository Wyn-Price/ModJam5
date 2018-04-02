package com.wynprice.modjam5.common.items;

import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.WorldColorsHandler.DataInfomation;
import com.wynprice.modjam5.common.network.WorldPaintNetwork;
import com.wynprice.modjam5.common.network.packets.MessagePacketSingleBlockUpdate;
import com.wynprice.modjam5.common.network.packets.MessagePacketSyncChunk;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPaintBrush extends Item {
	public ItemPaintBrush() {
		this.setRegistryName("paintbrush");
		this.setUnlocalizedName("paintbrush");
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			DataInfomation infomation = new DataInfomation(ColorUtils.getColor(player.getHeldItem(hand)), false, pos, new int[0]);
			WorldColorsHandler.putInfo(worldIn, pos, infomation, false);
			WorldPaintNetwork.sendToPlayersInWorld(worldIn, new MessagePacketSingleBlockUpdate(pos, infomation)); 
		}
		return EnumActionResult.SUCCESS;
	}
}
