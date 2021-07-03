package com.zalinius.bingojam.worlds;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.zalinius.bingojam.Rocky;
import com.zalinius.bingojam.physics.Vector3;
import com.zalinius.bingojam.pieces.Door;
import com.zalinius.bingojam.pieces.Pitfall;
import com.zalinius.bingojam.pieces.Ramp;
import com.zalinius.bingojam.pieces.RespawnPoint;
import com.zalinius.bingojam.pieces.Wall;
import com.zalinius.bingojam.puzzle.Barrel;
import com.zalinius.bingojam.puzzle.LetterPuzzle;
import com.zalinius.bingojam.puzzle.LetterTile;
import com.zalinius.bingojam.puzzle.BarrelPlate;
import com.zalinius.bingojam.puzzle.Button;
import com.zalinius.bingojam.puzzle.PuzzleFactory;
import com.zalinius.bingojam.utilities.Geometry;
import com.zalinius.zje.math.Interpolation;
import com.zalinius.zje.physics.Point;

public class WorldFactory {

	public static World theWorld() {
		World world = new World();

		Rocky rocky = new Rocky(world);

		List<Wall> walls = new ArrayList<>();
		
		walls.addAll(buildAllWithSlits(Geometry.centeredSquare(new Point(), 800), 200, Direction.NORTH));
		walls.addAll(northSouthCorridor(Geometry.centeredRectangle(new Point(0, -600), 200, 400)));
		walls.addAll(buildAllWithSlits(Geometry.centeredSquare(new Point(0, -1200), 800), 200, Direction.NORTH, Direction.SOUTH));
		walls.addAll(northSouthCorridor(Geometry.centeredRectangle(new Point(0, -1800), 200, 400)));
		walls.addAll(buildAllWithSlits(Geometry.centeredSquare(new Point(0, -2300), 600), 200, Direction.values()));

		//Code tutorial area
		walls.addAll(eastWestCorridor(Geometry.centeredRectangle(new Point(-400, -2300), 200, 200)));
		walls.addAll(buildAllWithSlits(Geometry.centeredSquare(new Point(-700, -2300), 400), 200, Direction.EAST));

		//Button Area
		walls.addAll(eastWestCorridor(Geometry.centeredRectangle(new Point( 400, -2300), 200, 200)));
		walls.addAll(buildAllWithSlits(Geometry.centeredSquare(new Point(700, -2300), 400), 200, Direction.WEST));		
		
		walls.addAll(northSouthCorridor(Geometry.centeredRectangle(new Point(0, -2700), 200, 200)));

		List<Pitfall> pitfalls = new ArrayList<>();
		pitfalls.add(new Pitfall(new Point(0, -1200), 400));

		List<Ramp> ramps = new ArrayList<>();
		ramps.add(new Ramp(new Point(0, -600), 200, 400, new Vector3(0, 1, -2)));
		walls.add(new Wall(new Point(-100, -800), new Point(100, -800), true));
		
		List<RespawnPoint> respawnPoints = new ArrayList<>();
		respawnPoints.add(buildRespawnPoint(rocky, rocky.position()) ); //initial respawn point
		respawnPoints.add(buildRespawnPoint(rocky, new Point(0, -900), 200));

		List<Door> doors = new ArrayList<>();
		Door tutorialCodeDoor = new Door(new Point(-100, -2650), new Point(100, -2650));
		doors.add(tutorialCodeDoor);
		Door tutorialButtonDoor = new Door(new Point(-100, -2750), new Point(100, -2750));
		doors.add(tutorialButtonDoor);
		
		List<Button> buttons = new ArrayList<>();
		buttons.add(new Button(new Point(700, -2300), 50, ()-> tutorialButtonDoor.open(), rocky));

		
		List<LetterPuzzle> puzzles =new ArrayList<>();
		puzzles.add(tutorialLetterPuzzle(rocky, tutorialCodeDoor));

		List<Barrel> barrels = new ArrayList<>();
		List<BarrelPlate> plates = new ArrayList<>();

		attachWorld(world, rocky, walls, pitfalls, ramps, respawnPoints, doors, puzzles, barrels, plates, buttons);

		return world;		
	}
	
	public static LetterPuzzle tutorialLetterPuzzle(Rocky rocky, Door tutorialCodeDoor) {
		List<LetterTile> tiles = new ArrayList<>();
		tiles.add(new LetterTile('E', new Point(-800, -2400)));
		tiles.add(new LetterTile('K', new Point(-700, -2400)));
		tiles.add(new LetterTile('Y', new Point(-600, -2400)));
		
		Map<String, Runnable> actions = new HashMap<>();
		actions.put("KEY", () -> tutorialCodeDoor.open());
		
		return new LetterPuzzle(tiles, actions, rocky);	
	}


	public static World demoLand() {
		World world = new World();

		Rocky rocky = new Rocky(world);

		List<Wall> walls = Arrays.asList(new Wall(new Point(200, 200), new Point(300, 200), 1), new Wall(new Point(350, 200), new Point(450, 200), true),
				new Wall(new Point(600, -700), new Point(600, -900), 1), new Wall(new Point(600, -700), new Point(800, -700), 1) );
		List<Pitfall> pitfalls = Arrays.asList(new Pitfall(new Point(-250, -250), 200, 200));
		List<Ramp> ramps = Arrays.asList(new Ramp(new Point(-500, 300), 400, 200, new Vector3(1, 0, -5)));
		List<RespawnPoint> respawnPoints = Arrays.asList(buildRespawnPoint(rocky, new Point()), buildRespawnPoint(rocky, new Point(300, 300)));

		Door codeDoorOpen = new Door(new Point(-400, -100), new Point(-400, 100));
		List<Door> doors = Arrays.asList(codeDoorOpen);
		
		List<Button> buttons = new ArrayList<>();
		buttons.add(new Button(new Point(-400, 400), 50, ()->System.out.println("Boop"), rocky));
		
		LetterPuzzle simplePuzzle = PuzzleFactory.makeDemoLandLetterPuzzle(rocky, codeDoorOpen);
		List<LetterPuzzle> puzzles =new ArrayList<>();
		puzzles.add(simplePuzzle);


		List<Barrel> barrels = Arrays.asList(new Barrel(new Point(100, 100), 25, world), new Barrel(new Point(425, -150), 25, world), new Barrel(new Point(500, -150), 25, world));
		List<BarrelPlate> plates = Arrays.asList(new BarrelPlate(new Point(450, -250), 75));

		attachWorld(world, rocky, walls, pitfalls, ramps, respawnPoints, doors, puzzles, barrels, plates, buttons);

		return world;		
	}



	private static List<Wall> buildWalls(Rectangle2D.Double rectangle){
		List<Wall> walls = new ArrayList<>();

		List<Line2D.Double> lines = Geometry.lines(rectangle);
		for (Iterator<Line2D.Double> it = lines.iterator(); it.hasNext();) {
			Line2D.Double line = it.next();

			walls.add(new Wall(new Point(line.getP1()), new Point(line.getP2())));
		}

		return walls;
	}

	public enum Direction {NORTH, EAST, SOUTH, WEST}

	private static List<Wall> buildAllButOneWall(Rectangle2D.Double rectangle, Direction direction){
		List<Wall> walls = buildWalls(rectangle);
		walls.remove(direction.ordinal());

		return walls;
	}

	private static List<Wall> buildAllWithSlits(Rectangle2D.Double rectangle, double width, Direction... directions){
		List<Wall> walls = buildWalls(rectangle);

		List<Direction> directionsList = new ArrayList<>(Arrays.asList(directions));
		directionsList.sort((Direction d1, Direction d2) -> - d1.ordinal() + d2.ordinal());
				
		for (Iterator<Direction> it = directionsList.iterator(); it.hasNext();) {
			Direction direction = it.next();
			Wall removedWall = walls.remove(direction.ordinal());
			Line2D.Double line = removedWall.line();

			if(width > Geometry.lineLength(line)) {
				width = Geometry.lineLength(line);
			}
			double interpolant = (1-(width/Geometry.lineLength(line))) / 2;

			Wall wall1 = new Wall(new Point(line.getP1()), Interpolation.linearInterpolation(new Point(line.getP1()), new Point(line.getP2()), interpolant));		
			Wall wall2 = new Wall(new Point(line.getP2()), Interpolation.linearInterpolation(new Point(line.getP1()), new Point(line.getP2()), 1-interpolant));		

			walls.add(wall1);
			walls.add(wall2);
		}
		
		return walls;
	}



	private static List<Wall> northSouthCorridor(Rectangle2D.Double rectangle){
		List<Wall> walls = buildWalls(rectangle);
		walls.remove(Direction.SOUTH.ordinal());
		walls.remove(Direction.NORTH.ordinal());
		return walls;
	}

	private static List<Wall> eastWestCorridor(Rectangle2D.Double rectangle){
		List<Wall> walls = buildWalls(rectangle);
		walls.remove(Direction.WEST.ordinal());
		walls.remove(Direction.EAST.ordinal());
		return walls;
	}


	private static RespawnPoint buildRespawnPoint(Rocky rocky, Point respawnPoint) {
		return buildRespawnPoint(rocky, respawnPoint, 100);
	}

	private static RespawnPoint buildRespawnPoint(Rocky rocky, Point respawnPoint, double width) {
		Runnable setRespawn = () -> rocky.setRespawn(respawnPoint);
		Shape respawnTrigger = Geometry.centeredSquare(respawnPoint, width);
		return new RespawnPoint(respawnTrigger, rocky, setRespawn);
	}

	private static void attachWorld(World world, Rocky rocky, List<Wall> walls, List<Pitfall> pitfalls, List<Ramp> ramps, List<RespawnPoint> respawnPoints, List<Door> doors, List<LetterPuzzle> puzzle, List<Barrel> barrels, List<BarrelPlate> plates, List<Button> buttons) {
		world.setRocky(rocky);
		world.setWalls(walls);
		world.setPitfalls(pitfalls);
		world.setRamps(ramps);
		world.setRespawnPoints(respawnPoints);
		world.setDoors(doors);
		world.setPuzzles(puzzle);
		world.setBarrels(barrels);
		world.setBarrelPlates(plates);
		world.setButtons(buttons);
	}


}
