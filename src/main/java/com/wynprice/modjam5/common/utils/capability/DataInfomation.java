package com.wynprice.modjam5.common.utils.capability;

import com.wynprice.modjam5.common.utils.ByteBufHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class DataInfomation {
			
	public static final DataInfomation DEFAULT = new DataInfomation(-1, false, BlockPos.ORIGIN, new int[0]);
	
	private final int color;
	private final boolean isSpreadable;
	private final BlockPos origin;
	private int[] spreadTo = new int[0];
	
	public DataInfomation(int color, boolean isSpreadable, BlockPos origin, int[] spreadTo) {
		this.color = color;
		this.isSpreadable = isSpreadable;
		this.origin = origin;
		this.spreadTo = spreadTo;

	}
	
	public int getColor() {
		return color;
	}
	
	public boolean isSpreadable() {
		return isSpreadable;
	}

	public final boolean isDefault() {
		return this == DEFAULT;
	}
	
	public BlockPos getOrigin() {
		return origin;
	}
	
	public int[] getSpreadTo() {
		return spreadTo;
	}
	
	public void addFace(EnumFacing face) {
		int[] aint = new int[this.spreadTo.length + 1];
		for(int i = 0; i < this.spreadTo.length; i++) {
			aint[i] = this.spreadTo[i];
		}
		aint[this.spreadTo.length] = face.getIndex();
	}
	
	public static DataInfomation fromNBT(NBTTagCompound data) {
		return new DataInfomation(
						data.getInteger("color"), 
						data.getBoolean("doesSpread"), 
						BlockPos.fromLong(data.getLong("originPos")), 
						data.getIntArray("spreadTo"));
	}
	
	public static DataInfomation fromByteBuf(ByteBuf buf) {
		return new DataInfomation(buf.readInt(),
				buf.readBoolean(),
				BlockPos.fromLong(buf.readLong()),
				ByteBufHelper.readIntArray(buf));
	}

	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("color", this.getColor());
		nbt.setBoolean("doesSpread", this.isSpreadable());
		nbt.setLong("originPos", this.getOrigin().toLong());
		nbt.setIntArray("spreadTo", this.getSpreadTo());
		return nbt;
	}
	
	public void writeToByteBuf(ByteBuf buf) {
		buf.writeInt(this.getColor());
		buf.writeBoolean(this.isSpreadable());
		buf.writeLong(this.getOrigin().toLong());
		ByteBufHelper.writeIntArray(buf, this.getSpreadTo());
	}
}