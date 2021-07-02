package com.zalinius.bingojam.puzzle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zalinius.bingojam.Rocky;
import com.zalinius.bingojam.pieces.Door;
import com.zalinius.zje.physics.Point;

public class PuzzleFactory {

	public static LetterPuzzle makeDemoLandLetterPuzzle(Rocky rocky, Door codeDoorOpen) {
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
