package com.wynprice.modjam5.common.colorfunctionality;

import java.util.ArrayList;

import com.wynprice.modjam5.common.colorfunctionality.ColorFunction.RangeType;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorBlue;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorGreen;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorPurple;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorRed;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorWhite;
import com.wynprice.modjam5.common.colorfunctionality.colors.NoneColor;

public class ColorFunctions {
	public static final ArrayList<ColorFunction> ALL_FUNCTIONS = new ArrayList<>();
	
	public static final ColorFunction NONE = new NoneColor();

	public static final ColorFunction BLUE = register(new ColorBlue());
	public static final ColorFunction GREEN = register(new ColorGreen());
	public static final ColorFunction PURPLE = register(new ColorPurple());
	public static final ColorFunction RED = register(new ColorRed());
	public static final ColorFunction WHITE = register(new ColorWhite());

	private static ColorFunction register(ColorFunction function) {
		ALL_FUNCTIONS.add(function);
		return function;
	}
}
