package com.zalinius.bingojam;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import com.zalinius.bingojam.resources.FontFactory;
import com.zalinius.bingojam.resources.Palette;
import com.zalinius.zje.architecture.GameObject;
import com.zalinius.zje.physics.Collideable;
import com.zalinius.zje.physics.Point;

public class LetterTile implements GameObject, Collideable{

	private final String letter;
	private final Point center;
	private final double width;

	private boolean pressed;

	public LetterTile(char letter, Point center) {
		this.letter = Character.toString(letter);
		this.center = center;
		this.width = 50;
		this.pressed = false;
	}


	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub
	}
	@Override
	public void render(Graphics2D g) {
		g.setStroke(new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
		g.setColor(Palette.BRIGHT);
		g.setFont(prepareFont());

		if(pressed) {
			g.fill(shape());
			g.setColor(Palette.GROUND);
			drawLetter(g);
		}
		else {
			g.draw(shape());
			drawLetter(g);
		}
	}

	@Override
	public RoundRectangle2D.Double shape() {
		return new RoundRectangle2D.Double(center.x - width/2, center.y - width/2, width, width, width/2, width/2);
	}


	public void press() {
		if(!pressed) {
			pressed = true;
		}
	}
	
	private void drawLetter(Graphics2D g) {
		Font font = prepareFont();
		FontMetrics metrics = g.getFontMetrics(font);
		
	    double xLetter = center.x - metrics.stringWidth(letter) / 2  ;
	    double yLetter = center.y - metrics.getHeight()/2 + metrics.getAscent();
	    
		g.drawString(letter,(float) xLetter,(float) yLetter);

	}

	private Font prepareFont() {
		Font font = FontFactory.getFont();
		font = font.deriveFont((float) (width*.75));
		font = font.deriveFont(Font.BOLD);
		return font;
	}



}
