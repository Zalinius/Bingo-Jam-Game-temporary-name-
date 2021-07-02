package com.zalinius.bingojam.physics;

import java.awt.geom.Line2D;

public interface CollideableLine {

	public Line2D.Double line();
	public default double bouncyness() {return 0.1;}
	public default boolean oneWay() {return false;}
}
