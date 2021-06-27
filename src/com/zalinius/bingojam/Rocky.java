package com.zalinius.bingojam;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.zalinius.zje.architecture.GameObject;
import com.zalinius.zje.architecture.input.Inputtable;
import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;
import com.zalinius.zje.physics.Vertex;

public class Rocky implements GameObject, Locatable{

	private Vertex center;
	private Quaternion orientation;
	private double radius;

	public Rocky() {
		this.center = new Vertex(new Point(), 5);
		this.radius = 50;
		this.orientation = new Quaternion();
		this.directionOfInput = new Vector();
	}

	@Override
	public void update(double delta) {
		Vector pushing = directionOfInput.scale(500);
		Vector friction = center.velocity().scale(-1);
		Vector sumOfForces = pushing.add(friction);

		center.update(sumOfForces, delta);

		//Update orientation
		if(center.velocity().length() != 0) {
			Vector3 rotationAxis = Quaternion.rotateAroundAxis(Vector3.OUT, Math.PI/2, new Vector3(center.velocity()) );
			double deltaRotation = (delta*center.velocity().length()) / radius;
			Quaternion deltaOrientation = Quaternion.buildQuaternion(rotationAxis, deltaRotation);
			orientation = orientation.multiply(deltaOrientation);
			orientation = orientation.normalize();
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (Iterator<Vector3> it = smileyFace().iterator(); it.hasNext();) {
			Vector3 point = it.next();
			point = orientation.rotate(point);
			if(point.z <= 0) {
				g.setColor(Color.GREEN.darker().darker());
			}else {
				g.setColor(Color.GREEN);
			}
			Vector projection = point.project();
			Point spot = center.position().add(projection);
			g.draw(new Line2D.Double(spot.point2D(), spot.point2D()));
		}

		g.setColor(Color.WHITE);
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


}
