package com.zalinius.bingojam.physics.forces;

import com.zalinius.zje.physics.Vector;

public interface Forceful {
	
	/**
	 * Get the force as a 2D Vector
	 * @return A vector representing the force
	 */
	public Vector getForce();
	
	public default double getForceMagnitude() {return getForce().length();}

}
