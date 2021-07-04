package com.zalinius.bingojam;

import java.awt.Color;
import java.awt.Graphics2D;

import com.zalinius.zje.math.Interpolation;
import com.zalinius.zje.math.ZMath;
import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.plugins.RuntimePlugin;

public class ChangingBackgroundColor extends RuntimePlugin {
	private Color initialBackgroundColor;
	private Color finalBackgroundColor;
	private Locatable rockyPosition;
	private static final double yLimit = 2700;
	private static final double yExtreme = 10000;
	private Runnable exitAction;

	public ChangingBackgroundColor(Color initialBackgroundColor, Color finalBackgroundColor, Locatable rocky, Runnable exitAction) {
		this.initialBackgroundColor = initialBackgroundColor;
		this.finalBackgroundColor = finalBackgroundColor;
		this.rockyPosition = rocky;
		this.exitAction = exitAction;
	}
	
	@Override
	public void renderBefore(Graphics2D g) {
		g.setBackground(computeBackgroundColor());
		g.clearRect(0, 0, 1920, 1080); //TODO obtain height and width properly
	}

	private Color computeBackgroundColor() {
		double value = rockyPosition.y();
		if(value > yExtreme) {
			exitAction.run();
		}
		value = ZMath.clamp(value, yLimit, yExtreme);
		double interpolant = Interpolation.linearInterpolant(yLimit, yExtreme, value);
		int r = (int) Interpolation.linearInterpolation(initialBackgroundColor.getRed(), finalBackgroundColor.getRed(), interpolant);
		int g = (int) Interpolation.linearInterpolation(initialBackgroundColor.getGreen(), finalBackgroundColor.getGreen(), interpolant);
		int b = (int) Interpolation.linearInterpolation(initialBackgroundColor.getBlue(), finalBackgroundColor.getBlue(), interpolant);
		return new Color(r, g, b);

	}

}
