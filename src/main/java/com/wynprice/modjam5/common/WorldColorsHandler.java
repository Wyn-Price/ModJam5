package com.wynprice.modjam5.common;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;

import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.network.WorldPaintNetwork;
import com.wynprice.modjam5.common.network.packets.MessagePacketRequestCapability;
import com.wynprice.modjam5.common.network.packets.MessagePacketSyncChunk;
import com.wynprice.modjam5.common.utils.BlockPosHelper;
import com.wynprice.modjam5.common.utils.capability.CapabilityHandler;
import com.wynprice.modjam5.common.utils.capability.DataInfomation;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid=WorldPaint.MODID)
public class WorldColorsHandler {
	
	@SubscribeEvent
	public static void chunkRegistry(AttachCapabilitiesEvent<Chunk> event) {
		event.addCapability(new ResourceLocation(WorldPaint.MODID, "colorProvider"), new CapabilityHandler.ColorCapabilityProvider());
	}
	
	private static final ArrayList<ChunkPos> requestedChunks = new ArrayList<>();
	
	public static DataInfomation getInfo(World worldIn, BlockPos pos) {
		if(worldIn == null) return DataInfomation.DEFAULT; //Not usually called
		Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
		CapabilityHandler.IDataInfomationProvider cap = chunk.hasCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) ? chunk.getCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) : null;
		if(cap != null) {
			if(worldIn.isRemote) {
				if(!cap.hasSynced() && !requestedChunks.contains(chunk.getPos())) {
					requestedChunks.add(chunk.getPos());
					WorldPaintNetwork.sendToServer(new MessagePacketRequestCapability(chunk));
				}
				if(cap.hasSynced() && requestedChunks.contains(chunk.getPos())) {
					requestedChunks.remove(chunk.getPos());
				}
			}
			DataInfomation info = cap.getMap().get(pos);
			return info == null ? DataInfomation.DEFAULT : info;
		}
		return DataInfomation.DEFAULT;
	}
	
	public static HashMap<ChunkPos, Pair<Long, ArrayList<BlockPos>>> syncedChunks = new HashMap<>();
	
	public static void putInfo(World worldIn, BlockPos pos, DataInfomation info, boolean doRenderUpdate) {
		Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
		CapabilityHandler.IDataInfomationProvider cap = chunk.hasCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) ? chunk.getCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) : null;
		if(cap != null) {
			if(info == null) {
				cap.getMap().remove(pos);
				
			} else {
				cap.getMap().put(pos, info);
				cap.addPositionToOriginList(info.getOrigin(), pos);
			}
			if(doRenderUpdate && !worldIn.isRemote) {
				if(!syncedChunks.containsKey(chunk.getPos())) {
					syncedChunks.put(chunk.getPos(), Pair.of(worldIn.getTotalWorldTime(), new ArrayList<>()));
				}
				syncedChunks.get(chunk.getPos()).getRight().add(pos);
				if(worldIn.getTotalWorldTime() - syncedChunks.get(chunk.getPos()).getLeft() > 20) {
					Pair<BlockPos, BlockPos> positions = BlockPosHelper.getRange(syncedChunks.get(chunk.getPos()).getRight());
					syncedChunks.put(chunk.getPos(), Pair.of(worldIn.getTotalWorldTime(), new ArrayList<>()));
					WorldPaintNetwork.sendToAll(new MessagePacketSyncChunk(chunk, positions.getLeft(), positions.getRight()));
				}
			}
		}
	}
}
