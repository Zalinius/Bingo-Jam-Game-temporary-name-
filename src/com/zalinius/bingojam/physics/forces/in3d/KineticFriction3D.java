package com.zalinius.bingojam.physics.forces.in3d;

import com.zalinius.bingojam.physics.forces.Forceful;
import com.zalinius.zje.physics.Mobile;
import com.zalinius.zje.physics.Vector;

public class KineticFriction3D implements Forceful{

	private double frictionCoefficient;
	private Forceful3D normalForce;
	private Mobile object;

	public KineticFriction3D(Mobile object, Forceful3D normalForce, double frictionCoefficient) {
		this.object = object;
		this.normalForce = normalForce;
		this.frictionCoefficient = frictionCoefficient;
	}

	@Override
	public Vector getForce() {
		if(object.speed() == 0) {
			return new Vector();
		}
		else {
			Vector frictionOrientation = object.velocity().reflect().normalize();
			double magnitude = frictionCoefficient * normalForce.getForceMagnitude();
			return frictionOrientation.scale(magnitude);
		}
	}

}
