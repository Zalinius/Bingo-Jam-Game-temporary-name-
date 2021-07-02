package com.zalinius.bingojam.worlds;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zalinius.bingojam.Rocky;
import com.zalinius.bingojam.pieces.Door;
import com.zalinius.bingojam.pieces.Pitfall;
import com.zalinius.bingojam.pieces.Ramp;
import com.zalinius.bingojam.pieces.RespawnPoint;
import com.zalinius.bingojam.pieces.Wall;
import com.zalinius.bingojam.puzzle.Barrel;
import com.zalinius.bingojam.puzzle.LetterPuzzle;
import com.zalinius.bingojam.puzzle.PressurePlate;
import com.zalinius.bingojam.puzzle.PuzzleFactory;
import com.zalinius.bingojam.utilities.Geometry;
import com.zalinius.zje.physics.Point;

public class WorldFactory {
	
	private static void attachWorld(World world, Rocky rocky, List<Wall> walls, List<Pitfall> pitfalls, List<Ramp> ramps, List<RespawnPoint> respawnPoints, List<Door> doors, List<LetterPuzzle> puzzle, List<Barrel> barrels, List<PressurePlate> plates) {
		world.setRocky(rocky);
		world.setWalls(walls);
		world.setPitfalls(pitfalls);
		world.setRamps(ramps);
		world.setRespawnPoints(respawnPoints);
		world.setDoors(doors);
		world.setPuzzles(puzzle);
		world.setBarrels(barrels);
		world.setPlates(plates);
	}
	
	public static World demoLand() {
		World world = new World();
		
 		Rocky rocky = new Rocky(world);

		List<Wall> walls = Arrays.asList(new Wall(new Point(200, 200), new Point(300, 200), 1), new Wall(new Point(350, 200), new Point(450, 200), true),
				new Wall(new Point(600, -700), new Point(600, -900), 1), new Wall(new Point(600, -700), new Point(800, -700), 1) );
		List<Pitfall> pitfalls = Arrays.asList(new Pitfall(new Point(-250, -250), 200, 200));
		List<Ramp> ramps = Arrays.asList(new Ramp(new Point(-500, 300), 400, 200));
		List<RespawnPoint> respawnPoints = Arrays.asList(buildRespawnPoint(rocky, new Point()), buildRespawnPoint(rocky, new Point(300, 300)));
		
		Door codeDoorOpen = new Door(new Point(-400, -100), new Point(-400, 100));
		List<Door> doors = Arrays.asList(codeDoorOpen);
		
		LetterPuzzle simplePuzzle = PuzzleFactory.makeDemoLandLetterPuzzle(rocky, codeDoorOpen);
		List<LetterPuzzle> puzzles =new ArrayList<>();
		puzzles.add(simplePuzzle);
		
		
	    List<Barrel> barrels = Arrays.asList(new Barrel(new Point(100, 100), 25, world), new Barrel(new Point(425, -150), 25, world), new Barrel(new Point(500, -150), 25, world));
		List<PressurePlate> plates = Arrays.asList(new PressurePlate(new Point(450, -250), 75));
		
		attachWorld(world, rocky, walls, pitfalls, ramps, respawnPoints, doors, puzzles, barrels, plates);
		
		return world;		
	}
	
	
	
	
	private static RespawnPoint buildRespawnPoint(Rocky rocky, Point respawnPoint) {
		Runnable setRespawn = () -> rocky.setRespawn(respawnPoint);
		Shape respawnTrigger = Geometry.centeredSquare(respawnPoint, 100);
		return new RespawnPoint(respawnTrigger, rocky, setRespawn);
	}

}
