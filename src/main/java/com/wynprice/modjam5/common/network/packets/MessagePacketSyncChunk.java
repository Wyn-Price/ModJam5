package com.wynprice.modjam5.common.network.packets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.wynprice.modjam5.common.WorldColorsHandler.CapabilityHandler;
import com.wynprice.modjam5.common.WorldColorsHandler.DataInfomation;
import com.wynprice.modjam5.common.WorldColorsHandler.CapabilityHandler.IDataInfomationProvider;
import com.wynprice.modjam5.common.network.MessagePacket;
import com.wynprice.modjam5.common.utils.ByteBufHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public class MessagePacketSyncChunk extends MessagePacket<MessagePacketSyncChunk> {

	private Map<BlockPos, DataInfomation> map;
	private ChunkPos pos;
	private BlockPos fromPos;
	private BlockPos toPos;
	
	boolean refreshChunks;

	private boolean doRenderUpdate = true;	
	
	public MessagePacketSyncChunk() {
	}
	
	public MessagePacketSyncChunk(Chunk chunk) {
		this(chunk, BlockPos.ORIGIN, BlockPos.ORIGIN);
		doRenderUpdate = false;
	}
	
	public MessagePacketSyncChunk(Chunk chunk, BlockPos renderPos) {
		this(chunk, renderPos, renderPos);
	}
	
	public MessagePacketSyncChunk(Chunk chunk, BlockPos fromPos, BlockPos toPos) {
		this.map = chunk.hasCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) ? new HashMap<>(chunk.getCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP).getMap())/*Make immutatable*/ : new HashMap<>();
		this.pos = chunk.getPos();
		this.fromPos = fromPos;
		this.toPos = toPos;
	}
	
	public MessagePacketSyncChunk setRefreshChunk(boolean refreshChunks) {
		this.refreshChunks = refreshChunks;
		return this;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufHelper.writeCapsToBuf(buf, map);
		buf.writeInt(pos.x);
		buf.writeInt(pos.z);
		buf.writeBoolean(refreshChunks);
		buf.writeBoolean(doRenderUpdate);
		if(doRenderUpdate) {
			buf.writeInt(fromPos.getX());
			buf.writeInt(fromPos.getY());
			buf.writeInt(fromPos.getZ());
			
			buf.writeInt(toPos.getX());
			buf.writeInt(toPos.getY());
			buf.writeInt(toPos.getZ());
		}	
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.map = ByteBufHelper.readCapsFromBuf(buf);
		this.pos = new ChunkPos(buf.readInt(), buf.readInt());
		this.refreshChunks = buf.readBoolean();
		this.doRenderUpdate = buf.readBoolean();
		if(doRenderUpdate) {
			this.fromPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			this.toPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		}
	}
	
	@Override
	public void onReceived(MessagePacketSyncChunk message, EntityPlayer player) {
		Chunk chunk = Minecraft.getMinecraft().world.getChunkFromChunkCoords(message.pos.x, message.pos.z);
		if(chunk.hasCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP)) {
			IDataInfomationProvider provider = chunk.getCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP);
			provider.getMap().clear();
			provider.getMap().putAll(message.map);
			provider.sync();
		}
		if(message.refreshChunks) {
			Minecraft.getMinecraft().renderGlobal.loadRenderers();
		}
		if(message.doRenderUpdate) {
			player.world.markBlockRangeForRenderUpdate(message.fromPos, message.toPos);
		}
		
		
	}
}
