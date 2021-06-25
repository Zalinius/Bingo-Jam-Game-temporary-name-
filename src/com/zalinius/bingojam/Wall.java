package com.zalinius.bingojam;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import com.zalinius.zje.physics.Point;

public class Wall {
	private Point p1;
	private Point p2;
	
	public Wall(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public void render(Graphics2D g) {
		g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(Color.WHITE);
		g.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));	
		g.setColor(Color.DARK_GRAY);
	}
	
	public Line2D.Double line() {
		return new Line2D.Double(p1.point2D(), p2.point2D());
	}

}
