package com.zalinius.bingojam;

import java.awt.Color;
import java.awt.Graphics2D;
import java.time.Duration;

import com.zalinius.zje.architecture.input.RumbleListener;
import com.zalinius.zje.math.Interpolation;
import com.zalinius.zje.math.ZMath;
import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.plugins.RuntimePlugin;

public class ChangingBackgroundColor extends RuntimePlugin {
	private Color initialBackgroundColor;
	private Color finalBackgroundColor;
	private RumbleListener rumbleListener;
	private Locatable rockyPosition;
	private static final double yLimit = 2700;
	private static final double yExtreme = 10000;
	private Runnable exitAction;

	public ChangingBackgroundColor(Color initialBackgroundColor, Color finalBackgroundColor, Locatable rocky, Runnable exitAction, RumbleListener rumbleListener) {
		this.initialBackgroundColor = initialBackgroundColor;
		this.finalBackgroundColor = finalBackgroundColor;
		this.rockyPosition = rocky;
		this.exitAction = exitAction;
		this.rumbleListener = rumbleListener;
	}
	
	@Override
	public void renderBefore(Graphics2D g) {
		g.setBackground(computeBackgroundColor());
		g.clearRect(0, 0, 1920, 1080); //TODO obtain height and width properly
	}
	
	@Override
	public void updateBefore(double delta) {
		float rumbleStrength = (float) computeInterpolant();
		rumbleListener.requestRumble(Duration.ofSeconds(1), rumbleStrength);
	}

	private Color computeBackgroundColor() {
		double interpolant = computeInterpolant();
		int r = (int) Interpolation.linearInterpolation(initialBackgroundColor.getRed(), finalBackgroundColor.getRed(), interpolant);
		int g = (int) Interpolation.linearInterpolation(initialBackgroundColor.getGreen(), finalBackgroundColor.getGreen(), interpolant);
		int b = (int) Interpolation.linearInterpolation(initialBackgroundColor.getBlue(), finalBackgroundColor.getBlue(), interpolant);
		return new Color(r, g, b);

	}
	
	private double computeInterpolant() {
		double value = rockyPosition.y();
		if(value > yExtreme) {
			exitAction.run();
		}
		value = ZMath.clamp(value, yLimit, yExtreme);
		return Interpolation.linearInterpolant(yLimit, yExtreme, value);

	}

}
