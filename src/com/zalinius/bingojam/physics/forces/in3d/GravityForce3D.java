package com.zalinius.bingojam.physics.forces.in3d;

import com.zalinius.bingojam.physics.Vector3;
import com.zalinius.zje.physics.Physical;

public class GravityForce3D implements Forceful3D{
	public static final double EARTH_GRAVITY = 9.81;
	

	private Physical object;
	private double localGravity;
	
	public GravityForce3D(Physical object) {
		this(object, EARTH_GRAVITY);
	}

	public GravityForce3D(Physical object, double localGravity) {
		this.object = object;
		this.localGravity = localGravity;
	}
	
	@Override
	public Vector3 getForce() {
		Vector3 gravityOrientation = Vector3.IN;
		double gravityMagnitude = localGravity * object.mass();
		return gravityOrientation.scale(gravityMagnitude);
	}

}
