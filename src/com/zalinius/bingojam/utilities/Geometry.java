package com.zalinius.bingojam.utilities;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;

public class Geometry {

	public static Rectangle2D.Double centeredSquare(Point center, double width){
		return centeredRectangle(center, width, width);
	}
	
	public static RoundRectangle2D.Double centeredRoundedSquare(Point center, double width){
		return new RoundRectangle2D.Double(center.x - width/2, center.y - width/2, width, width, width/4, width/4);
	}

	public static Rectangle2D.Double centeredRectangle(Point center, double width, double height){
		return new Rectangle2D.Double(center.x - width/2, center.y - height/2, width, height);
	}
	

	public static Ellipse2D.Double centeredCircle(Point center, double radius){
		return centeredEllipse(center, 2*radius, 2*radius);
	}

	public static Ellipse2D.Double centeredEllipse(Point center, double width, double height){
		return new Ellipse2D.Double(center.x - width/2, center.y - height/2, width, height);
	}
	
	
	

	public static double combinedBouncyness(double b1, double b2) {
		return Math.sqrt(b1*b2);
	}
	
	
	public static boolean twoCirclesColliding(Ellipse2D.Double c1, Ellipse2D.Double c2) {
		double distanceBetweenCenters = new Vector(c1.getCenterX(), c1.getCenterY(), c2.getCenterX(), c2.getCenterY()).length();
		return distanceBetweenCenters <= (c1.width + c2.width) / 2.0;
	}

}
