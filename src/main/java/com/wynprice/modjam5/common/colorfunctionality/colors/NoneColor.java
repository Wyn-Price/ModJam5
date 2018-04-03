package com.wynprice.modjam5.common.colorfunctionality.colors;

import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunction.RangeType;

public class NoneColor extends ColorFunction {

	public NoneColor() {
		super("unknown", 0, 0, RangeType.HUE);
	}
	
	@Override
	public boolean shouldApply(float[] hsb) {
		return false;
	}

}
