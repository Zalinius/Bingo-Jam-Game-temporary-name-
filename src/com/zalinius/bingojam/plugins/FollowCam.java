package com.zalinius.bingojam.plugins;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.zalinius.zje.math.Interpolation;
import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;
import com.zalinius.zje.plugins.RuntimePlugin;

public class FollowCam extends RuntimePlugin implements Locatable{
	
	private Point cameraInterpolation;
	
	private Locatable target;

	public FollowCam(Locatable target) {
		cameraInterpolation = new Point();
		this.target = target;
	}


	@Override
	public void updateBefore(double delta) {
		double cameraSpeed = 2;
		cameraInterpolation = Interpolation.linearInterpolation(cameraInterpolation, target.position(), delta * cameraSpeed);
	}


	@Override
	public void renderBefore(Graphics2D g) {
		Point cameraPosition = reflectCameraOffTarget().subtract(500, 500);
		AffineTransform trans = new AffineTransform(1, 0, 0, 1, -cameraPosition.x, - cameraPosition.y);
		g.setTransform(trans);

	}

	private Point reflectCameraOffTarget() {
		Vector cameraToTarget = new Vector(cameraInterpolation, target.position());
		return target.position().add(cameraToTarget);
	}

	@Override
	public Point position() {
		return reflectCameraOffTarget();
	}
}