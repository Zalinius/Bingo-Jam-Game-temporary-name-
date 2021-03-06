package com.zalinius.bingojam.pieces;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import com.zalinius.bingojam.resources.FontSingleton;
import com.zalinius.bingojam.resources.Palette;
import com.zalinius.zje.architecture.Graphical;
import com.zalinius.zje.physics.Point;

public class TextSpot implements Graphical{
	private Point center;
	private String textString;
	private float fontSize;
	
	public TextSpot(Point center, String textString) {
		this(center, textString, 72);
	}
	
	public TextSpot(Point center, String textString, double fontSize) {
		this.center = center;
		this.textString = textString;
		this.fontSize = (float)fontSize;
	}

	
	@Override
	public void render(Graphics2D g) {
		g.setColor(Palette.BRIGHT);
		drawText(g);
	}
	
	private void drawText(Graphics2D g) {
		Font font = prepareFont();
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);

		double xText = center.x - metrics.stringWidth(textString) / 2.0  ;
		double yText = center.y - metrics.getHeight()/2.0 + metrics.getAscent();

		g.drawString(textString,(float) xText,(float) yText);
	}

	private Font prepareFont() {
		Font font = FontSingleton.getFont();
		font = font.deriveFont((fontSize));
		font = font.deriveFont(Font.BOLD);
		return font;
	}

	

}
