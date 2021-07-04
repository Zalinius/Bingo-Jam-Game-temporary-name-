package com.zalinius.bingojam;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;

import com.zalinius.zje.architecture.Graphical;

public class RunicLine implements Comparable<RunicLine>, Graphical{
	
	private Path2D.Double path;
	private double thickness;
	private Color color;
	
	public RunicLine(Double path, double thickness, Color color) {
		this.path = path;
		this.thickness = thickness;
		this.color = color;
	}
	
	@Override
	public void render(Graphics2D g) {
		g.setStroke(new BasicStroke((float)thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(color);
		g.draw(path);		
	}
	 
	@Override
	public int compareTo(RunicLine other) {
		return (int) (other.thickness - this.thickness);
	}
	
	public void changeColor(Color newColor) {
		this.color = newColor;
	}

}
