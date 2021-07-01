package com.zalinius.bingojam.utilities;

import java.awt.geom.Rectangle2D;

import com.zalinius.zje.physics.Point;

public class Geometry {
	
	public static Rectangle2D.Double centeredSquare(Point center, double width){
		return centeredRectangle(center, width, width);
	}

	public static Rectangle2D.Double centeredRectangle(Point center, double width, double height){
		return new Rectangle2D.Double(center.x - width/2, center.y - height/2, width, height);
	}

}
