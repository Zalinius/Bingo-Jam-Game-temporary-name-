package com.zalinius.bingojam.resources;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FontSingleton {
	
	private static Font font;

	public static Font getFont() {
		if(font == null) {
			font = findPrioritizedFont();
		}
		return font;
	}
	
	private static Font findPrioritizedFont() {
		for (Iterator<String> it = fontFamilyPriorityOrder().iterator(); it.hasNext();) {
			String fontFamilyName = it.next();
			if(availableFontFamilies().contains(fontFamilyName)) {
				return new Font(fontFamilyName, Font.PLAIN, 1);
			}			
		}
		
		try {
			return packagedFont();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new Font(availableFontFamilies().get(0), Font.PLAIN, 1);
	}
	
	private static List<String> availableFontFamilies(){
		return Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
	}
	
	private static List<String> fontFamilyPriorityOrder(){
		return Arrays.asList("Comic Sans MS");
	}
	
	private static Font packagedFont() throws FontFormatException, IOException {
		return Font.createFont(Font.TRUETYPE_FONT, new File("res/ldfcomicsans-font/Ldfcomicsans-jj7l.ttf"));
	}
}
