package com.zalinius.bingojam;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.zalinius.zje.architecture.GameContainer;
import com.zalinius.zje.architecture.input.Inputtable;
import com.zalinius.zje.physics.Collisions;
import com.zalinius.zje.physics.Locatable;
import com.zalinius.zje.physics.Physical;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.UnitVector;
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

	private Magnet m1 = new Magnet(new Point(100, -100), UnitVector.up(), 1000);
	private Magnet m2 = new Magnet(new Point(100, -150), UnitVector.down(), 1000);

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
		FollowCam cam = new FollowCam(rocky);
		plugins.add(cam);
		plugins.add(axes(cam));
		return plugins;
	}


	@Override
	public void render(Graphics2D g) {
		m1.render(g);
		m2.render(g);
		rocky.render(g);
		wall.render(g);
	}

	public Vector dynamicFriction(Physical object, double coefficient) {
		if(object.velocity().isZeroVector()) {
			return new Vector();
		}
		return object.velocity().normalize().reflect().scale(coefficient).scale(object.mass());
	}

	public double staticFrictionThreshold(Physical object, double staticCoefficient) {
		return object.mass() * staticCoefficient;
	}

	@Override
	public void update(double delta) {

		double ScalarForce = Magnet.forceBetweenMagnets(m1, m2);
		Vector forceOnM1 = new Vector(m1.position(), m2.position()).scale(ScalarForce);
		forceOnM1 = forceOnM1.add(dynamicFriction(m1, 100));
		m1.update(forceOnM1, delta);

		Vector forceOnM2 = new Vector(m2.position(), m1.position()).scale(ScalarForce);
		forceOnM2 = forceOnM2.add(dynamicFriction(m2, 100));

		if(forceOnM2.length() > staticFrictionThreshold(m2, 150) || m2.velocity().length() > 1) {
			m2.update(forceOnM2, delta);
		}
		else {
			m2.update(m2.velocity().scale(-1 * m2.mass()), delta);
		}
		



		rocky.update(delta);		
		if(Collisions.intersection(rocky.rockyShape(), wall.line())) {
			Vector velocity = rocky.physicality().velocity();
			Vector reflector = new Vector(wall.line());
			double bouncyness = 1.0;
			Vector reflection = reflector.rejection(velocity).reflect().scale(rocky.physicality().mass()*(bouncyness + 1));

			Vector localRockyCenter = new Vector(new Point(wall.line().getP1()), rocky.position());
			Vector direction = reflector.rejection(localRockyCenter);

			if(Vector.dotProduct(velocity, direction) < 0) {
				rocky.physicality().impulse(reflection);
			}
		}
	}

	private AbstractPlugin axes(final Locatable cameraCenter) {
		return new RuntimePlugin() {
			@Override
			public void renderBefore(Graphics2D g) {
				int xOffset = (int) cameraCenter.x();
				xOffset -= xOffset%100;
				int yOffset = (int) cameraCenter.y();
				yOffset -= yOffset%100;

				g.setStroke(new BasicStroke(2));
				g.setColor(Color.BLUE);
				g.drawLine(xOffset -700, 0, xOffset + 700, 0);
				g.setColor(Color.RED);
				g.drawLine(0, -5000, 0, 5000);
				g.setColor(Color.WHITE);
				g.drawLine(0, 0, 0, 0);

				g.setStroke(new BasicStroke(1));
				for (int i = -700; i <= 700; i+= 100) {
					g.setColor(new Color(0,  0,  1f,  0.5f));
					Line2D.Double lineX = new Line2D.Double(xOffset - 700, yOffset + i, xOffset + 700, yOffset + i);
					g.draw(lineX);
					g.setColor(new Color(1f,  0,  0,  0.5f));
					Line2D.Double lineY = new Line2D.Double(xOffset  + i, yOffset - 700, xOffset + i, yOffset + 700);
					g.draw(lineY);
				}

			}
		};
	}


	@Override
	public void shutdownActions() {
		System.out.println("Goodbye Bingo");		
	}

}
