package com.zalinius.bingojam;

public class Quaternion {
	private final double a, b, c, d;
	
	/**
	 * Builds an identity quaternion (no rotation)
	 */
	public Quaternion() {
		this(1, 0, 0, 0);
	}
	
	private Quaternion(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	public static Quaternion buildQuaternion(Vector3 rotationAxis, double radians) {
		if(rotationAxis.length() == 0) {
			throw new ArithmeticException("can't rotate around 0-vector");
		}
		rotationAxis = rotationAxis.normalize();
		double cos = Math.cos(radians / 2);
		double sin = Math.sin(radians / 2);
		return new Quaternion(cos, sin * rotationAxis.x, sin * rotationAxis.y, sin * rotationAxis.z);
	}
	
	public Vector3 rotate(Vector3 vector) {
		final double x = vector.x;
		final double y = vector.y;
		final double z = vector.z;

		double xP = (a*a + b*b - c*c - d*d)*x + (-2*a*d + 2*b*c)*y +  (2*a*c + 2*b*d)*z;
		double yP = (a*a - b*b + c*c - d*d)*y +  (2*a*d + 2*b*c)*x + (-2*a*b + 2*c*d)*z;
		double zP = (a*a - b*b - c*c + d*d)*z + (-2*a*c + 2*b*d)*x + ( 2*a*b + 2*c*d)*y;
		
		return new Vector3(xP, yP, zP);
	}
	
	public static Vector3 rotateAroundAxis(Vector3 rotationAxis, double radians, Vector3 rotatee) {
		return buildQuaternion(rotationAxis, radians).rotate(rotatee);		
	}
	
	
	/**
	 * Creates a quaternion which is the Concatenation of a new orientation to the current one, such that the current one occurred first
	 * Both the original and argument quaternions are unchanged
	 * @param q The orientation to be applied second, which is multiplied from the left
	 * @return A new quaternion corresponding to q*this
	 */
	public Quaternion multiply(Quaternion q) {
		double aNew = q.a*a - q.b*b - q.c*c - q.d*d;
		double bNew = q.a*b + q.b*a + q.c*d - q.d*c;
		double cNew = q.a*c - q.b*d + q.c*a + q.d*b;
		double dNew = q.a*d + q.b*c - q.c*b + q.d*a;
		
		return new Quaternion(aNew, bNew, cNew, dNew);
	}
	
	@Override
	public String toString() {
		return "<" + a + ", " + b + ", " + c + ", " + d + ">";
	}
	
	public double length() {
		return Math.sqrt(a*a + b*b + c*c + d*d);
	}
	
	public Quaternion normalize() {
		double length = length();
		return new Quaternion(a/length, b/length, c/length, d/length);
	}
}



