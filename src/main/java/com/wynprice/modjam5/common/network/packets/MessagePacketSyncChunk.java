package com.wynprice.modjam5.common.network.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.network.MessagePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

public class MessagePacketSyncChunk extends MessagePacket<MessagePacketSyncChunk> {

	private NBTTagCompound nbt;
	private ChunkPos pos;
	private BlockPos fromPos;
	private BlockPos toPos;

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
		this.nbt = chunk.hasCapability(WorldColorsHandler.CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) ? chunk.getCapabilities().serializeNBT() : new NBTTagCompound();
		this.pos = chunk.getPos();
		this.fromPos = fromPos;
		this.toPos = toPos;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			CompressedStreamTools.writeCompressed(nbt, baos);
			byte[] abyte = baos.toByteArray();
			buf.writeInt(abyte.length);
			for(byte b : abyte) {
				buf.writeByte(b);
			}
			buf.writeInt(pos.x);
			buf.writeInt(pos.z);
			
			buf.writeBoolean(doRenderUpdate);
			
			if(doRenderUpdate) {
				buf.writeInt(fromPos.getX());
				buf.writeInt(fromPos.getY());
				buf.writeInt(fromPos.getZ());
				
				buf.writeInt(toPos.getX());
				buf.writeInt(toPos.getY());
				buf.writeInt(toPos.getZ());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			int size = buf.readInt();
			byte[] abyte = new byte[size];
			for(int i = 0; i < size; i++) {
				abyte[i] = buf.readByte();
			}
			ByteArrayInputStream bais = new ByteArrayInputStream(abyte);
			this.nbt = CompressedStreamTools.readCompressed(bais);
			this.pos = new ChunkPos(buf.readInt(), buf.readInt());
			this.doRenderUpdate = buf.readBoolean();
			if(doRenderUpdate) {
				this.fromPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
				this.toPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
			}
			
		} catch (IndexOutOfBoundsException | IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	@Override
	public void onReceived(MessagePacketSyncChunk message, EntityPlayer player) {
		if(!message.nbt.getKeySet().isEmpty()) {
			Minecraft.getMinecraft().world.getChunkFromChunkCoords(message.pos.x, message.pos.z).getCapabilities().deserializeNBT(message.nbt);
		}
		if(message.doRenderUpdate) {
			player.world.markBlockRangeForRenderUpdate(message.fromPos, message.toPos);
		}
		
		
	}
}
