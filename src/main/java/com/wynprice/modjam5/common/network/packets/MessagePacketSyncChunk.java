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
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessagePacketSyncChunk extends MessagePacket<MessagePacketSyncChunk> {

	public MessagePacketSyncChunk() {
	}
	
	private NBTTagCompound nbt;
	private ChunkPos pos;
	
	public MessagePacketSyncChunk(Chunk chunk) {
		this.nbt = chunk.hasCapability(WorldColorsHandler.CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) ? chunk.getCapabilities().serializeNBT() : new NBTTagCompound();
		this.pos = chunk.getPos();
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
		} catch (IndexOutOfBoundsException | IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	@Override
	public void onReceived(MessagePacketSyncChunk message, EntityPlayer player) {
		if(!message.nbt.getKeySet().isEmpty()) {
			Minecraft.getMinecraft().world.getChunkFromChunkCoords(message.pos.x, message.pos.z).getCapabilities().deserializeNBT(message.nbt);
		}
	}
}
