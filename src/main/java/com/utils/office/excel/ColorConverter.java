package com.utils.office.excel;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jxl.format.Colour;
import jxl.write.WritableWorkbook;

public class ColorConverter {

	private Colour[] colours = null;
	private Map<String, Colour> colorMap = null;

	public Colour toJXLColour(WritableWorkbook wwb, WebColor webColor) {
		if (colours == null) {
			colours = new Colour[] { Colour.BLUE, Colour.AQUA, Colour.BLUE2,
					Colour.BLUE_GREY, Colour.BRIGHT_GREEN, Colour.BROWN,
					Colour.DARK_BLUE, Colour.DARK_BLUE2, Colour.DARK_GREEN,
					Colour.DARK_PURPLE, Colour.DARK_RED, Colour.DARK_TEAL,
					Colour.DARK_YELLOW };
		}
		if (colorMap == null) {
			colorMap = new ConcurrentHashMap<String, Colour>();
		}
		if (colorMap.containsKey(webColor.getIdentifier())) {
			return colorMap.get(webColor.getIdentifier());
		} else {
			Colour colour = null;
			if (colorMap.size() >= colours.length) {
				colour = colours[0];
			} else {
				colour = colours[colorMap.size()];
				Color color = Color.decode(webColor.getHexCode());
				wwb.setColourRGB(colour, color.getRed(), color.getGreen(),
						color.getBlue());
			}
			colorMap.put(webColor.getIdentifier(), colour);
			return colour;
		}
	}
}
