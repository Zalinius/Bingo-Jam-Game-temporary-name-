package com.zalinius.bingojam;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.zalinius.zje.architecture.GameContainer;
import com.zalinius.zje.architecture.input.Inputtable;
import com.zalinius.zje.physics.Collisions;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.Vector;
import com.zalinius.zje.plugins.AbstractPlugin;
import com.zalinius.zje.plugins.BackgroundColor;
import com.zalinius.zje.plugins.RuntimePlugin;

public class BingoJamGame extends GameContainer{
	public static void main(String[] args) {
		BingoJamGame game = new BingoJamGame();
		game.startGame();
	}

	private Rocky rocky;
	private Wall wall;
	
	public BingoJamGame() {
		super("Bingo jam game (temp name)", 1000, 1000);
		
		
		rocky = new Rocky();
		wall = new Wall(new Point(200, 200), new Point(300, 200));
		
		addControls(getKeyboardControls(), null);
		
		
	}


	private Collection<Inputtable> getKeyboardControls() {
		List<Inputtable> inputs = new ArrayList<>();
		inputs.add(new Inputtable() {
			
			@Override
			public void released() {
				//Do nothing
			}
			
			@Override
			public void pressed() {
				exit();
			}
			
			@Override
			public int keyCode() {
				// TODO Auto-generated method stub
				return KeyEvent.VK_ESCAPE;
			}
		});
		inputs.addAll(rocky.inputs());
		return inputs;
	}
	
	@Override
	public List<AbstractPlugin> getPlugins() {
		List<AbstractPlugin> plugins = new ArrayList<>();
		plugins.add(new BackgroundColor(Color.DARK_GRAY.darker().darker()));
		plugins.add(axes());
		plugins.add(new FollowCam(rocky));
		return plugins;
	}


	@Override
	public void render(Graphics2D g) {
		rocky.render(g);
		wall.render(g);
	}

	@Override
	public void update(double delta) {
		rocky.update(delta);		
		if(Collisions.intersection(rocky.rockyShape(), wall.line())) {
			System.out.println("Wapow");
			Vector velocity = rocky.physicality().velocity();
			Vector reflector = new Vector(wall.line());
			Vector reflection = reflector.rejection(velocity).reflect().scale(rocky.physicality().mass()*2);
			rocky.physicality().impulse(reflection);
		}
	}
	
	private AbstractPlugin axes() {
		return new RuntimePlugin() {
			@Override
			public void renderBefore(Graphics2D g) {
				g.setStroke(new BasicStroke(2));
				g.setColor(Color.BLUE);
				g.drawLine(-5000, 0, 5000, 0);
				g.setColor(Color.RED);
				g.drawLine(0, -5000, 0, 5000);
				g.setColor(Color.WHITE);
				g.drawLine(0, 0, 0, 0);
			}
		};
	}


	@Override
	public void shutdownActions() {
		System.out.println("Goodbye Bingo");		
	}

}
