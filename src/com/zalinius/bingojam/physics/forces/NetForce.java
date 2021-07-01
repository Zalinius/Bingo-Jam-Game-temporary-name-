package com.zalinius.bingojam.physics.forces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.zalinius.zje.physics.Vector;

public class NetForce implements Forceful {
	
	private List<Forceful> forces;
	
	public NetForce(Forceful... forces) {
		this.forces = new ArrayList<>(Arrays.asList(forces));
	}

	public void addForce(Forceful force) {
		forces.add(force);
	}

	@Override
	public Vector getForce() {
		Vector netForce = new Vector();
		for (Iterator<Forceful> it = forces.iterator(); it.hasNext();) {
			Forceful forceful = it.next();
			netForce = netForce.add(forceful.getForce());
		}

		return netForce;
	}

}
