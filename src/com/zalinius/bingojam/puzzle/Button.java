package com.zalinius.bingojam.puzzle;

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
	private final double width;
	private final Runnable action;
	private final Locatable player;
	
	private boolean pressed;
	
	public Button(Point center, double width, Runnable action, Locatable player) {
		this.center = center;
		this.width = width;
		this.action = action;
		this.player = player;
		
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

	}
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(Palette.DOOR);
		g.fill(shape());
		g.setColor(Palette.GROUND);
		g.fill(Geometry.centeredCircle(center, width * .75));
		if(pressed) {
			g.setColor(Palette.DOOR);
			g.fill(Geometry.centeredCircle(center, width * .5));
		}
	}

	@Override
	public Shape shape() {
		return Geometry.centeredCircle(center, width);
	}
	
}
