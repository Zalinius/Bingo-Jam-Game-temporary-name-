package com.zalinius.bingojam.physics;

import com.zalinius.zje.physics.Physical;
import com.zalinius.zje.physics.Vector;

public class Friction {

	public static Vector dynamicFriction(Physical object, double coefficient) {
		if(object.velocity().isZeroVector()) {
			return new Vector();
		}
		return object.velocity().normalize().reflect().scale(coefficient).scale(object.mass());
	}

	public static double staticFrictionThreshold(Physical object, double staticCoefficient) {
		return object.mass() * staticCoefficient;
	}
}
