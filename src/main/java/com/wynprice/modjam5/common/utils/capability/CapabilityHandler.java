package com.wynprice.modjam5.common.utils.capability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
* If you're hear to tell me how bad the capability is, I know. It works so im not touching it
 * @author Wyn Price
 *
 */
public class CapabilityHandler {

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