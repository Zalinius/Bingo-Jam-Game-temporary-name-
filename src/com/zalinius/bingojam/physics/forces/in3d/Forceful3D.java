package com.zalinius.bingojam.physics.forces.in3d;

import com.zalinius.bingojam.physics.Vector3;

public interface Forceful3D {

	/**
	 * Get the force as a 3D Vector
	 * @return A vector representing the force
	 */
	public Vector3 getForce();
	
	public default double getForceMagnitude() {return getForce().length();}

}
