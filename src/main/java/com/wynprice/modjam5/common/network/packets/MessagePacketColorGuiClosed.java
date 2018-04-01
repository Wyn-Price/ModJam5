package com.wynprice.modjam5.common.network.packets;

import com.wynprice.modjam5.common.network.MessagePacket;
import com.wynprice.modjam5.common.utils.ColorUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class MessagePacketColorGuiClosed extends MessagePacket<MessagePacketColorGuiClosed>{
	
	private int handOrdinal;
	private int newColor;
	
	public MessagePacketColorGuiClosed() {
	}
	
	public MessagePacketColorGuiClosed(EnumHand hand, int newColor) {
		this.handOrdinal = hand.ordinal();
		this.newColor = newColor;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(handOrdinal);
		buf.writeInt(newColor);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		handOrdinal = buf.readInt();
		newColor = buf.readInt();
	}

	@Override
	public void onReceived(MessagePacketColorGuiClosed message, EntityPlayer player) {
		ColorUtils.setColor(player.getHeldItem(EnumHand.values()[message.handOrdinal]), message.newColor);
	}
}
