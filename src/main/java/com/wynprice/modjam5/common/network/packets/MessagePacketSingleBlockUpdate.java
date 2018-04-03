package com.wynprice.modjam5.common.network.packets;

import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.network.MessagePacket;
import com.wynprice.modjam5.common.utils.capability.DataInfomation;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

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
