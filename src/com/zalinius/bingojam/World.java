package com.zalinius.bingojam;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.zalinius.bingojam.physics.Friction;
import com.zalinius.bingojam.plugins.FollowCam;
import com.zalinius.zje.architecture.GameObject;
import com.zalinius.zje.architecture.input.Inputtable;
import com.zalinius.zje.physics.Collisions;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.UnitVector;
import com.zalinius.zje.physics.Vector;

public class World implements GameObject {

	private Rocky rocky;
	private Wall wall;

	private Magnet m1 = new Magnet(new Point(100, -100), UnitVector.up(), 1000);
	private Magnet m2 = new Magnet(new Point(100, -150), UnitVector.down(), 1000);
	
	private LetterTile tile;

	private Pitfall pitfall;
	
	public World() {
		rocky = new Rocky();
		wall = new Wall(new Point(200, 200), new Point(300, 200));
		tile = new LetterTile('A', new Point(-200, 200));
		pitfall = new Pitfall(new Point(-250, -250), 200, 200);
	}
	
	@Override
	public void update(double delta) {
		List<Vector> forcesOnRocky = new ArrayList<>();

		double ScalarForce = Magnet.forceBetweenMagnets(m1, m2);
		Vector forceOnM1 = new Vector(m1.position(), m2.position()).scale(ScalarForce);
		forceOnM1 = forceOnM1.add(Friction.dynamicFriction(m1, 100));
		m1.update(forceOnM1, delta);

		Vector forceOnM2 = new Vector(m2.position(), m1.position()).scale(ScalarForce);
		forceOnM2 = forceOnM2.add(Friction.dynamicFriction(m2, 100));

		if(forceOnM2.length() > Friction.staticFrictionThreshold(m2, 150) || m2.velocity().length() > 1) {
			m2.update(forceOnM2, delta);
		}
		else {
			m2.update(m2.velocity().scale(-1 * m2.mass()), delta);
		}
		

		if(pitfall.shape().contains(rocky.position().point2D())) {
			Vector pitfallForce = new Vector(rocky.position(), pitfall.position());
			if(!pitfallForce.isZeroVector()) {
				if(pitfall.innerShape(rocky.radius()).contains(rocky.position().point2D())) {
					rocky.disable();
				}
				else {
					pitfallForce = pitfallForce.normalize().scale(500);
					forcesOnRocky.add(pitfallForce);
				}
				
			}
		}

		rocky.update(delta, forcesOnRocky);		
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
		
		if(tile.shape().contains(rocky.position().point2D())) {
			tile.press();
		}
	}

	@Override
	public void render(Graphics2D g) {
		m1.render(g);
		m2.render(g);
		wall.render(g);
		tile.render(g);
		pitfall.render(g);
		rocky.render(g);
	}
	


	public Collection<Inputtable> getKeyboardControls(BingoJamGame game) {
		List<Inputtable> inputs = new ArrayList<>();
		inputs.add(new Inputtable() {

			@Override
			public void released() {
				//Do nothing
			}

			@Override
			public void pressed() {
				game.exit();
			}

			@Override
			public int keyCode() {
				return KeyEvent.VK_ESCAPE;
			}
		});
		inputs.addAll(rocky.inputs());
		return inputs;
	}

	public FollowCam getFollowCamera() {
		return new FollowCam(rocky);
	}

}
