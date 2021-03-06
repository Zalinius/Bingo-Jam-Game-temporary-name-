package com.zalinius.bingojam.worlds;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.zalinius.bingojam.ChangingBackgroundColor;
import com.zalinius.bingojam.Rocky;
import com.zalinius.bingojam.RunicLine;
import com.zalinius.bingojam.audio.MusicTrack;
import com.zalinius.bingojam.physics.CollideableLine;
import com.zalinius.bingojam.physics.Kinetic;
import com.zalinius.bingojam.physics.Topographical;
import com.zalinius.bingojam.physics.Vector3;
import com.zalinius.bingojam.pieces.Door;
import com.zalinius.bingojam.pieces.Pitfall;
import com.zalinius.bingojam.pieces.Ramp;
import com.zalinius.bingojam.pieces.RespawnPoint;
import com.zalinius.bingojam.pieces.Slopable;
import com.zalinius.bingojam.pieces.TextSpot;
import com.zalinius.bingojam.pieces.Wall;
import com.zalinius.bingojam.puzzle.Barrel;
import com.zalinius.bingojam.puzzle.BarrelPlate;
import com.zalinius.bingojam.puzzle.Button;
import com.zalinius.bingojam.puzzle.LetterPuzzle;
import com.zalinius.bingojam.puzzle.PlateAnd;
import com.zalinius.bingojam.resources.Palette;
import com.zalinius.bingojam.utilities.Geometry;
import com.zalinius.zje.architecture.GameObject;
import com.zalinius.zje.architecture.input.RumbleListener;
import com.zalinius.zje.architecture.input.actions.Axisable;
import com.zalinius.zje.architecture.input.actions.Inputtable;
import com.zalinius.zje.physics.Collisions;
import com.zalinius.zje.physics.Point;
import com.zalinius.zje.plugins.AbstractPlugin;
import com.zalinius.zje.plugins.camera.AbstractCamera;
import com.zalinius.zje.plugins.camera.GentleFollowCam;
import com.zalinius.zje.plugins.configuration.ScreenStrategy;

public class World implements GameObject, Topographical{

	private Rocky rocky;
	private Collection<Wall> walls;
	private Collection<RespawnPoint> respawnPoints;
	private Collection<Door> doors;
	private Collection<Button> buttons;
	
	private Collection<Barrel> barrels;
	private Collection<BarrelPlate> barrelPlates;
	private Collection<PlateAnd> plateAnds;
	
	private Collection<Ramp> ramps;
	private Collection<Pitfall> pitfalls;
	
	private Collection<LetterPuzzle> puzzles;
	
	private Collection<TextSpot> texts;
	private List<RunicLine> lines;//because order matter
	private List<RunicLine> decor; //drawn last
	
	private Rectangle2D.Double redZone;
	private Rectangle2D.Double greenZone;
	private Rectangle2D.Double blueZone;

	private MusicTrack music;
	
	public World() {
		
		redZone = Geometry.centeredRectangle(new Point(-6800, -2100), 5200, 1600);
		greenZone = Geometry.centeredRectangle(new Point(-3500, -4400), 1200, 3200);
		blueZone = new Rectangle2D.Double(-2800, -2200, 1700, 4000);

	}
	
	public void startMusic() {
		music = new MusicTrack(this);
		music.start();
	}

	public void setRocky(Rocky rocky) {
		this.rocky = rocky;
	}

	public void setWalls(Collection<Wall> walls) {
		this.walls = walls;
	}

	public void setRespawnPoints(Collection<RespawnPoint> respawnPoints) {
		this.respawnPoints = respawnPoints;
	}

	public void setDoors(Collection<Door> doors) {
		this.doors = doors;
	}

	public void setButtons(Collection<Button> buttons) {
		this.buttons = buttons;
	}

	public void setBarrels(Collection<Barrel> barrels) {
		this.barrels = barrels;
	}

	public void setBarrelPlates(Collection<BarrelPlate> barrelPlates) {
		this.barrelPlates = barrelPlates;
	}

	public void setPlateAnds(Collection<PlateAnd> plateAnds) {
		this.plateAnds = plateAnds;
	}

	public void setRamps(Collection<Ramp> ramps) {
		this.ramps = ramps;
	}

	public void setPitfalls(Collection<Pitfall> pitfalls) {
		this.pitfalls = pitfalls;
	}

	public void setPuzzles(Collection<LetterPuzzle> puzzles) {
		this.puzzles = puzzles;
	}
	
	public void setText(Collection<TextSpot> texts) {
		this.texts = texts;
	}
	
	public void setLines(List<RunicLine> lines) {
		this.lines = lines;
	}
	public void setDecor(List<RunicLine> decor) {
		this.decor = decor;
	}

	@Override
	public void update(double delta) {
		rocky.update(delta);		

		for (Iterator<Pitfall> it = pitfalls.iterator(); it.hasNext();) {
			Pitfall pitfall = it.next();
			if(pitfall.innerShape(rocky.radius()).contains(rocky.position().point2D())) {
				rocky.disable();
				rocky.respawn();
			}			
		}

		puzzles.forEach(puzzle -> puzzle.update(delta));
		buttons.forEach(button -> button.update(delta));
		respawnPoints.forEach(res -> res.update(delta));
		barrels.forEach(barrel -> barrel.update(delta));
		barrelPlates.forEach(barrelPlate -> barrelPlate.update(delta));
		plateAnds.forEach(plateAnd -> plateAnd.update(delta));
		
		for (Iterator<BarrelPlate> itPlate = barrelPlates.iterator(); itPlate.hasNext();) {
			BarrelPlate pressurePlate = itPlate.next();
			boolean pressurePlatePressed = false;
			for (Iterator<Barrel> itBarrel = barrels.iterator(); itBarrel.hasNext();) {
				Barrel barrel = itBarrel.next();
				if(pressurePlate.shape().contains(barrel.getPhysical().position().point2D())) {
					pressurePlatePressed = true;
				}
			}
			pressurePlate.setPressed(pressurePlatePressed);
		}

	}

	@Override
	public void render(Graphics2D g) {
		lines.forEach(line -> line.render(g));
		
		pitfalls.forEach(pitfall -> pitfall.render(g));
		puzzles.forEach(puzzle -> puzzle.render(g));
		ramps.forEach(ramp -> ramp.render(g));
		respawnPoints.forEach(res -> res.render(g));
		doors.forEach(door -> door.render(g));
		buttons.forEach(button -> button.render(g));
		barrelPlates.forEach(plate -> plate.render(g));
		barrels.forEach(barrel -> barrel.render(g));
		walls.forEach(wall -> wall.render(g));
		texts.forEach(text -> text.render(g));
		decor.forEach(deco -> deco.render(g));

		rocky.render(g);
		
		g.setColor(Palette.DEBUG);
		g.draw(redZone);
		g.draw(greenZone);
		g.draw(blueZone);
	}

	public Collection<Inputtable> getKeyboardControls() {
		List<Inputtable> inputs = new ArrayList<>();
		inputs.addAll(rocky.inputs());
		return inputs;
	}
	public Collection<Axisable> getAxisControls() {
		return rocky.axisInputs();
	}

	public AbstractCamera getCamera(ScreenStrategy screenStrategy) {
		return new GentleFollowCam(rocky, screenStrategy);
	}

	@Override	
	public Vector3 getSurfaceNormal(Point position) {
		List<Slopable> slopes = new ArrayList<>();
		slopes.addAll(ramps);
		slopes.addAll(pitfalls);
		
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

	@Override
	public List<Kinetic> getKineticObjects() {
		List<Kinetic> kinetics = new ArrayList<>();
		kinetics.add(rocky);
		kinetics.addAll(barrels);
		return kinetics;
	}

	@Override
	public Kinetic getRockyKinetics() {
		return rocky;
	}

	public AbstractPlugin getBackground(Runnable exitAction, RumbleListener rumbleListener) {
		return new ChangingBackgroundColor(Palette.GROUND, Palette.BRIGHT, rocky, exitAction, rumbleListener);
	}
	
	public boolean inRedZone() {
		return redZone.contains(rocky.position().point2D());
	}
	
	public boolean inGreenZone() {
		return greenZone.contains(rocky.position().point2D());
	}
	
	public boolean inBlueZone() {
		return blueZone.contains(rocky.position().point2D());
	}



}
