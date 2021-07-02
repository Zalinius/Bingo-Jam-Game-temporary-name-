package com.zalinius.bingojam.physics.forces.in3d;

import com.zalinius.bingojam.physics.Vector3;

public class NormalForce3D implements Forceful3D {
	
	private Vector3 surfaceNormal;
	private Forceful3D cause;
	
	public NormalForce3D(Vector3 surfaceNormal, Forceful3D cause) {
		this.surfaceNormal = surfaceNormal.normalize();
		this.cause = cause;
	}

	@Override
	public Vector3 getForce() {
		double magnitude = Vector3.dotProduct(surfaceNormal, cause.getForce());
		
		if(magnitude < 0) {
			return surfaceNormal.scale(Math.abs(magnitude));
		}
		else {
			return new Vector3();
		}
	}

}
