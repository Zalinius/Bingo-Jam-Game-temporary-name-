package com.zalinius.bingojam.pieces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

import com.zalinius.bingojam.physics.CollideableLine;
import com.zalinius.bingojam.resources.Palette;
import com.zalinius.zje.math.Interpolation;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;

public class Wall implements CollideableLine {
	private Point p1;
	private Point p2;
	
	private double bouncyness;
	private boolean oneWay;

	public Wall(Point p1, Point p2) {
		this(p1, p2, 0.1);
	}
	
	public Wall(Point p1, Point p2, boolean oneWay) {
		this.p1 = p1;
		this.p2 = p2;
		this.bouncyness = 0.1;
		this.oneWay = oneWay;

	}
	
	public Wall(Point p1, Point p2, double bouncyness) {
		this.p1 = p1;
		this.p2 = p2;
		this.bouncyness = bouncyness;
		this.oneWay = false;
	}
	
	public void render(Graphics2D g) {
		
		if(oneWay) {
			Point left = Interpolation.linearInterpolation(p1, p2, .25);
			Point right = Interpolation.linearInterpolation(p1, p2, .75);
			Point tip = Interpolation.linearInterpolation(p1, p2, .5);
			tip = tip.add(new Vector(left, right).perpendicularCCW().normalize().scale(new Vector(left, right).length()/4));
			
			g.setColor(Palette.DEBUG);
			Path2D.Double arrow = new Path2D.Double();
			arrow.moveTo(left.x, left.y);
			arrow.lineTo(right.x, right.y);
			arrow.lineTo(tip.x, tip.y);
			arrow.closePath();
			g.draw(arrow);
		}
		
		g.setStroke(Palette.THICK);
		g.setColor(computeBouncynessColor());
		g.draw(line());	

	}
	
	public Line2D.Double line() {
		return new Line2D.Double(p1.point2D(), p2.point2D());
	}
	
	public double bouncyness() {
		return bouncyness;
	}
	
	public boolean oneWay() {
		return oneWay;
	}
	
	private Color computeBouncynessColor() {
		int r = (int) Interpolation.linearInterpolation(Palette.BRIGHT.getRed(), Palette.GREEN.getRed(), bouncyness);
		int g = (int) Interpolation.linearInterpolation(Palette.BRIGHT.getGreen(), Palette.GREEN.getGreen(), bouncyness);
		int b = (int) Interpolation.linearInterpolation(Palette.BRIGHT.getBlue(), Palette.GREEN.getBlue(), bouncyness);
		return new Color(r, g, b);
	}

}
