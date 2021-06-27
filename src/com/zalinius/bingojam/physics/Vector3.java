package com.zalinius.bingojam.physics;

import com.zalinius.zje.physics.Vector;

public class Vector3 {

	public static final Vector3 OUT = new Vector3(0, 0, 1);
	
	
	public final double x;
	public final double y;
	public final double z;

	public Vector3() {
		this(0,0,0);
	}

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(Vector xyVector) {
		this(xyVector.x, xyVector.y, 0);
	}

	public double length() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	public Vector3 scale(double k) {
		return new Vector3(k*x, k*y, k*z);
	}
	
	public Vector3 normalize() {
		return this.scale(1/length());
	}
	
	/**
	 * Projects a 3D vector onto the XY-plane
	 * @return The 2D projection of the vector on the XY-plane
	 */
	public Vector project() {
		return new Vector(x, y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3 other = (Vector3) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "<" + x + ", " + y + ", " + z + ">";
	}

	
	

}