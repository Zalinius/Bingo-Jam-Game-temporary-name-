package com.zalinius.bingojam.physics.forces.in3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.zalinius.bingojam.physics.Vector3;

public class NetForce3D implements Forceful3D {
	
	private List<Forceful3D> forces;
	
	public NetForce3D(Forceful3D... forces) {
		this.forces = new ArrayList<>(Arrays.asList(forces));
	}

	public void addForce(Forceful3D force) {
		forces.add(force);
	}

	@Override
	public Vector3 getForce() {
		Vector3 netForce = new Vector3();
		for (Iterator<Forceful3D> it = forces.iterator(); it.hasNext();) {
			Forceful3D forceful = it.next();
			netForce = netForce.add(forceful.getForce());
		}

		return netForce;
	}

}
