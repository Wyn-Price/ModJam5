package com.wynprice.modjam5.common.colorfunctionality;

import java.util.ArrayList;

import com.wynprice.modjam5.common.colorfunctionality.ColorFunction.RangeType;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorBlack;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorBlue;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorGray;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorGreen;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorOrange;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorPink;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorPurple;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorRed;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorWhite;
import com.wynprice.modjam5.common.colorfunctionality.colors.ColorYellow;
import com.wynprice.modjam5.common.colorfunctionality.colors.NoneColor;

public class ColorFunctions {
	public static final ArrayList<ColorFunction> ALL_FUNCTIONS = new ArrayList<>();
	public static final ArrayList<ColorFunction> AWAY_FUNCTIONS = new ArrayList<>();
	
	public static final ColorFunction NONE = new NoneColor();

	public static final ColorFunction BLUE = register(new ColorBlue());
	public static final ColorFunction GREEN = register(new ColorGreen());
	public static final ColorFunction PURPLE = register(new ColorPurple());
	public static final ColorFunction RED = register(new ColorRed());
	public static final ColorFunction WHITE = register(new ColorWhite());
	public static final ColorFunction YELLOW = register(new ColorYellow());
	public static final ColorFunction GRAY = register(new ColorGray());
	public static final ColorFunction ORANGE = register(new ColorOrange());
	public static final ColorFunction BLACK = register(new ColorBlack());
	public static final ColorFunction PINk = register(new ColorPink());

	private static ColorFunction register(ColorFunction function) {
		ALL_FUNCTIONS.add(function);
		if(function.recieveAwayCalls()) {
			AWAY_FUNCTIONS.add(function);
		}
		return function;
	}
}
