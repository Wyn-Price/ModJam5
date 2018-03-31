package com.wynprice.modjam5.common.network.packets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.network.MessagePacket;
import com.wynprice.modjam5.common.network.WorldPaintNetwork;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MessagePacketRequestCapability extends MessagePacket<MessagePacketRequestCapability> {

	public MessagePacketRequestCapability() {
	}
	
	private ChunkPos pos;
	
	public MessagePacketRequestCapability(Chunk chunk) {
		this.pos = chunk.getPos();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.x);
		buf.writeInt(pos.z);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = new ChunkPos(buf.readInt(), buf.readInt());
	}
	
	@Override
	public void onReceived(MessagePacketRequestCapability message, EntityPlayer player) {
		WorldPaintNetwork.sendToPlayer(player, new MessagePacketSyncChunk(player.world.getChunkFromChunkCoords(message.pos.x, message.pos.z)));
	}

}
