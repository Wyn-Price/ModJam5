package com.wynprice.modjam5.common.network.packets;

import com.sun.jna.platform.unix.X11.XClientMessageEvent.Data;
import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.WorldColorsHandler.DataInfomation;
import com.wynprice.modjam5.common.network.MessagePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessagePacketSingleBlockUpdate extends MessagePacket<MessagePacketSingleBlockUpdate> {

	private BlockPos fromPos;
	private BlockPos toPos;
	
	public MessagePacketSingleBlockUpdate() {
	}
	
	public MessagePacketSingleBlockUpdate(BlockPos fromPos, BlockPos toPos) {
		this.fromPos = fromPos;
		this.toPos = toPos;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(fromPos.getX());
		buf.writeInt(fromPos.getY());
		buf.writeInt(fromPos.getZ());
		
		buf.writeInt(toPos.getX());
		buf.writeInt(toPos.getY());
		buf.writeInt(toPos.getZ());
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.fromPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.toPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}
	
	
	@Override
	public void onReceived(MessagePacketSingleBlockUpdate message, EntityPlayer player) {
		player.world.markBlockRangeForRenderUpdate(fromPos, toPos);
	}
	
}
