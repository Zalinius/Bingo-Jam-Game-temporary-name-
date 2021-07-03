package com.zalinius.bingojam.puzzle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import com.zalinius.bingojam.resources.Palette;
import com.zalinius.bingojam.utilities.Geometry;
import com.zalinius.zje.architecture.GameObject;
import com.zalinius.zje.physics.Collideable;
import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.physics.Point;

public class Button implements GameObject, Collideable{
	
	private final Point center;
	private final Runnable action;
	private final Locatable player;
	private final boolean staysPressed;
	private final Color color;
	
	private boolean pressed;
	private final double width;

	public Button(Point center, Runnable action, Locatable player, Color color) {
		this(center, action, player, color, true);
	}

	public Button(Point center, Runnable action, Locatable player, Color color, boolean staysPressed) {
		this.center = center;
		this.width = 50;
		this.action = action;
		this.player = player;
		this.staysPressed = staysPressed;
		this.color = color;
		
		this.pressed = false;
	}
	
	@Override
	public void update(double delta) {
		if(shape().contains(player.position().point2D())) {
			if(!pressed) {
				pressed = true;
				action.run();
			}
		}
		else {
			if(pressed && !staysPressed) {
				pressed = false;
			}
		}

	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(color);
		g.fill(shape());
		g.setColor(Palette.GROUND);
		g.fill(Geometry.centeredCircle(center, width * .75));
		if(pressed) {
			g.setColor(color);
			g.fill(Geometry.centeredCircle(center, width * .5));
		}
	}

	@Override
	public Shape shape() {
		return Geometry.centeredCircle(center, width);
	}
	
}
