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
        this.r = (color>>16)&0xFF;
        this.g = (color>>8)&0xFF;
        this.b = (color>>0)&0xFF;
        this.color = color;
        this.behaviour = behaviour;
    }

    public double distanceTo(int color) {
        int deltaR = this.r - (color>>16)&0xFF;
        int deltaG = this.g - (color>>8)&0xFF;
        int deltaB = this.b - (color>>0)&0xFF;
        return ((deltaR * deltaR) + (deltaG * deltaG) + (deltaB * deltaB));
    }
    
    public EnumColorBehaviour getBehaviour() {
		return behaviour;
	}
}