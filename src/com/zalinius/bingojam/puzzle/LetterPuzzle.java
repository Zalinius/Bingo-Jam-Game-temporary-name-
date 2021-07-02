package com.zalinius.bingojam.puzzle;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.zalinius.zje.architecture.GameObject;
import com.zalinius.zje.architecture.Logical;
import com.zalinius.zje.physics.Locatable;

public class LetterPuzzle implements GameObject{

	private List<LetterTile> letterTiles;
	private Map<String, Runnable> puzzleActions;

	private Locatable player;

	private String codeEntry;
	private PuzzleState puzzleState;


	public LetterPuzzle(List<LetterTile> letterTiles, Map<String, Runnable> puzzleActions, Locatable player) {
		this.letterTiles = letterTiles;
		this.puzzleActions = puzzleActions;
		this.player = player;

		this.codeEntry = "";
		this.puzzleState = updateState();
	}

	@Override
	public void update(double delta) {
		puzzleState.update(delta);
	}

	@Override
	public void render(Graphics2D g) {
		letterTiles.forEach(tile -> tile.render(g));		
	}

	private void resetPuzzle() {
		letterTiles.stream().forEach((tile) -> tile.reset());
		codeEntry = "";
	}


	private interface PuzzleState extends Logical {/**/}

	public final PuzzleState updateState() {
		return new PuzzleState() {
			private double age = 0;
			private final double lifeTime = 15; //before resetting

			@Override
			public void update(double delta) {
				for (Iterator<LetterTile> it = letterTiles.iterator(); it.hasNext();) {
					LetterTile letterTile = it.next();
					if(letterTile.shape().contains(player.position().point2D())) {
						if(!letterTile.isPressed()) {
							age = 0;
							letterTile.press();
							codeEntry += letterTile.letter();

							if(puzzleActions.containsKey(codeEntry)) {
								//TODO SOUND: play a little sound
								Runnable actionForCode = puzzleActions.get(codeEntry);
								actionForCode.run();
								
								resetPuzzle();
								puzzleActions.remove(codeEntry);
								puzzleState = winAnimation();
							}
							else if(codeEntry.length() == letterTiles.size()) {
								//TODO SOUND: play WRONG sound
								resetPuzzle();
								puzzleState = wrongAnimation();
							}
						}
					}
				}
				
				age += delta;
				if(age >= lifeTime) {
					resetPuzzle();
					puzzleState = updateState();
					//TODO SOUND: play a little reset sound
				}
				
			}};
	}



	public final PuzzleState winAnimation() {
		return new PuzzleState() {
			private double age = 0;
			private final double lifeTime = 5;
			private final double tileLitTime = .3 / letterTiles.size();

			@Override
			public void update(double delta) {
				int activeTile = ((int) Math.floor(age / tileLitTime)) % letterTiles.size();

				letterTiles.forEach(tile -> tile.reset());
				letterTiles.get(activeTile).press();

				age += delta;
				if(age >= lifeTime) {
					resetPuzzle();
					puzzleState = updateState();
				}
			}};
	}


	public final PuzzleState wrongAnimation() {
		return new PuzzleState() {
			private double age = 0;
			private final double lifeTime = 5;
			private final double tileLitTime = 1;

			@Override
			public void update(double delta) {
				boolean evenTilesLit = ((int) Math.floor(age / tileLitTime)) % 2 == 0;

				for (int i = 0; i < letterTiles.size(); i++) {
					if(i % 2 == 0) {
						if(evenTilesLit) {
							letterTiles.get(i).press();
						}
						else {
							letterTiles.get(i).reset();
						}
					}
					else {
						if(!evenTilesLit) {
							letterTiles.get(i).press();
						}
						else {
							letterTiles.get(i).reset();
						}						
					}
				}

				age += delta;
				if(age >= lifeTime) {
					resetPuzzle();
					puzzleState = updateState();
				}
			}};
	}
}
