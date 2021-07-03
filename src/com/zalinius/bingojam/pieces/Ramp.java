package com.zalinius.bingojam.pieces;

import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import com.zalinius.bingojam.physics.Vector3;
import com.zalinius.bingojam.resources.Palette;
import com.zalinius.zje.architecture.Graphical;
import com.zalinius.zje.math.Interpolation;
import com.zalinius.zje.physics.Collideable;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;

public class Ramp implements Collideable, Graphical, Slopable{
	
	private Vector3 normal;
	
	private Point bottomLeft, bottomRight, topLeft, topRight;
	
	public Ramp(Point center, double width, double height, Vector3 normal) {
		this.topLeft = center.add(-width/2, -height/2);
		this.topRight = center.add(width/2, -height/2);
		this.bottomLeft = center.add(-width/2, height/2);
		this.bottomRight = center.add(width/2, height/2);

		this.normal = normal.normalize();
	}
		
	public Ramp(Point bottomLeft, Point bottomRight, Point topLeft, Point topRight, double elevation) {
		this.normal = new Vector3(new Vector(topLeft, bottomLeft).normalize(), -elevation).normalize();
		this.bottomLeft = bottomLeft;
		this.bottomRight = bottomRight;
		this.topLeft = topLeft;
		this.topRight = topRight;
	}



	@Override
	public void render(Graphics2D g) {
		Point2D lowPoint = Interpolation.linearInterpolation(bottomLeft, bottomRight, 0.5).point2D();
		Point2D highPoint =Interpolation.linearInterpolation(topLeft, topRight, 0.5).point2D();
		Paint gradient = new GradientPaint(lowPoint, Palette.GROUND, highPoint, Palette.BRIGHT);
		g.setPaint(gradient);
		g.fill(shape());
	}

	@Override
	public Shape shape() {
		Path2D.Double shape = new Path2D.Double();
		shape.moveTo(bottomLeft.x, bottomLeft.y);
		shape.lineTo(topLeft.x, topLeft.y);
		shape.lineTo(topRight.x, topRight.y);
		shape.lineTo(bottomRight.x, bottomRight.y);
		shape.closePath();
		return shape;
	}
	
	public Vector3 getNormalForSphere(Point point, double radius) {
		if(shape().contains(point.point2D())) {
			return normal;
		}
		else {
			return Vector3.OUT;
		}
	}
	
	public Wall getOneWayWall() {
		return new Wall(topLeft, topRight, true);
	}
}
