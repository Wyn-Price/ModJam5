package com.wynprice.modjam5.common.network.packets;

import java.awt.Point;

import com.wynprice.modjam5.common.network.MessagePacket;
import com.wynprice.modjam5.common.utils.ColorUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

public class MessagePacketColorGuiClosed extends MessagePacket<MessagePacketColorGuiClosed>{
	
	private int handOrdinal;
	private int newColor;
	private Point colorPoint;
	private float lightness;
	
	public MessagePacketColorGuiClosed() {
	}
	
	public MessagePacketColorGuiClosed(EnumHand hand, int newColor, Point colorPoint, float lightness) {
		this.handOrdinal = hand.ordinal();
		this.newColor = newColor;
		this.colorPoint = colorPoint;
		this.lightness = lightness;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(handOrdinal);
		buf.writeInt(newColor);
		buf.writeInt(colorPoint.x);
		buf.writeInt(colorPoint.y);
		buf.writeFloat(lightness);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		handOrdinal = buf.readInt();
		newColor = buf.readInt();
		this.colorPoint = new Point(buf.readInt(), buf.readInt());
		this.lightness = buf.readFloat();
	}

	@Override
	public void onReceived(MessagePacketColorGuiClosed message, EntityPlayer player) {
		ItemStack stack = player.getHeldItem(EnumHand.values()[message.handOrdinal]);
		ColorUtils.setColor(stack, message.newColor);
		NBTTagCompound tag = stack.getOrCreateSubCompound("worldpaint");
		tag.setInteger("pointX", message.colorPoint.x);
		tag.setInteger("pointY", message.colorPoint.y);
		tag.setFloat("lightnessValue", message.lightness);

	}
}
