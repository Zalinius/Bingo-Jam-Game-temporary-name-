package com.zalinius.bingojam.pieces;

import com.zalinius.bingojam.physics.Vector3;
import com.zalinius.zje.physics.Point;

public interface Slopable {
	public Vector3 getNormalForSphere(Point point, double radius);
}
