package com.wynprice.modjam5.common.entities;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import com.wynprice.modjam5.client.particles.ParticleThrownEntityPaintExplosion;
import com.wynprice.modjam5.common.WorldColorsHandler;
import com.wynprice.modjam5.common.WorldPaintConfig;
import com.wynprice.modjam5.common.core.WorldPaintHooks;
import com.wynprice.modjam5.common.utils.BlockPosHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
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
    	ArrayList<BlockPos> posisionList = new ArrayList<>();
		Random rand = new Random(id);
		world.setEntityState(this, (byte)id);
		int rad = rand.nextInt(3) + 4;
		for(int x = -rad; x < rad; x++) {
			for(int y = -rad; y < rad; y++) {
				for(int z = -rad; z < rad; z++) {
					BlockPos pos = getPosition().add(x, y, z);
					if(!WorldPaintConfig.GENERAL.getAllowedBlocks().contains(world.getBlockState(pos).getBlock())) {
						continue;
					}
					posisionList.add(pos);
					WorldColorsHandler.putInfo(this.world, pos, new WorldColorsHandler.DataInfomation(color, true, getPosition(), new int[0]), false);

				}
			}
		}	
    	
		if(this.world.isRemote) {
			Pair<BlockPos, BlockPos> pair = BlockPosHelper.getRange(posisionList);
			this.world.markBlockRangeForRenderUpdate(pair.getLeft(), pair.getRight());
		}
		
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
		if(result.entityHit != null) {
			return;
		}
		if(!this.world.isRemote) {
			this.setDead();
			int id = new Random().nextInt();
			Random rand = new Random(id);
			world.setEntityState(this, (byte)id);
			int rad = rand.nextInt(5) + 2;
			for(int x = -rad; x < rad; x++) {
				for(int y = -rad; y < rad; y++) {
					for(int z = -rad; z < rad; z++) {
						BlockPos pos = getPosition().add(x, y, z);
						if(!WorldPaintConfig.GENERAL.getAllowedBlocks().contains(world.getBlockState(pos).getBlock())) {
							continue;
						}
						if(getPosition().getDistance(pos.getX(), pos.getY(), pos.getZ()) < rad || rand.nextFloat() < 0.3f) {
							WorldColorsHandler.putInfo(this.world, pos, new WorldColorsHandler.DataInfomation(color, true, getPosition(), new int[0]), false);
						}
					}
				}
			}	
			this.world.playSound(null, getPosition(), SoundEvents.ENTITY_SLIME_SQUISH, SoundCategory.NEUTRAL, 3f, (rad - 2) / 5f);
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
