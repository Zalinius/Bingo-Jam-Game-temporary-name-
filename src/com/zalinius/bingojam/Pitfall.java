package com.zalinius.bingojam;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import com.zalinius.bingojam.resources.Palette;
import com.zalinius.zje.physics.Collideable;
import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.physics.Point;

public class Pitfall implements Collideable, Locatable{
	
	private Point center;
	private double width, height;
	
	public Pitfall(Point center, double width, double height) {
		this.center = center;
		this.width = width;
		this.height = height;
	}

	public void render(Graphics2D g) {
		Color pitColor = Palette.DEATH;
		Color outlineColor = Palette.STONE;
		
		g.setColor(pitColor);
		g.fill(shape());
		
		g.setColor(outlineColor);
		g.draw(shape());
	}

	@Override
	public Shape shape() {
		Rectangle2D.Double bounds = new Rectangle2D.Double(center.x - width/2, center.y - height/2, width, height);
		return bounds;
	}

	@Override
	public Point position() {
		return center;
	}

	public Shape innerShape(double offset) {
		Rectangle2D.Double bounds = new Rectangle2D.Double(center.x - width/2 + offset, center.y - height/2 + offset, width - 2*offset, height - 2*offset);
		return bounds;
	}

}
