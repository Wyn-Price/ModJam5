package com.wynprice.modjam5.common.network.packets;

import com.sun.jna.platform.unix.X11.XClientMessageEvent.Data;
import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.WorldColorsHandler.DataInfomation;
import com.wynprice.modjam5.common.network.MessagePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessagePacketSingleBlockUpdate extends MessagePacket<MessagePacketSingleBlockUpdate> {

	private BlockPos pos;
	private DataInfomation info;
	
	public MessagePacketSingleBlockUpdate() {
	}
	
	public MessagePacketSingleBlockUpdate(BlockPos pos, DataInfomation info) {
		this.pos = pos;
		this.info = info;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		buf.writeBoolean(info != null);
		if(info != null) {
			info.writeToByteBuf(buf);
		}
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = BlockPos.fromLong(buf.readLong());
		if(buf.readBoolean()) {
			this.info = DataInfomation.fromByteBuf(buf);
		}
	}
	
	
	@Override
	public void onReceived(MessagePacketSingleBlockUpdate message, EntityPlayer player) {
		WorldColorsHandler.putInfo(player.world, message.pos, message.info, false);
		player.world.markBlockRangeForRenderUpdate(message.pos, message.pos);
	}
	
}
