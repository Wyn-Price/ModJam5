package com.wynprice.modjam5.common.entities;

import java.util.HashMap;

import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.WorldColorsHandler.DataInfomation;
import com.wynprice.modjam5.common.registries.WorldPaintItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPaintThrown extends EntityThrowable {

	public EntityPaintThrown(World worldIn) {
		super(worldIn);
	}
	
	public EntityPaintThrown(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityPaintThrown(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }
    
    private int color = 0xFFFFFF;
    
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
	            this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, Item.getIdFromItem(WorldPaintItems.THROWABLE_PAINT));
            }
        }
    }

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote)
        {
			this.setDead();
			world.setEntityState(this, (byte)3);
			HashMap<BlockPos, DataInfomation> map = new HashMap<>();
			
			int rad = this.rand.nextInt(5) + 2;
			for(int x = -rad; x < rad; x++) {
				for(int y = -rad; y < rad; y++) {
					for(int z = -rad; z < rad; z++) {
						map.put(getPosition().add(x, y, z), new WorldColorsHandler.DataInfomation(color, true, getPosition(), new int[0]));
					}
				}
			}
			
			WorldColorsHandler.putInfoLargeRenderUpdate(world, map);
        }
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("worldPaintColor", color);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.color = compound.getInteger("worldPaintColor");
	}
	
	public int getColor() {
		return color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
}
