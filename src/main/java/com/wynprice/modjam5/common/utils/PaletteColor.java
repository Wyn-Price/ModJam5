package com.wynprice.modjam5.common.utils;

import com.wynprice.modjam5.common.colorfunctionality.EnumColorBehaviour;

public class PaletteColor {
	
    private final int r;
    private final int g;
    private final int b;
    private final int color;
    private final EnumColorBehaviour behaviour;

    public PaletteColor(EnumColorBehaviour behaviour) {
    	int color = behaviour.getBaseColor().getRGB();
        this.r = ((color & 0xff000000) >>> 24);
        this.g = ((color & 0x00ff0000) >>> 16);
        this.b = ((color & 0x0000ff00) >>> 8);
        this.color = color;
        this.behaviour = behaviour;
    }

    public double distanceTo(int color) {
        double deltaR = this.r - ((color & 0xff000000) >>> 24);
        double deltaG = this.g - ((color & 0x00ff0000) >>> 16);
        double deltaB = this.b - ((color & 0x0000ff00) >>> 8);
        return (deltaR * deltaR) + (deltaG * deltaG) + (deltaB * deltaB);
    }
    
    public EnumColorBehaviour getBehaviour() {
		return behaviour;
	}
}