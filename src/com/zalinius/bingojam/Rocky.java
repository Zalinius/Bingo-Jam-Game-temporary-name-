package com.zalinius.bingojam;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.zalinius.bingojam.physics.CollideableLine;
import com.zalinius.bingojam.physics.Kinetic;
import com.zalinius.bingojam.physics.Quaternion;
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
import com.zalinius.zje.architecture.input.actions.Axisable;
import com.zalinius.zje.architecture.input.actions.Inputtable;
import com.zalinius.zje.physics.Collisions;
import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;
import com.zalinius.zje.physics.Vertex;
import com.zalinius.zje.utilities.input.GamepadKeyboardDirection;

public class Rocky implements GameObject, Locatable, Kinetic{

	private Vertex center;
	private Quaternion orientation;

	private double radius;
	private double acceleration;
	private double friction;
	private double bouncyness;
	private Point respawnPoint;

	private boolean youreacat;

	private GamepadKeyboardDirection input;

	private Topographical worldSurface;

	public Rocky(Topographical worldSurface) {
		//TODO cleanup
		this.center = new Vertex(new Point(0, 0), 5); //Start
		//this.center = new Vertex(new Point(-3500, -2100), 5);// main room
		//this.center = new Vertex(new Point(-4900, -2100), 5);// red wing start
		//this.center = new Vertex(new Point(-7000, -2100), 5);// red wing room 3
		//this.center = new Vertex(new Point(-8500, -2100), 5);// red wing end
		//this.center = new Vertex(new Point(-1500, -1100), 5);// blue wing start
		//this.center = new Vertex(new Point(-1500,  1600), 5);// blue wing end
		//this.center = new Vertex(new Point(-3500, -3500), 5);// green wing start
		//this.center = new Vertex(new Point(-3500, -4200), 5);// green wing middle
		//this.center = new Vertex(new Point(-3500, -5800), 5);// green wing end
		//this.center = new Vertex(new Point(-3500, -1000), 5);// final corridor

		this.respawnPoint = center.position();
		this.radius = 45;
		this.orientation = new Quaternion();
		this.input = new GamepadKeyboardDirection();
		this.acceleration = 1000;
		this.friction = .2;
		this.bouncyness = 0.5;

		this.youreacat = false;

		this.worldSurface = worldSurface;
	}

	@Override
	public void update(double delta) {
		Vector wallbumpingImpulse = getImpulseFromHittingWalls();
		center.impulse(wallbumpingImpulse);

		center.update(netForces(), delta);

		//Update orientation
		if(center.velocity().length() != 0) {
			Vector3 rotationAxis = Quaternion.rotateAroundAxis(Vector3.IN, Math.PI/2, new Vector3(center.velocity()) );
			double deltaRotation = (delta*center.velocity().length()) / radius;
			Quaternion deltaOrientation = Quaternion.buildQuaternion(rotationAxis, deltaRotation);
			orientation = orientation.multiply(deltaOrientation);
			orientation = orientation.normalize();
		}

	}

	public Vector netForces() {
		GravityForce3D gravityForce = new GravityForce3D(center, 500);
		Forceful pushingForce = getPushingForce();

		NetForce3D directForces = new NetForce3D(new Force3D(pushingForce.getForce()), gravityForce);

		NormalForce3D normalForce3D = new NormalForce3D(getLocalSurfaceNormal(), directForces);
		Forceful wallNormalForces = getWallNormalForces(pushingForce);

		Forceful kineticFriction = new KineticFriction3D(center, normalForce3D, friction);
		Forceful3D staticFriction =  new StaticFriction3D(center, directForces, normalForce3D, friction);

		Forceful3D netForcesOnRocky = new NetForce3D(directForces, normalForce3D, new Force3D(kineticFriction.getForce()), staticFriction, new Force3D(wallNormalForces.getForce()));

		Vector forceOnRocky2D = netForcesOnRocky.getForce().project();

		return forceOnRocky2D;
	}

	private Forceful getPushingForce() {
		Vector pushing = input.direction();
		
		pushing = pushing.scale(acceleration);

		return new Force(pushing);
	}

	public void render(Graphics2D g) {
		g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (Iterator<Vector3> it = smileyFace().iterator(); it.hasNext();) {
			Vector3 point = it.next();
			point = orientation.rotate(point);
			if(point.z <= 0) {
				if(youreacat) {
					g.setColor(Palette.CAT_SHADED);
					g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

				}
				else {
					g.setColor(Palette.FACE_SHADED);
				}
			}else {
				if(youreacat) {
					g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
					g.setColor(Palette.CAT);
				}
				else {
					g.setColor(Palette.FACE);
				}
			}
			Vector projection = point.project();
			Point spot = center.position().add(projection);
			g.draw(new Line2D.Double(spot.point2D(), spot.point2D()));
		}
		{	
			List<Vector3> smilePoints;
			if(youreacat) {
				smilePoints = catPointsAsQuadPath();
			}
			else {
				smilePoints = smilePointsAsQuadPath();
			}

			for (int i = 0; i != smilePoints.size(); i+=3) {
				Color smileColor = null;
				Color smileShadedColor = null;
				if(youreacat) {
					smileColor = Palette.CAT;
					smileShadedColor = Palette.CAT_SHADED;
				}
				else {
					smileColor = Palette.FACE;
					smileShadedColor = Palette.FACE_SHADED;
				}
				g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				projectAndDrawQuadCurve(g, smilePoints.get(i+0), smilePoints.get(i+1), smilePoints.get(i+2), smileColor, smileShadedColor);				
			}


			if(youreacat) {
				drawCatNose(g);
			}
		}

		if(youreacat) {
			g.setColor(Palette.CAT);
		}
		else {
			g.setColor(Palette.BRIGHT);
		}
		g.setStroke(new BasicStroke(5));
		g.draw(shape());
	}

	public Ellipse2D.Double shape() {
		return new Ellipse2D.Double(center.x()-radius, center.y()-radius, 2*radius, 2*radius);
	}	


	public void makeCat() {
		this.youreacat = true;
	}

	private void projectAndDrawQuadCurve(Graphics2D g, Vector3 p0, Vector3 p1, Vector3 p2, Color lightColor, Color darkColor){
		Vector3 pc = p1.scale(2).add(p0.scale(-0.5)).add(p2).scale(-0.5);

		p0 = orientation.rotate(p0);
		pc = orientation.rotate(p1); //TODO woops?
		p2 = orientation.rotate(p2);

		double z = (p0.z + p2.z) / 2;

		Vector p0XY = p0.project();
		Vector pCXY = pc.project();
		Vector p2XY = p2.project();

		Point p0Final = center.position().add(p0XY);
		Point pCFinal = center.position().add(pCXY);
		Point p2Final = center.position().add(p2XY);

		QuadCurve2D.Double curve = new QuadCurve2D.Double(p0Final.x, p0Final.y, pCFinal.x, pCFinal.y, p2Final.x, p2Final.y);

		if(z < 0) {
			g.setColor(darkColor);
		}
		else {
			g.setColor(lightColor);
		}

		g.draw(curve);

	}

	private List<Vector3> smileyFace(){
		List<Vector3> points = new ArrayList<>();

		points.add(new Vector3(1, -1, 2).normalize().scale(radius));
		points.add(new Vector3(-1, -1, 2).normalize().scale(radius));

		return points;
	}

	//
	private List<Vector3> smilePointsAsQuadPath(){
		List<Vector3> points = new ArrayList<>();
		points.add(new Vector3(-1, 1, 2).normalize().scale(radius));
		points.add(new Vector3(-0.5, 2, 2).normalize().scale(radius));
		points.add(new Vector3( 0, 0.9, 1).normalize().scale(radius));
		points.add(new Vector3( 0, 0.9, 1).normalize().scale(radius));
		points.add(new Vector3( 0.5, 2, 2).normalize().scale(radius));
		points.add(new Vector3( 1, 1, 2).normalize().scale(radius));

		return points;
	}
	private void drawCatNose(Graphics2D g){
		Vector3 catNose3D = new Vector3( 0, 0, 1).normalize().scale(radius);
		catNose3D = orientation.rotate(catNose3D);

		Vector projection = catNose3D.project();
		Point nose = center.position().add(projection);
		g.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		if(catNose3D.z < 0) {
			g.setColor(Color.PINK.darker());
		}
		else {
			g.setColor(Color.PINK);
		}
		g.draw(new Line2D.Double(nose.x, nose.y, nose.x, nose.y));



	}

	private List<Vector3> catPointsAsQuadPath(){
		List<Vector3> points = new ArrayList<>();
		points.add(new Vector3(-1.2, 0.5, 2).normalize().scale(radius));
		points.add(new Vector3(-.5, 2, 2).normalize().scale(radius));
		points.add(new Vector3( 0, 0, 1).normalize().scale(radius));
		points.add(new Vector3( 0, 0, 1).normalize().scale(radius));
		points.add(new Vector3( .5, 2, 2).normalize().scale(radius));
		points.add(new Vector3( 1.2, 0.5, 2).normalize().scale(radius));

		return points;
	}

	@Override
	public Point position() {
		return center.position();
	}

	public Vertex getPhysical() {
		return center;
	}

	public double radius() {
		return radius;
	}

	public void disable() {
		acceleration = 0;
	}
	public void enable() {
		acceleration = 1000;
	}

	public void setRespawn(Point respawnPoint) {
		this.respawnPoint = respawnPoint;
	}

	public void respawn() {
		center = new Vertex(respawnPoint, 5);
		orientation = new Quaternion();
		enable();
	}


	private Vector3 getLocalSurfaceNormal() {
		return worldSurface.getSurfaceNormal(center.position());
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
				Vector reflection = reflector.rejection(velocity).reflect().scale(center.mass()*(Geometry.combinedBouncyness(bouncyness, wall.bouncyness()) + 1));

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

	public Collection<Inputtable> inputs() {
		return input.inputs();
	}

	public Collection<Axisable> axisInputs() {
		return input.axisInput();
	}


}
