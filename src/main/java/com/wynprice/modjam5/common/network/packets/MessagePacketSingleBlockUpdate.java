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

	private BlockPos blockPos;
	private DataInfomation info;
	
	public MessagePacketSingleBlockUpdate() {
	}
	
	public MessagePacketSingleBlockUpdate(BlockPos pos, DataInfomation info) {
		this.blockPos = pos;
		this.info = info;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(blockPos.getX());
		buf.writeInt(blockPos.getY());
		buf.writeInt(blockPos.getZ());
		ByteBufUtils.writeTag(buf, info.serializeNBT());
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		info = DataInfomation.fromNBT(ByteBufUtils.readTag(buf));
	}
	
	
	@Override
	public void onReceived(MessagePacketSingleBlockUpdate message, EntityPlayer player) {
		WorldColorsHandler.putInfo(player.world, message.blockPos, message.info, false);
	}
	
}
