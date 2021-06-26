package com.zalinius.bingojam;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.zalinius.zje.math.Interpolation;
import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.plugins.RuntimePlugin;

public class FollowCam extends RuntimePlugin implements Locatable{
	
	private Point center;
	
	private Locatable target;

	public FollowCam(Locatable target) {
		center = new Point();
		this.target = target;
	}


	@Override
	public void updateBefore(double delta) {
		double cameraSpeed = 1;
		center = Interpolation.linearInterpolation(center, target.position(), delta * cameraSpeed);
	}


	@Override
	public void renderBefore(Graphics2D g) {
		Point cameraPosition = center.subtract(500, 500);
		AffineTransform trans = new AffineTransform(1, 0, 0, 1, -cameraPosition.x, - cameraPosition.y);
		g.setTransform(trans);

	}


	@Override
	public Point position() {
		return center;
	}
}
