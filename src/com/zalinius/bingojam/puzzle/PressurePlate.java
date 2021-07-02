package com.zalinius.bingojam.puzzle;

import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import com.zalinius.bingojam.resources.Palette;
import com.zalinius.bingojam.utilities.Geometry;
import com.zalinius.zje.architecture.Graphical;
import com.zalinius.zje.physics.Collideable;
import com.zalinius.zje.physics.Point;

public class PressurePlate implements Collideable, Graphical{

	private final Point center;
	private final double width;
	private boolean pressed;
	
	public PressurePlate(Point center, double width) {
		this.center = center;
		this.width = width;
		this.pressed = false;
	}

	@Override
	public RoundRectangle2D.Double shape() {
		return Geometry.centeredRoundedSquare(center, width);
	}
	public RoundRectangle2D.Double smallerShape(double scale) {
		return Geometry.centeredRoundedSquare(center, scale*width);
	}

	
	@Override
	public void render(Graphics2D g) {
		g.setColor(Palette.PLATE);
		g.fill(shape());
		if(pressed) {
			g.setColor(Palette.PLATE);
			g.fill(smallerShape(.75));
			g.setColor(Palette.GROUND);
			g.fill(smallerShape(.5));
		}
		else {
			g.setColor(Palette.GROUND);
			g.fill(smallerShape(.75));
			g.setColor(Palette.PLATE);
			g.fill(smallerShape(.5));

		}
	}
	

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
	public boolean isPressed() {
		return pressed;
	}

}
