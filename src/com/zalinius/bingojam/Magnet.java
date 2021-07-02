package com.zalinius.bingojam;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import com.zalinius.zje.architecture.Graphical;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.UnitVector;
import com.zalinius.zje.physics.Vector;
import com.zalinius.zje.physics.Vertex;

public class Magnet extends Vertex implements Graphical{
	private UnitVector northPole;
	private double strength;
	
	public Magnet(Point position, UnitVector northPole, double strength) {
		super(position);
		this.northPole = northPole;
		this.strength = strength;
	}
		
	/**
	 * The scalar value of the interaction between magnets.
	 * Positive if attracting, negative if repulsing
	 * @param m1
	 * @param m2
	 * @return A scalar force
	 */
	public static double forceBetweenMagnets(Magnet m1, Magnet m2) {
		double scalarForce = m1.strength * m2.strength;
		scalarForce *= Vector.dotProduct(m1.northPole, m2.northPole);
		scalarForce /= Math.pow(Point.distance(m1.position(), m2.position()), 3);
		
		return scalarForce;
	}

	@Override
	public void render(Graphics2D g) {
		Point2D p1 = position().add(northPole.scale(-5)).point2D();
		Point2D p2 = position().add(northPole.scale( 5)).point2D();
		g.setPaint(new GradientPaint(p1, Color.BLUE, p2, Color.RED));
		g.setStroke(new BasicStroke(10, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
		g.draw(new Line2D.Double(p1, p2));
	}
	
	
	
}
