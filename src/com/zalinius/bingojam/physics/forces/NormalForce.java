package com.zalinius.bingojam.physics.forces;

import com.zalinius.zje.physics.UnitVector;
import com.zalinius.zje.physics.Vector;

public class NormalForce implements Forceful {
	
	private UnitVector surfaceNormal;
	private Forceful cause;
	
	public NormalForce(UnitVector surfaceNormal, Forceful cause) {
		this.surfaceNormal = surfaceNormal;
		this.cause = cause;
	}

	@Override
	public Vector getForce() {
		double magnitude = Vector.dotProduct(surfaceNormal, cause.getForce());
		
		if(magnitude < 0) {
			return surfaceNormal.scale(Math.abs(magnitude));
		}
		else {
			return new Vector();
		}
	}

}
