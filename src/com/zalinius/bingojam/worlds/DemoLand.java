package com.zalinius.bingojam.worlds;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.zalinius.bingojam.Magnet;
import com.zalinius.bingojam.Rocky;
import com.zalinius.bingojam.physics.CollideableLine;
import com.zalinius.bingojam.physics.Topographical;
import com.zalinius.bingojam.physics.Vector3;
import com.zalinius.bingojam.pieces.Door;
import com.zalinius.bingojam.pieces.Pitfall;
import com.zalinius.bingojam.pieces.Ramp;
import com.zalinius.bingojam.pieces.RespawnPoint;
import com.zalinius.bingojam.pieces.Slopable;
import com.zalinius.bingojam.pieces.Wall;
import com.zalinius.bingojam.plugins.FollowCam;
import com.zalinius.bingojam.puzzle.LetterPuzzle;
import com.zalinius.bingojam.puzzle.LetterTile;
import com.zalinius.bingojam.utilities.Geometry;
import com.zalinius.zje.architecture.input.Inputtable;
import com.zalinius.zje.physics.Collisions;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.physics.UnitVector;

public class DemoLand extends AbstractWorld implements Topographical{

	private Rocky rocky;
	private Collection<Wall> walls;
	private Collection<RespawnPoint> respawnPoints;
	private Collection<Door> doors;
	
	private Magnet m1 = new Magnet(new Point(100, -100), UnitVector.up(), 1000);
	private Magnet m2 = new Magnet(new Point(100, -150), UnitVector.down(), 1000);

	private LetterTile tile;
	private LetterPuzzle puzzle;

	private Pitfall pitfall;

	private Ramp ramp;

	public DemoLand() {
		rocky = new Rocky(this);
		
		walls = Arrays.asList(new Wall(new Point(200, 200), new Point(300, 200), 1), new Wall(new Point(350, 200), new Point(450, 200), true));
		tile = new LetterTile('A', new Point(-200, 200));
		pitfall = new Pitfall(new Point(-250, -250), 200, 200);
		ramp = new Ramp(new Point(-500, 300), 400, 200);
		respawnPoints = Arrays.asList(buildRespawnPoint(new Point()), buildRespawnPoint(new Point(300, 300)));
		Door codeDoorOpen = new Door(new Point(-400, -100), new Point(-400, 100));
		doors = Arrays.asList(codeDoorOpen);
		puzzle = makeLetterPuzzle(codeDoorOpen);

	}
	
	public RespawnPoint buildRespawnPoint(Point respawnPoint) {
		Runnable setRespawn = () -> rocky.setRespawn(respawnPoint);
		Shape respawnTrigger = Geometry.centeredSquare(respawnPoint, 100);
		return new RespawnPoint(respawnTrigger, rocky, setRespawn);
	}


	@Override
	public void update(double delta) {
		rocky.update(delta);		

		//TODO fix for new forces
		//		double ScalarForce = Magnet.forceBetweenMagnets(m1, m2);
		//		Vector forceOnM1 = new Vector(m1.position(), m2.position()).scale(ScalarForce);
		//		forceOnM1 = forceOnM1.add(Friction.dynamicFriction(m1, 100));
		//		m1.update(forceOnM1, delta);
		//
		//		Vector forceOnM2 = new Vector(m2.position(), m1.position()).scale(ScalarForce);
		//		forceOnM2 = forceOnM2.add(Friction.dynamicFriction(m2, 100));
		//
		//		if(forceOnM2.length() > Friction.staticFrictionThreshold(m2, 150) || m2.velocity().length() > 1) {
		//			m2.update(forceOnM2, delta);
		//		}
		//		else {
		//			m2.update(m2.velocity().scale(-1 * m2.mass()), delta);
		//		}
		//		

		if(pitfall.innerShape(rocky.radius()).contains(rocky.position().point2D())) {
			rocky.disable();
			rocky.respawn();
		}

		if(tile.shape().contains(rocky.position().point2D())) {
			tile.press();
		}
		puzzle.update(delta);
		respawnPoints.forEach(res -> res.update(delta));

	}

	@Override
	public void render(Graphics2D g) {
		m1.render(g);
		m2.render(g);
		walls.forEach(wall -> wall.render(g));
		tile.render(g);
		pitfall.render(g);
		ramp.render(g);
		puzzle.render(g);
		respawnPoints.forEach(res -> res.render(g));
		doors.forEach(door -> door.render(g));
		

		rocky.render(g);
	}

	@Override
	public Collection<Inputtable> getKeyboardControls() {
		List<Inputtable> inputs = new ArrayList<>();
		inputs.addAll(rocky.inputs());
		return inputs;
	}

	@Override
	public FollowCam getFollowCamera() {
		return new FollowCam(rocky);
	}

	@Override	
	public Vector3 getSurfaceNormal(Point position) {
		List<Slopable> slopes = Arrays.asList(pitfall, ramp);
		Vector3 mostExtremeNormal = Vector3.OUT;

		for (Iterator<Slopable> it = slopes.iterator(); it.hasNext();) {
			Slopable slopable = it.next();
			Vector3 newNormal = slopable.getNormalForSphere(position, rocky.radius());
			if(Vector3.angleBetweenVectors(newNormal, Vector3.OUT) > Vector3.angleBetweenVectors(mostExtremeNormal, Vector3.OUT)) {
				mostExtremeNormal = newNormal;
			}
		}

		return mostExtremeNormal;
	}

	@Override
	public List<CollideableLine> getAdjacentWalls(Ellipse2D.Double circle){
		List<CollideableLine> adjacentWalls = new ArrayList<>();

		for (Iterator<Wall> it = walls.iterator(); it.hasNext();) {
			Wall wall = it.next();
			if(Collisions.intersection(circle, wall.line())) {
				adjacentWalls.add(wall);
			}			
		}

		for (Iterator<Door> it = doors.iterator(); it.hasNext();) {
			Door door = it.next();
			if(Collisions.intersection(circle, door.line()) && !door.isOpen()) {
				adjacentWalls.add(door);
			}			
		}

		return adjacentWalls;
	}
	
	private LetterPuzzle makeLetterPuzzle(Door codeDoorOpen) {
		List<LetterTile> tiles = new ArrayList<>();
		tiles.add(new LetterTile('E', new Point(0, -500)));
		tiles.add(new LetterTile('R', new Point(100, -500)));
		tiles.add(new LetterTile('D', new Point(200, -500)));
		tiles.add(new LetterTile('C', new Point(300, -500)));
		tiles.add(new LetterTile('S', new Point(400, -500)));
		tiles.add(new LetterTile('O', new Point(500, -500)));
		tiles.add(new LetterTile('P', new Point(600, -500)));
		tiles.add(new LetterTile('N', new Point(700, -500)));
		
		Map<String, Runnable> actions = new HashMap<>();
		actions.put("CODE", () -> System.out.println("You did it!"));
		actions.put("OPEN", () -> codeDoorOpen.open());
		
		return new LetterPuzzle(tiles, actions, rocky);
	}

}
