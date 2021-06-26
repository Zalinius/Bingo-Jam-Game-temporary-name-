package com.zalinius.bingojam;

public class Quaternion {
	private final double a, b, c, d;
	
	private Quaternion() {
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
		System.out.println(rotationAxis);
		System.out.println(radians);
		System.out.println(rotatee);
		System.out.println();
		
		return buildQuaternion(rotationAxis, radians).rotate(rotatee);		
	}
}
