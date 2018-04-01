package com.wynprice.modjam5.common.items;

import com.wynprice.modjam5.common.entities.EntityPaintThrown;
import com.wynprice.modjam5.common.network.WorldPaintNetwork;
import com.wynprice.modjam5.common.network.packets.MessagePacketSyncEntity;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemThrowablePaint extends Item
{
    public ItemThrowablePaint() {
        this.maxStackSize = 16;
        this.setRegistryName("throwable_paint");
        this.setUnlocalizedName("throwable_paint");
    }

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote)
        {
            EntityPaintThrown entityPaintThrown = new EntityPaintThrown(worldIn, playerIn);
            entityPaintThrown.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            entityPaintThrown.setColor(ColorUtils.getColor(itemstack));
            worldIn.spawnEntity(entityPaintThrown);
            WorldPaintNetwork.sendToPlayersInWorld(worldIn, new MessagePacketSyncEntity(entityPaintThrown));
        }

        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
}
