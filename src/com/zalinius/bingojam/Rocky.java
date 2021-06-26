package com.zalinius.bingojam;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import com.zalinius.zje.architecture.GameObject;
import com.zalinius.zje.architecture.input.Inputtable;
import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;
import com.zalinius.zje.physics.Vertex;

public class Rocky implements GameObject, Locatable{

	private Vertex center;
	private double radius;
	
	public Rocky() {
		this.center = new Vertex(new Point(), 5);
		this.radius = 50;
		this.directionOfInput = new Vector();
	}
	
	@Override
	public void update(double delta) {
		Vector pushing = directionOfInput.scale(500);
		Vector friction = center.velocity().scale(-1);
		
		Vector sumOfForces = pushing.add(friction);
		
		center.update(sumOfForces, delta);
	}

	@Override
	public void render(Graphics2D g) {
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
