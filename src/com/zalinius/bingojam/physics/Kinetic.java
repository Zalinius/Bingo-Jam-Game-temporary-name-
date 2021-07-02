package com.zalinius.bingojam.physics;

import java.awt.geom.Ellipse2D;

import com.zalinius.zje.physics.Physical;

public interface Kinetic {
	public Physical getPhysical();
	public Ellipse2D.Double shape();
}
