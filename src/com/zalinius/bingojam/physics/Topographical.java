package com.zalinius.bingojam.physics;

import java.awt.geom.Ellipse2D;
import java.util.List;

import com.zalinius.bingojam.pieces.Wall;
import com.zalinius.zje.physics.Point;

public interface Topographical {
	public Vector3 getSurfaceNormal(Point position);
	public List<Wall> getAdjacentWalls(Ellipse2D.Double circle);
}
