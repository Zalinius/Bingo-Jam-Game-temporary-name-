package com.zalinius.bingojam.pieces;

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.zalinius.bingojam.physics.Vector3;
import com.zalinius.bingojam.resources.Palette;
import com.zalinius.zje.architecture.Graphical;
import com.zalinius.zje.physics.Collideable;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;

public class Ramp implements Collideable, Graphical, Slopable{
	
	private Point center;
	private double width, height;
	private Vector3 normal;
	
	public Ramp(Point center, double width, double height, Vector3 normal) {
		this.center = center;
		this.width = width;
		this.height = height;
		this.normal = normal.normalize();
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setStroke(new BasicStroke(5));
		
		Vector direction = normal.project().normalize();
		Point2D lowPoint;
		Point2D highPoint;
		if(direction.x == 0) {
			lowPoint = center.add(direction.scale(height/2)).point2D();
			highPoint = center.add(direction.scale(-height/2)).point2D();
		}
		else {
			lowPoint = center.add(direction.scale(width/2)).point2D();
			highPoint = center.add(direction.scale(-width/2)).point2D();			
		}
		Paint gradient = new GradientPaint(lowPoint, Palette.GROUND, highPoint, Palette.BRIGHT);
		g.setPaint(gradient);
		g.fill(shape());
	}

	@Override
	public Shape shape() {
		return new Rectangle2D.Double(center.x - width/2 , center.y - height / 2, width, height);
	}
	
	public Vector3 getNormalForSphere(Point point, double radius) {
		if(shape().contains(point.point2D())) {
			return normal;
		}
		else {
			return Vector3.OUT;
		}
	}
	
}
