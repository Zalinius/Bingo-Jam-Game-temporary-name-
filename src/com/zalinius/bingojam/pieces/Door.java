package com.zalinius.bingojam.pieces;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import com.zalinius.bingojam.physics.CollideableLine;
import com.zalinius.bingojam.resources.Palette;
import com.zalinius.zje.architecture.Graphical;
import com.zalinius.zje.math.Interpolation;
import com.zalinius.zje.physics.Point;

public class Door implements Graphical, CollideableLine{
	private Point p1;
	private Point p2;
	private boolean open;
	private Color color;

	public Door(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
		this.open = false;
		this.color = Palette.DOOR;
	}
	
	public Door(Point p1, Point p2, Color color) {
		this.p1 = p1;
		this.p2 = p2;
		this.open = false;
		this.color = color;
	}



	@Override
	public void render(Graphics2D g) {
		g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(color);

		if(open) {
			Line2D.Double line1 = new Line2D.Double(p1.point2D(), Interpolation.linearInterpolation(p1, p2, 0.05).point2D());
			Line2D.Double line2 = new Line2D.Double(p2.point2D(), Interpolation.linearInterpolation(p1, p2, 0.95).point2D());
			g.draw(line1);
			g.draw(line2);
		}
		else {
			g.draw(line());		
		}

	}

	@Override
	public Line2D.Double line() {
		return new Line2D.Double(p1.point2D(), p2.point2D());
	}


	public boolean isOpen() {
		return open;
	}
	
	public void open() {
		open = true;
	}

}
