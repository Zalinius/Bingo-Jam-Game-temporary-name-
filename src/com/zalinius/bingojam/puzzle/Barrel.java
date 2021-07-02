package com.zalinius.bingojam.puzzle;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zalinius.bingojam.physics.CollideableLine;
import com.zalinius.bingojam.physics.Kinetic;
import com.zalinius.bingojam.physics.Topographical;
import com.zalinius.bingojam.physics.Vector3;
import com.zalinius.bingojam.physics.forces.Force;
import com.zalinius.bingojam.physics.forces.Forceful;
import com.zalinius.bingojam.physics.forces.NetForce;
import com.zalinius.bingojam.physics.forces.NormalForce;
import com.zalinius.bingojam.physics.forces.in3d.Force3D;
import com.zalinius.bingojam.physics.forces.in3d.Forceful3D;
import com.zalinius.bingojam.physics.forces.in3d.GravityForce3D;
import com.zalinius.bingojam.physics.forces.in3d.KineticFriction3D;
import com.zalinius.bingojam.physics.forces.in3d.NetForce3D;
import com.zalinius.bingojam.physics.forces.in3d.NormalForce3D;
import com.zalinius.bingojam.physics.forces.in3d.StaticFriction3D;
import com.zalinius.bingojam.resources.Palette;
import com.zalinius.bingojam.utilities.Geometry;
import com.zalinius.zje.architecture.GameObject;
import com.zalinius.zje.physics.Collideable;
import com.zalinius.zje.physics.Collisions;
import com.zalinius.zje.physics.Physical;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.UnitVector;
import com.zalinius.zje.physics.Vector;
import com.zalinius.zje.physics.Vertex;

public class Barrel implements GameObject, Collideable, Kinetic{

	private Vertex center;
	private double radius;
	private double friction;
	private double bouncyness;

	private Topographical worldSurface;

	public Barrel(Point center, double radius, Topographical worldSurface) {
		this.center = new Vertex(center, 10);
		this.radius = radius;
		this.friction = .2;
		this.bouncyness = 0.5;

		this.worldSurface = worldSurface;
	}

	@Override
	public void update(double delta) {
		Vector wallbumpingImpulse = getImpulseFromHittingWalls();
		center.impulse(wallbumpingImpulse);

		//impulse from barrels and rocky
		Vector impulse = new Vector();
		for (Iterator<Kinetic> it = worldSurface.getKineticObjects().iterator(); it.hasNext();) {
			Kinetic object = it.next();
			Ellipse2D.Double objectShape = object.shape();
			Vector relativeVelocity = center.velocity().add(object.getPhysical().velocity());
			if(Geometry.twoCirclesColliding(shape(), objectShape) && relativeVelocity.length() != 0 && this != object) {
				UnitVector normal = new Vector(object.getPhysical().position(), center.position()).normalize();

				double dotProduct = Vector.dotProduct(relativeVelocity, normal);
				if(dotProduct > 0) {
					impulse = impulse.add(normal.scale(dotProduct * 0.5));
				}
			}
		}
		center.impulse(impulse);

		GravityForce3D gravityForce = new GravityForce3D(center, 500);

		Vector netPushForce = new Vector();
		for (Iterator<Kinetic> it = worldSurface.getKineticObjects().iterator(); it.hasNext();) {
			Kinetic object = it.next();
			if(Geometry.twoCirclesColliding(shape(), object.shape())) {
				Vector normal = new Vector(center.position(), object.getPhysical().position());
				netPushForce = netPushForce.add(normal.reflect().scale(10));
			}
		}
		NetForce3D directForces = new NetForce3D(new Force3D(netPushForce), gravityForce);

		NormalForce3D normalForce3D = new NormalForce3D(Vector3.OUT, directForces);
		Forceful wallNormalForces = getWallNormalForces(new Force(netPushForce));

		Forceful kineticFriction = new KineticFriction3D(center, normalForce3D, friction);
		Forceful3D staticFriction =  new StaticFriction3D(center, directForces, normalForce3D, friction);

		Forceful3D netForcesOnBarrel = new NetForce3D(directForces, normalForce3D, new Force3D(kineticFriction.getForce()), staticFriction, new Force3D(wallNormalForces.getForce()));

		Vector forceOnBarrel2D = netForcesOnBarrel.getForce().project();

		center.update(forceOnBarrel2D, delta);
		//center.update(delta);

	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(Palette.BARREL);
		g.draw(shape());
	}




	@Override
	public Ellipse2D.Double shape() {
		return Geometry.centeredCircle(center.position(), radius);
	}

	//@Override
	public Physical getPhysical() {
		return center;
	}



	private List<Vector> getLocalWallNormals() {
		List<Vector> normals = new ArrayList<>();
		Ellipse2D.Double circle = shape();
		for (Iterator<CollideableLine> it = worldSurface.getAdjacentWalls(circle).iterator(); it.hasNext();) {
			CollideableLine wall = it.next();
			Line2D.Double line = wall.line();

			if(Collisions.intersection(circle, line)) {
				Vector normal =new Vector(line).rejection(new Vector(center.position().subtract(line.x1, line.y1)));

				if(! (wall.oneWay() && Vector.dotProduct(normal, new Vector(line).perpendicularCCW()) < 0) ) {
					normals.add(normal.normalize());
				}
			}
		}

		return normals;
	}

	private Forceful getWallNormalForces(Forceful cause) {
		NetForce wallNormalForces = new NetForce();
		Iterator<Vector> it = getLocalWallNormals().iterator();
		while (it.hasNext()) {
			Vector vector = it.next();
			NormalForce normalForce = new NormalForce(vector.normalize(), cause);
			wallNormalForces.addForce(normalForce);
		}

		return wallNormalForces;
	}

	private Vector getImpulseFromHittingWalls() {
		Vector impulse = new Vector();
		Ellipse2D.Double circle = shape();
		Iterator<CollideableLine> it = worldSurface.getAdjacentWalls(circle).iterator();

		while(it.hasNext()) {
			CollideableLine wall = it.next();
			Line2D.Double line = wall.line();

			if(Collisions.intersection(circle, line)) {
				Vector velocity = center.velocity();
				Vector reflector = new Vector(line);
				Vector reflection = reflector.rejection(velocity).reflect().scale(center.mass()*(Geometry.combinedBouncyness(bouncyness, wall.bouncyness()) + 1d));

				Vector localRockyCenter = new Vector(new Point(line.getP1()), new Point(circle.getCenterX(), circle.getCenterY()));
				Vector direction = reflector.rejection(localRockyCenter);

				if(Vector.dotProduct(velocity, direction) < 0) {
					if(! (wall.oneWay() && Vector.dotProduct(direction, new Vector(line).perpendicularCCW()) < 0) ) {
						impulse = impulse.add(reflection);
					}

				}

			}
		}
		return impulse;
	}




}
