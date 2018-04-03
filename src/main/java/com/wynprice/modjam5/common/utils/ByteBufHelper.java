package com.wynprice.modjam5.common.utils;

import java.util.HashMap;
import java.util.Map;

import com.wynprice.modjam5.common.utils.capability.DataInfomation;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;

public class ByteBufHelper {
	public static void writeCapsToBuf(ByteBuf buf, Map<BlockPos, DataInfomation> map) {
		buf.writeInt(map.size());
		for(Map.Entry<BlockPos, DataInfomation> entry : map.entrySet()) {
			buf.writeLong(entry.getKey().toLong());
			entry.getValue().writeToByteBuf(buf);
		}
	}
	
	public static Map<BlockPos, DataInfomation> readCapsFromBuf(ByteBuf buf) {
		Map<BlockPos, DataInfomation> map = new HashMap<>();
		int size = buf.readInt();
		for(int i = 0; i < size; i++) {
			map.put(BlockPos.fromLong(buf.readLong()), new DataInfomation(buf.readInt(), buf.readBoolean(), BlockPos.fromLong(buf.readLong()), readIntArray(buf)));
		}
		return map;
	}
	
	public static void writeIntArray(ByteBuf buf, int[] aint) {
		buf.writeInt(aint.length);
		for(int i : aint) {
			buf.writeInt(i);
		}
	}
	
	public static int[] readIntArray(ByteBuf buf) {
		int size = buf.readInt();
		int[] aint = new int[size];
		for(int i = 0; i < size; i++) {
			aint[i] = buf.readInt();
		}
		return aint;
	}
	
	public static void writeLongArray(ByteBuf buf, long[] along) {
		buf.writeInt(along.length);
		for(long l : along) {
			buf.writeLong(l);
		}
	}
	
	public static long[] readLongArray(ByteBuf buf) {
		int size = buf.readInt();
		long[] along = new long[size];
		for(int i = 0; i < size; i++) {
			along[i] = buf.readInt();
		}
		return along;
	}
}
