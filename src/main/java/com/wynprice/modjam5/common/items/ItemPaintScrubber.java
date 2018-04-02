package com.wynprice.modjam5.common.items;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.WorldColorsHandler.CapabilityHandler.IDataInfomationProvider;
import com.wynprice.modjam5.common.WorldColorsHandler.DataInfomation;
import com.wynprice.modjam5.common.network.WorldPaintNetwork;
import com.wynprice.modjam5.common.network.packets.MessagePacketSingleBlockUpdate;
import com.wynprice.modjam5.common.network.packets.MessagePacketSyncChunk;
import com.wynprice.modjam5.common.utils.BlockPosHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
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
		BlockPos defaultPos = pos;
		DataInfomation defaultInfo = info;
		if(info.isDefault()) {
			for(EnumFacing dir : EnumFacing.values()) {
				DataInfomation info2 = WorldColorsHandler.getInfo(worldIn, pos.offset(dir));
				if(!info2.isDefault()) {
					info = info2;
					pos = pos.offset(dir);
					break;
				}
			}
		}
		IDataInfomationProvider provider = worldIn.getChunkFromBlockCoords(pos).getCapability(WorldColorsHandler.CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP);
		if(!info.isDefault() && provider != null) {
			if(info.isSpreadable()) {
				List<Chunk> chunkList = Lists.newArrayList();
				List<BlockPos> list = provider.getPositionFromOrigin(info.getOrigin());
				for(int i = 0; i < list.size(); i++) {
					if(list.get(i) == null) {
						continue;
					}
					WorldColorsHandler.putInfo(worldIn, list.get(i), null, false);
					Chunk chunk = worldIn.getChunkFromBlockCoords(list.get(i));
					if(!chunkList.contains(chunk)) {
						chunkList.add(chunk);
					}
				}
				if(!worldIn.isRemote) {
					for(Chunk chunk : chunkList) {
						if(chunkList.indexOf(chunk) == chunkList.size() - 1) {
							Pair<BlockPos, BlockPos> positions = BlockPosHelper.getRange(list);
							WorldPaintNetwork.sendToPlayersInWorld(worldIn, new MessagePacketSyncChunk(chunk, positions.getLeft(), positions.getRight()));
						} else {
							WorldPaintNetwork.sendToPlayersInWorld(worldIn, new MessagePacketSyncChunk(chunk));
						}
					}
				}
			} else if(!worldIn.isRemote) {
				WorldColorsHandler.putInfo(worldIn, defaultPos, null, false);
				WorldPaintNetwork.sendToPlayersInWorld(worldIn, new MessagePacketSingleBlockUpdate(defaultPos, null));
			}

		}
		return EnumActionResult.SUCCESS;
	}
	
	@SideOnly(Side.CLIENT)
	public void refreshChunks() {
		Minecraft.getMinecraft().renderGlobal.loadRenderers();
	}
}
