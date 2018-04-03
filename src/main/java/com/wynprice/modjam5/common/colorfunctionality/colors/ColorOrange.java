package com.wynprice.modjam5.common.colorfunctionality.colors;

import com.wynprice.modjam5.common.WorldPaintConfig;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunction;
import com.wynprice.modjam5.common.colorfunctionality.ColorFunction.RangeType;

public class ColorOrange extends ColorFunction {

	public ColorOrange() {
		super("orange", WorldPaintConfig.COLOR_VALUES.orangeMin, WorldPaintConfig.COLOR_VALUES.orangeMax, RangeType.HUE);
	}

}
