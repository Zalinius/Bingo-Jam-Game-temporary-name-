package com.zalinius.bingojam.pieces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.zalinius.bingojam.physics.Quaternion;
import com.zalinius.bingojam.physics.Vector3;
import com.zalinius.bingojam.resources.Palette;
import com.zalinius.zje.physics.Collideable;
import com.zalinius.zje.physics.Collisions;
import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;

public class Pitfall implements Collideable, Locatable, Slopable{
	
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
	public Rectangle2D.Double shape() {
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
	
	public Vector3 getNormalForSphere(Point position, double radius) {
		if(!shape().contains(position.point2D())) {
			return Vector3.OUT;
		}
		else if (innerShape(radius * 2).contains(position.point2D())){
			return new Vector3(1, 0, 0);
		}
		else {
			Ellipse2D.Double circle = new Ellipse2D.Double(position.x - radius, position.y - radius, 2*radius, 2*radius);
			Iterator<Line2D.Double> it = lines().iterator();
			Line2D.Double closestLine = it.next();
			while (it.hasNext()) {
				Line2D.Double line = it.next();
				if(Collisions.distanceBetweenInfiniteLineAndCircle(circle, line) < Collisions.distanceBetweenInfiniteLineAndCircle(circle, closestLine)) {
					closestLine = line;
				}
			}
			
			double distanceToEdge = Collisions.distanceBetweenInfiniteLineAndCircle(circle, closestLine);
			double slopeAngle = Math.asin(distanceToEdge / (radius));
			Vector3 edge = new Vector3(new Vector(closestLine));
			Vector3 normal = Quaternion.rotateAroundAxis(edge, slopeAngle, Vector3.OUT);
			//Fix edge case, should angle be positive or negative???
			
			return normal.normalize();
		}
	}
	
	private List<Line2D.Double> lines(){
		Rectangle2D.Double rectangle = shape();
		double x = rectangle.x;
		double y = rectangle.y;
		double w = rectangle.width;
		double h = rectangle.height;
		Line2D.Double l1 = new Line2D.Double(x,   y,   x+w, y);
		Line2D.Double l2 = new Line2D.Double(x+w, y,   x+w, y+h);
		Line2D.Double l3 = new Line2D.Double(x+w, y+h, x,   y+h );
		Line2D.Double l4 = new Line2D.Double(x,   y+h, x,   y);
		
		return Arrays.asList(l1, l2, l3, l4);
	}

}
