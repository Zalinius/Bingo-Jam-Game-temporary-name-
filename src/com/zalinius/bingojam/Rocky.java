package com.zalinius.bingojam;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
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
import com.zalinius.zje.architecture.input.Inputtable;
import com.zalinius.zje.physics.Collisions;
import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;
import com.zalinius.zje.physics.Vertex;

public class Rocky implements GameObject, Locatable, Kinetic{

	private Vertex center;
	private Quaternion orientation;

	private double radius;
	private double acceleration;
	private double friction;
	private double bouncyness;
	private Point respawnPoint;

	private Vector directionOfInput;

	private Topographical worldSurface;

	public Rocky(Topographical worldSurface) {
		//TODO cleanup
		//this.center = new Vertex(new Point(0, 0), 5); //Start
		//this.center = new Vertex(new Point(-3500, -2100), 5);// main room
		//this.center = new Vertex(new Point(-1500, -1100), 5);// blue wing start
		//this.center = new Vertex(new Point(-1500, 1600), 5);// blue wing end
		this.center = new Vertex(new Point(-3500, -3500), 5);// green wing start
		//this.center = new Vertex(new Point(-3500, -4200), 5);// green wing middle
		//this.center = new Vertex(new Point(-3500, -5800), 5);// green wing end
		
		this.respawnPoint = center.position();
		this.radius = 45;
		this.orientation = new Quaternion();
		this.directionOfInput = new Vector();
		this.acceleration = 500;
		this.friction = .01;
		this.bouncyness = 0.5;

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
		Vector pushing = directionOfInput;
		if(!pushing.isZeroVector()) {
			pushing = pushing.normalize().scale(acceleration);
		}
		return new Force(pushing);
	}

	public void render(Graphics2D g) {
		g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (Iterator<Vector3> it = smileyFace().iterator(); it.hasNext();) {
			Vector3 point = it.next();
			point = orientation.rotate(point);
			if(point.z <= 0) {
				g.setColor(Palette.FACE_SHADED);
			}else {
				g.setColor(Palette.FACE);
			}
			Vector projection = point.project();
			Point spot = center.position().add(projection);
			g.draw(new Line2D.Double(spot.point2D(), spot.point2D()));
		}

		g.setColor(Palette.BRIGHT);
		g.setStroke(new BasicStroke(5));
		g.draw(shape());
	}

	public Ellipse2D.Double shape() {
		return new Ellipse2D.Double(center.x()-radius, center.y()-radius, 2*radius, 2*radius);
	}	

	//INPUT//
	public List<Inputtable> inputs(){
		List <Inputtable> inputs = new ArrayList<>();

		inputs.add(directionInput(KeyEvent.VK_W, new Vector( 0, -1)));
		inputs.add(directionInput(KeyEvent.VK_A, new Vector(-1,  0)));
		inputs.add(directionInput(KeyEvent.VK_S, new Vector( 0,  1)));
		inputs.add(directionInput(KeyEvent.VK_D, new Vector( 1,  0)));

		inputs.add(new Inputtable() {
			
			@Override
			public void released() {/*Do nothing*/}
			
			@Override
			public void pressed() {
				System.out.println("(" + (int)position().x + ", " + (int)position().y + ")");				
			}
			
			@Override
			public int keyCode() {
				return KeyEvent.VK_SPACE;
			}
		});

		inputs.add(new Inputtable() {
			
			@Override
			public void released() {/*Do nothing*/}
			
			@Override
			public void pressed() {
				respawn();
			}
			
			@Override
			public int keyCode() {
				return KeyEvent.VK_K;
			}
		});

		return inputs;
	}

	private List<Vector3> smileyFace(){
		List<Vector3> points = new ArrayList<>();

		points.add(new Vector3(1, -1, 2).normalize().scale(radius));
		points.add(new Vector3(-1, -1, 2).normalize().scale(radius));

		points.add(new Vector3(-1, 1, 2).normalize().scale(radius));
		points.add(new Vector3(-0.5, 1.25, 2).normalize().scale(radius));
		points.add(new Vector3( 0, 1.35, 2).normalize().scale(radius));
		points.add(new Vector3(0.5, 1.25, 2).normalize().scale(radius));
		points.add(new Vector3( 1, 1, 2).normalize().scale(radius));

		return points;
	}

	private Inputtable directionInput(int key, Vector direction) {
		return new Inputtable() {

			@Override
			public void released() {
				directionOfInput = directionOfInput.subtract(direction);
			}

			@Override
			public void pressed() {
				directionOfInput = directionOfInput.add(direction);		
			}

			@Override
			public int keyCode() {
				return key;
			}
		};
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
		acceleration = 500;
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


}
