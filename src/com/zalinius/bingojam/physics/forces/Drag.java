package com.zalinius.bingojam.physics.forces;

import com.zalinius.zje.physics.Mobile;
import com.zalinius.zje.physics.Vector;

public class Drag implements Forceful {

	private Mobile mobileObject;
	private double magicalCoefficient; //TODO find a better thing for this, This number is proportional to surface area, shape and air density

	public Drag(Mobile mobileObject, double magicalCoefficient) {
		this.mobileObject = mobileObject;
		this.magicalCoefficient = magicalCoefficient;
	}

	@Override
	public Vector getForce() {
		if(mobileObject.velocity().isZeroVector()) {
			return new Vector();
		}
		else {
			double magnitude = magicalCoefficient * Math.pow(mobileObject.speed(), 2);
			return mobileObject.velocity().normalize().reflect().scale(magnitude);
		}
	}

}
