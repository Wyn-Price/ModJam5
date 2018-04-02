package com.wynprice.modjam5.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.crypto.Data;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.network.WorldPaintNetwork;
import com.wynprice.modjam5.common.network.packets.MessagePacketRequestCapability;
import com.wynprice.modjam5.common.network.packets.MessagePacketSyncChunk;
import com.wynprice.modjam5.common.utils.BlockPosHelper;
import com.wynprice.modjam5.common.utils.ByteBufHelper;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid=WorldPaint.MODID)
public class WorldColorsHandler {
	
	@SubscribeEvent
	public static void chunkRegistry(AttachCapabilitiesEvent<Chunk> event) {
		event.addCapability(new ResourceLocation(WorldPaint.MODID, "colorProvider"), new CapabilityHandler.ColorCapabilityProvider());
	}
	
	private static final ArrayList<ChunkPos> requestedChunks = new ArrayList<>();
	
	public static DataInfomation getInfo(World worldIn, BlockPos pos) {
		if(worldIn == null) return DataInfomation.DEFAULT; //Not usually called
		Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
		CapabilityHandler.IDataInfomationProvider cap = chunk.hasCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) ? chunk.getCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) : null;
		if(cap != null) {
			if(worldIn.isRemote) {
				if(!cap.hasSynced() && !requestedChunks.contains(chunk.getPos())) {
					requestedChunks.add(chunk.getPos());
					WorldPaintNetwork.sendToServer(new MessagePacketRequestCapability(chunk));
				}
				if(cap.hasSynced() && requestedChunks.contains(chunk.getPos())) {
					requestedChunks.remove(chunk.getPos());
				}
			}
			DataInfomation info = cap.getMap().get(pos);
			return info == null ? DataInfomation.DEFAULT : info;
		}
		return DataInfomation.DEFAULT;
	}
	
	public static HashMap<ChunkPos, Pair<Long, ArrayList<BlockPos>>> syncedChunks = new HashMap<>();
	
	public static void putInfo(World worldIn, BlockPos pos, DataInfomation info, boolean doRenderUpdate) {
		Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
		CapabilityHandler.IDataInfomationProvider cap = chunk.hasCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) ? chunk.getCapability(CapabilityHandler.DATA_CAPABILITY, EnumFacing.UP) : null;
		if(cap != null) {
			if(info == null) {
				cap.getMap().remove(pos);
				
			} else {
				cap.getMap().put(pos, info);
				cap.addPositionToOriginList(info.getOrigin(), pos);
			}
			if(doRenderUpdate && !worldIn.isRemote) {
				if(!syncedChunks.containsKey(chunk.getPos())) {
					syncedChunks.put(chunk.getPos(), Pair.of(worldIn.getTotalWorldTime(), new ArrayList<>()));
				}
				syncedChunks.get(chunk.getPos()).getRight().add(pos);
				if(worldIn.getTotalWorldTime() - syncedChunks.get(chunk.getPos()).getLeft() > 20) {
					Pair<BlockPos, BlockPos> positions = BlockPosHelper.getRange(syncedChunks.get(chunk.getPos()).getRight());
					syncedChunks.put(chunk.getPos(), Pair.of(worldIn.getTotalWorldTime(), new ArrayList<>()));
					WorldPaintNetwork.sendToAll(new MessagePacketSyncChunk(chunk, positions.getLeft(), positions.getRight()));
				}
			}
		}
	}
		
	public static class DataInfomation {
				
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
	
	/**
	 * If you're hear to tell me how bad the capability is, I know. It works so im not touching it
	 * @author Wyn Price
	 *
	 */
	public static class CapabilityHandler {
	
		@CapabilityInject(IDataInfomationProvider.class)
	    public static Capability<IDataInfomationProvider> DATA_CAPABILITY = null;
		
		public static class ColorCapabilityProvider implements ICapabilitySerializable<NBTBase> {
	
			private final DefaultImpl defaultImpl = new DefaultImpl();
			
			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
				if(capability == DATA_CAPABILITY) {
					return true;
				}
				return false;
			}
	
			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
				if(capability == DATA_CAPABILITY) {
					return (T) defaultImpl;
				}
				return null;
			}

			@Override
			public NBTBase serializeNBT() {
				return ColorStorage.INSTANCE.writeNBT(DATA_CAPABILITY, defaultImpl, EnumFacing.UP);
			}

			@Override
			public void deserializeNBT(NBTBase nbt) {
				ColorStorage.INSTANCE.readNBT(DATA_CAPABILITY, defaultImpl, EnumFacing.UP, nbt);
			}			
		}
		
		public static interface IDataInfomationProvider {
			Map<BlockPos, DataInfomation> getMap();
			
			List<BlockPos> getPositionFromOrigin(BlockPos origin);
						
			void clear();
			
			void addPositionToOriginList(BlockPos origin, BlockPos pos); 
			
			boolean hasSynced();
						
			/**Internal use only*/
			void sync();
		}
		
		public static class DefaultImpl implements IDataInfomationProvider {
			private final ConcurrentHashMap<BlockPos, DataInfomation> map = new ConcurrentHashMap();
			private final HashMap<BlockPos, List<BlockPos>> originMap = new HashMap<>();
			private boolean synced;
			
			@Override
			public synchronized Map<BlockPos, DataInfomation> getMap() {
				return map;
			}
			
			@Override
			public List<BlockPos> getPositionFromOrigin(BlockPos origin) {
				List<BlockPos> list = originMap.get(origin);
				return list == null ? new ArrayList<>() : list;
			}

			@Override
			public void clear() {
				originMap.clear();
				map.clear();
			}
			
			@Override
			public void addPositionToOriginList(BlockPos origin, BlockPos pos) {
				List<BlockPos> list = originMap.get(origin);
				if(list == null) {
					originMap.put(origin, Lists.newArrayList(pos));
				} else {
					list.add(pos);
				}
			}
			
			@Override
			public void sync() {
				this.synced = true;
			}
			
			@Override
			public boolean hasSynced() {
				return synced;
			}
		}
		
		public static enum ColorStorage implements IStorage<IDataInfomationProvider> {
			
			INSTANCE;
			
			@Override
			public NBTBase writeNBT(Capability<IDataInfomationProvider> capability, IDataInfomationProvider instance,
					EnumFacing side) {
				NBTTagCompound nbt = new NBTTagCompound();
				for(BlockPos pos : instance.getMap().keySet()) {
					nbt.setTag(pos.getX() + " " + pos.getY() + " " + pos.getZ(), instance.getMap().get(pos).serializeNBT());
				}
				return nbt;
			}

			@Override
			public void readNBT(Capability<IDataInfomationProvider> capability, IDataInfomationProvider instance,
					EnumFacing side, NBTBase nbt) {
	            NBTTagCompound tag = (NBTTagCompound) nbt;
				for(String key : tag.getKeySet()) {
					NBTTagCompound data = tag.getCompoundTag(key);
					BlockPos pos = new BlockPos(Integer.valueOf(key.split(" ")[0]), Integer.valueOf(key.split(" ")[1]), Integer.valueOf(key.split(" ")[2]));
					DataInfomation info = DataInfomation.fromNBT(data);
					instance.getMap().put(pos, info);
					instance.addPositionToOriginList(info.getOrigin(), pos);
				}
				instance.sync();
			}
		}	
	}
}
