package com.zalinius.bingojam.physics.forces.in3d;

import com.zalinius.bingojam.physics.forces.Forceful;
import com.zalinius.zje.physics.Mobile;
import com.zalinius.zje.physics.Vector;

public class RollingResistance3D implements Forceful {
	
	private Mobile object;
	private NormalForce3D normalForce;
	private double rollingResistanceCoefficient;

	public RollingResistance3D(Mobile object, NormalForce3D normalForce, double rollingResistanceCoefficient) {
		this.object = object;
		this.normalForce = normalForce;
		this.rollingResistanceCoefficient = rollingResistanceCoefficient;
	}

	@Override
	public Vector getForce() {
		if(object.speed() == 0) {
			return new Vector();
		}
		else {
			Vector frictionOrientation = object.velocity().reflect().normalize();
			double magnitude = rollingResistanceCoefficient * normalForce.getForceMagnitude();
			return frictionOrientation.scale(magnitude);
		}
	}

}
