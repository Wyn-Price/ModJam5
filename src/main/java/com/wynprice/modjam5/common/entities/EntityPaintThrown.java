package com.wynprice.modjam5.common.entities;

import java.util.HashMap;

import com.wynprice.modjam5.client.particles.ParticleThrownEntityPaintExplosion;
import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.WorldColorsHandler.DataInfomation;
import com.wynprice.modjam5.common.registries.WorldPaintItems;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.collection.parallel.ParIterableLike.Min;

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
    	float speed = 5f;
    	for(int i = 0; i < 15; i++) {
        	Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleThrownEntityPaintExplosion(world, posX, posY, posZ,
        			((rand.nextDouble() * 2) - 1) * speed,
        			((rand.nextDouble() * 2) - 1) * speed,
        			((rand.nextDouble() * 2) - 1) * speed,
        			color));
    	}
    }

	@Override
	protected void onImpact(RayTraceResult result) {
		if(!this.world.isRemote) {
			this.setDead();
			world.setEntityState(this, (byte)3);
		}
		int rad = this.rand.nextInt(5) + 2;
		for(int x = -rad; x < rad; x++) {
			for(int y = -rad; y < rad; y++) {
				for(int z = -rad; z < rad; z++) {
					WorldColorsHandler.putInfo(this.world, getPosition().add(x, y, z), new WorldColorsHandler.DataInfomation(color, true, getPosition(), new int[0]), false);
				}
			}
		}		
		this.world.playSound(null, getPosition(), SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.NEUTRAL, 3f, (rad - 2) / 5f);
		if(this.world.isRemote) {
			this.world.markBlockRangeForRenderUpdate(getPosition().add(-rad, -rad, -rad), getPosition().add(rad, rad, rad));
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
