package com.wynprice.modjam5.common.network.packets;

import com.wynprice.modjam5.common.network.MessagePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessagePacketSyncEntity extends MessagePacket<MessagePacketSyncEntity> {

	private int entityID; 
	private NBTTagCompound tag;
	
	public MessagePacketSyncEntity() {
	}
	
	public MessagePacketSyncEntity(Entity entity) {
		this.entityID = entity.getEntityId();
		this.tag = entity.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID);
		ByteBufUtils.writeTag(buf, tag);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		this.entityID = buf.readInt();
		this.tag = ByteBufUtils.readTag(buf);
	}
	
	@Override
	public void onReceived(MessagePacketSyncEntity message, EntityPlayer player) {
		Entity entity = player.world.getEntityByID(message.entityID);
		if(entity != null) {
			entity.readFromNBT(message.tag);
		}
	}

}
