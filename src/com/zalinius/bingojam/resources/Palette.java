package com.zalinius.bingojam.resources;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

public class Palette {
	
	public static final Color DEATH = Color.BLACK;
	public static final Color BRIGHT= Color.WHITE;
	public static final Color FACE  = Color.GREEN;
	public static final Color FACE_SHADED = FACE.darker().darker();
	public static final Color STONE = Color.DARK_GRAY;
	public static final Color GROUND = Color.DARK_GRAY.darker().darker();
	public static final Color BOUNCE = Color.ORANGE;
	public static final Color DOOR = new Color(.6f, .6f, .85f);

	public static final Color DEBUG = new Color(1f, 0f, 1f);
	
	
	public static final Stroke THIN = new BasicStroke(2.5f);
	public static final Stroke THICK = new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

	public static final Color RED = new Color(0.6f, 0.2f, 0.2f);
	public static final Color GREEN = new Color(0.2f, 0.6f, 0.2f);
	public static final Color BLUE = new Color(0.2f, 0.2f, 0.6f);
	public static final Color GRAY = Color.GRAY;
	

	public static final Color RED_BACKGROUND = RED.darker();
	public static final Color GREEN_BACKGROUND = GREEN.darker();
	public static final Color BLUE_BACKGROUND = BLUE.darker();
	public static final Color GRAY_BACKGROUND = GRAY.darker();
}
