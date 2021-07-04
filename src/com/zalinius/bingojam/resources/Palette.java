package com.zalinius.bingojam.resources;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;

public class Palette {
	public static final Color DEBUG = new Color(1f, 0f, 1f, 1);
	
		
	public static final Color DEATH = Color.BLACK;
	public static final Color BRIGHT= Color.WHITE;
	public static final Color FACE  = BRIGHT;
	public static final Color FACE_SHADED = BRIGHT.darker();
	public static final Color CAT   = Color.ORANGE;
	public static final Color CAT_SHADED = Color.ORANGE.darker();
	public static final Color GROUND = Color.DARK_GRAY.darker().darker();


	public static final Color RED_LIFE = new Color(1, 0,0);
	public static final Color GREEN_LIFE = Color.GREEN;
	public static final Color BLUE_LIFE = Color.BLUE;

	public static final Color RED = new Color(0.8f, 0, 0f);
	public static final Color GREEN = new Color(0.2f, 0.6f, 0.2f);
	public static final Color BLUE = new Color(0.2f, 0.2f, 0.6f);
	public static final Color GRAY = Color.GRAY;
	
	public static final Color RED_BACKGROUND = new Color(0.6f, 0, 0f);
	public static final Color GREEN_BACKGROUND = GREEN.darker();
	public static final Color BLUE_BACKGROUND = BLUE.darker();
	public static final Color GRAY_BACKGROUND = GRAY.darker();

	public static final Color RED_DEATH = new Color(0.4f, 0, 0f);
	public static final Color GREEN_DEATH = GREEN_BACKGROUND.darker();
	public static final Color BLUE_DEATH = BLUE_BACKGROUND.darker();
	public static final Color GRAY_DEATH = GRAY_BACKGROUND.darker();
	

	
	public static final Stroke THIN = new BasicStroke(2.5f);
	public static final Stroke THICK = new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

}
