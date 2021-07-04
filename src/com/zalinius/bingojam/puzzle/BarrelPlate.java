package com.zalinius.bingojam.puzzle;

import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import com.zalinius.bingojam.resources.Palette;
import com.zalinius.bingojam.utilities.Geometry;
import com.zalinius.zje.architecture.GameObject;
import com.zalinius.zje.physics.Collideable;
import com.zalinius.zje.physics.Point;

public class BarrelPlate implements Collideable, GameObject{

	private final Point center;
	private final double width;
	private final Runnable pressedAction;
	private final Runnable releasedAction;

	private boolean pressed;
	
	public BarrelPlate(Point center, Runnable pressedAction, Runnable releasedAction) {
		this.center = center;
		this.width = 75;
		this.pressedAction = pressedAction;
		this.releasedAction = releasedAction;
		
		this.pressed = false;

	}

	@Override
	public RoundRectangle2D.Double shape() {
		return Geometry.centeredRoundedSquare(center, width);
	}
	public RoundRectangle2D.Double smallerShape(double scale) {
		return Geometry.centeredRoundedSquare(center, scale*width);
	}

	@Override
	public void update(double delta) {
		if(pressed) {
			pressedAction.run();
		}
		else {
			releasedAction.run();
		}
	}	
	
	@Override
	public void render(Graphics2D g) {
		g.setColor(Palette.RED_BACKGROUND);
		g.fill(shape());
		if(pressed) {
			g.setColor(Palette.GROUND);
			g.fill(smallerShape(.75));
			g.setColor(Palette.RED_BACKGROUND);
			g.fill(smallerShape(.5));
		}
		else {
			g.setColor(Palette.GROUND);
			g.fill(smallerShape(.75));
		}
	}
	

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
	public boolean isPressed() {
		return pressed;
	}

}
