package com.zalinius.bingojam.plugins;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.plugins.RuntimePlugin;

public class Axes extends RuntimePlugin {
	
	private final Locatable visibleCenter;
	private final int gridWidth;
	
	public Axes(Locatable visibleCenter) {
		this.visibleCenter = visibleCenter;
		this.gridWidth = 100;
	}
	
	
	@Override
	public void renderBefore(Graphics2D g) {
		int xOffset = (int) visibleCenter.x();
		xOffset -= xOffset%100;
		int yOffset = (int) visibleCenter.y();
		yOffset -= yOffset%100;

		g.setStroke(new BasicStroke(2));
		g.setColor(Color.BLUE);
		g.drawLine(xOffset -700, 0, xOffset + 700, 0);
		g.setColor(Color.RED);
		g.drawLine(0, -5000, 0, 5000);
		g.setColor(Color.WHITE);
		g.drawLine(0, 0, 0, 0);

		g.setStroke(new BasicStroke(1));
		for (int i = -7*gridWidth; i <= 7*gridWidth; i+= gridWidth) {
			g.setColor(new Color(0,  0,  1f,  0.5f));
			Line2D.Double lineX = new Line2D.Double(xOffset - 700, yOffset + i, xOffset + 700, yOffset + i);
			g.draw(lineX);
			g.setColor(new Color(1f,  0,  0,  0.5f));
			Line2D.Double lineY = new Line2D.Double(xOffset  + i, yOffset - 700, xOffset + i, yOffset + 700);
			g.draw(lineY);
		}
	}

}
