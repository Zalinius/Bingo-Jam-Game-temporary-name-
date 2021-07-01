package com.zalinius.bingojam.physics.forces.in3d;

import com.zalinius.bingojam.physics.Vector3;
import com.zalinius.zje.physics.Mobile;

public class StaticFriction3D implements Forceful3D {
	
	private Mobile object;
	private Forceful3D cause;
	private Forceful3D normalForce;
	private double frictionCoefficient;

	public StaticFriction3D(Mobile object, Forceful3D cause, Forceful3D normalForce, double frictionCoefficient) {
		this.object = object;
		this.cause = cause;
		this.normalForce = normalForce;
		this.frictionCoefficient = frictionCoefficient;
	}

	@Override
	public Vector3 getForce() {
		if(object.speed() != 0) {
			return new Vector3();
		}
		else {
			double staticFrictionThreshold = normalForce.getForceMagnitude() * frictionCoefficient;
			
			if(cause.getForceMagnitude() < staticFrictionThreshold) {
				return cause.getForce().reflect();
			}
			else {
				return cause.getForce().reflect().normalize().scale(staticFrictionThreshold);
			}
			
		}
	}

}
