package com.zalinius.bingojam.physics.forces;

import com.zalinius.zje.physics.Vector;

public class Force implements Forceful{
	private Vector force;
	
	public Force(Vector force) {
		this.force = force;
	}

	@Override
	public Vector getForce() {
		return force;
	}

}
