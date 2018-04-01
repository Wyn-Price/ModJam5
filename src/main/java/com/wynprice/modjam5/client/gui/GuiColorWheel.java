package com.wynprice.modjam5.client.gui;

import java.awt.Color;
import java.awt.Point;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;

import com.wynprice.modjam5.WorldPaint;
import com.wynprice.modjam5.common.network.WorldPaintNetwork;
import com.wynprice.modjam5.common.network.packets.MessagePacketColorGuiClosed;
import com.wynprice.modjam5.common.registries.WorldPaintItems;
import com.wynprice.modjam5.common.utils.ColorUtils;

import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GuiColorWheel extends GuiScreen
{

	private Point currentColorWheel = new Point(-1, -1);
	
	private int prevColor = Color.WHITE.getRGB();
	
	private final ItemStack stack;
	private final EnumHand hand;
	
	public GuiColorWheel(EnumHand hand, ItemStack stack) 
	{
		this.stack = stack;
		this.hand = hand;
	}
	
	
	@Override
	public void initGui() 
	{
		super.initGui();
		this.currentColorWheel = new Point(Minecraft.getMinecraft().displayWidth / 2, Minecraft.getMinecraft().displayHeight / 2);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		this.drawDefaultBackground();		

		GlStateManager.enableAlpha();
		GlStateManager.disableLighting();
		
		
		int colorPre = getColorUnderMouse();
		

		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(WorldPaint.MODID, "textures/gui/widgits/color_wheel.png"));
		this.drawModalRectWithCustomSizedTexture(this.width / 2 - 64, this.height / 2 - 64, 0, 0, 128, 128, 128, 128);

		int newColor = getColorUnderMouse();
	
		if(colorPre != newColor && Mouse.isButtonDown(0)) {
			this.currentColorWheel = new Point(Mouse.getX(), Mouse.getY());
			this.prevColor = newColor;
		}
		
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(WorldPaint.MODID, "textures/gui/widgits/color_wheel_border.png"));
		this.drawModalRectWithCustomSizedTexture(this.width / 2 - 65, this.height / 2 - 65, 0, 0, 130, 130, 130, 130);
		
		Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation(WorldPaint.MODID, "textures/gui/widgits/color_wheel_cursor.png"));
		this.drawModalRectWithCustomSizedTexture(currentColorWheel.x * new ScaledResolution(this.mc).getScaledWidth() / this.mc.displayWidth - 4, 
				new ScaledResolution(this.mc).getScaledHeight() - currentColorWheel.y * new ScaledResolution(this.mc).getScaledHeight() / this.mc.displayHeight - 1 - 4, 0, 0, 8, 8, 8, 8);
		
		int a = 5;
		
		GlStateManager.scale(a,a,a);
		
		Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(ColorUtils.setColor(new ItemStack(WorldPaintItems.COLORPICKER), prevColor), this.width / (16 * a) * 3 - 8, this.height / (2 * a) - 8);		

		GlStateManager.scale(-a,-a,-a);

		super.drawScreen(mouseX, mouseY, partialTicks);		

	}
	
	@Override
	public void onGuiClosed() {
		WorldPaintNetwork.sendToServer(new MessagePacketColorGuiClosed(hand, prevColor));
		super.onGuiClosed();
	}
	
	public static int getColorUnderMouse()
	{
        IntBuffer intbuffer = BufferUtils.createIntBuffer(1);
        int[] ints = new int[1];
        GlStateManager.glReadPixels(Mouse.getX(), Mouse.getY(), 1, 1, 32993, 33639, intbuffer);
        intbuffer.get(ints);
        return ints[0];
	}

}
