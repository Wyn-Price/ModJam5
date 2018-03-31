
package com.wynprice.modjam5.client;

import com.wynprice.modjam5.common.CommonProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import scala.collection.parallel.ParIterableLike.Min;

public class ClientProxy extends CommonProxy {
	
	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}
}	
