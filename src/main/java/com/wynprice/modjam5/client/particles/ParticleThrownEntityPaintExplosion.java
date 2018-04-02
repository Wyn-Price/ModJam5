package com.wynprice.modjam5.client.particles;

import com.wynprice.modjam5.common.registries.WorldPaintItems;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class ParticleThrownEntityPaintExplosion extends Particle {

	private static final TextureAtlasSprite sprite = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(WorldPaintItems.THROWABLE_PAINT);
	
	public ParticleThrownEntityPaintExplosion(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn,
			double xSpeedIn, double ySpeedIn, double zSpeedIn, int color) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		this.setParticleTexture(sprite);
		this.setRBGColorF(((color >> 16) & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, ((color >> 0) & 0xFF) / 255f);
		this.setMaxAge(500);
		this.multiplyVelocity(4);
		this.particleScale = 5f;
		this.particleGravity = 1f;
	}
	
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX,
			float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		 	float f = ((float)this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
	        float f1 = f + 0.015609375F;
	        float f2 = ((float)this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
	        float f3 = f2 + 0.015609375F;
	        float f4 = 0.1F * this.particleScale;

	        if (this.particleTexture != null)
	        {
	            f = this.particleTexture.getInterpolatedU((double)(this.particleTextureJitterX / 4.0F * 16.0F));
	            f1 = this.particleTexture.getInterpolatedU((double)((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F));
	            f2 = this.particleTexture.getInterpolatedV((double)(this.particleTextureJitterY / 4.0F * 16.0F));
	            f3 = this.particleTexture.getInterpolatedV((double)((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F));
	        }

	        float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
	        float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
	        float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);
	        int i = this.getBrightnessForRender(partialTicks);
	        int j = i >> 16 & 65535;
	        int k = i & 65535;
	        buffer.pos((double)(f5 - rotationX * f4 - rotationXY * f4), (double)(f6 - rotationZ * f4), (double)(f7 - rotationYZ * f4 - rotationXZ * f4)).tex((double)f, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
	        buffer.pos((double)(f5 - rotationX * f4 + rotationXY * f4), (double)(f6 + rotationZ * f4), (double)(f7 - rotationYZ * f4 + rotationXZ * f4)).tex((double)f, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
	        buffer.pos((double)(f5 + rotationX * f4 + rotationXY * f4), (double)(f6 + rotationZ * f4), (double)(f7 + rotationYZ * f4 + rotationXZ * f4)).tex((double)f1, (double)f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
	        buffer.pos((double)(f5 + rotationX * f4 - rotationXY * f4), (double)(f6 - rotationZ * f4), (double)(f7 + rotationYZ * f4 - rotationXZ * f4)).tex((double)f1, (double)f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();	
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		particleScale -= 0.15f;
		if(particleScale < 0) {
			this.setExpired();
		}
	}

	@Override
	public int getFXLayer() {
		return 1;
	}
	
}
