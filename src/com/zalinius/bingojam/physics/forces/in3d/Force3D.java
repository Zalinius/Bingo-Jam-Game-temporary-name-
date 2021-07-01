package com.zalinius.bingojam.physics.forces.in3d;

import com.zalinius.bingojam.physics.Vector3;
import com.zalinius.zje.physics.Vector;

public class Force3D implements Forceful3D {
	
	private Vector3 force;
	
	public Force3D(Vector3 force) {
		this.force = force;
	}
	public Force3D(Vector force) {
		this(new Vector3(force));
	}

	@Override
	public Vector3 getForce() {
		return force;
	}

}
