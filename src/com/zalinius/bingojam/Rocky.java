package com.zalinius.bingojam;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zalinius.bingojam.physics.Quaternion;
import com.zalinius.bingojam.physics.Vector3;
import com.zalinius.bingojam.resources.Palette;
import com.zalinius.zje.architecture.input.Inputtable;
import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;
import com.zalinius.zje.physics.Vertex;

public class Rocky implements Locatable{

	private Vertex center;
	private Quaternion orientation;
	
	private double radius;
	private double acceleration;
	private double friction;
	private Point respawnPoint;

	public Rocky() {
		this.center = new Vertex(new Point(), 5);
		this.respawnPoint = center.position();
		this.radius = 50;
		this.orientation = new Quaternion();
		this.directionOfInput = new Vector();
		this.acceleration = 500;
		this.friction = 1;
	}

	public void update(double delta, List<Vector> forcesOnRocky) {
		Vector pushing = directionOfInput;
		if(!pushing.isZeroVector()) {
			pushing = pushing.normalize().scale(acceleration);
		}
		Vector frictionForce = center.velocity().scale(-friction);
		Vector sumOfForces = pushing.add(frictionForce);

		forcesOnRocky.add(sumOfForces);
		center.update(forcesOnRocky, delta);

		//Update orientation
		if(center.velocity().length() != 0) {
			Vector3 rotationAxis = Quaternion.rotateAroundAxis(Vector3.OUT, Math.PI/2, new Vector3(center.velocity()) );
			double deltaRotation = (delta*center.velocity().length()) / radius;
			Quaternion deltaOrientation = Quaternion.buildQuaternion(rotationAxis, deltaRotation);
			orientation = orientation.multiply(deltaOrientation);
			orientation = orientation.normalize();
		}
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
		g.draw(rockyShape());
	}

	public Ellipse2D.Double rockyShape() {
		return new Ellipse2D.Double(center.x()-radius, center.y()-radius, 2*radius, 2*radius);
	}	

	//INPUT//
	public List<Inputtable> inputs(){
		List <Inputtable> inputs = new ArrayList<>();

		inputs.add(directionInput(KeyEvent.VK_W, new Vector( 0, -1)));
		inputs.add(directionInput(KeyEvent.VK_A, new Vector(-1,  0)));
		inputs.add(directionInput(KeyEvent.VK_S, new Vector( 0,  1)));
		inputs.add(directionInput(KeyEvent.VK_D, new Vector( 1,  0)));

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

	private Vector directionOfInput;
	public Inputtable directionInput(int key, Vector direction) {
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

	public Vertex physicality() {
		return center;
	}
	
	public double radius() {
		return radius;
	}

	public void disable() {
		acceleration = 0;
		friction = 10;
	}
	public void enable() {
		acceleration = 500;
		friction = 1;
	}
	
	public void respawn() {
		center = new Vertex(respawnPoint, 5);
		orientation = new Quaternion();
		enable();
	}

}
