package com.zalinius.bingojam;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.zalinius.bingojam.plugins.Axes;
import com.zalinius.bingojam.plugins.FollowCam;
import com.zalinius.bingojam.resources.FontSingleton;
import com.zalinius.bingojam.worlds.World;
import com.zalinius.bingojam.worlds.WorldFactory;
import com.zalinius.zje.architecture.GameContainer;
import com.zalinius.zje.architecture.input.Inputtable;
import com.zalinius.zje.plugins.AbstractPlugin;

public class BingoJamGame extends GameContainer{
	public static void main(String[] args) {
		BingoJamGame game = new BingoJamGame();
		game.startGame();
	}

	private World gameWorld;

	public BingoJamGame() {
		super("Bingo jam game (temp name)", 1000, 1000);
		this.gameWorld = WorldFactory.theWorld();
		addControls(getControls(), null);
		prepareResources();
	}


	@Override
	public List<AbstractPlugin> getPlugins() {
		List<AbstractPlugin> plugins = new ArrayList<>();
		plugins.add(gameWorld.getBackground(()-> exit()));
		FollowCam cam = this.gameWorld.getFollowCamera();
		plugins.add(cam);
		plugins.add(new Axes(cam));
		return plugins;
	}

	private List<Inputtable> getControls(){
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
				return KeyEvent.VK_ESCAPE;
			}
		});

		inputs.addAll(gameWorld.getKeyboardControls());

		return inputs;
	}


	@Override
	public void render(Graphics2D g) {
		this.gameWorld.render(g);
	}

	@Override
	public void update(double delta) {
		this.gameWorld.update(delta);
	}

	@Override
	public void shutdownActions() {
		System.out.println("Goodbye Bingo");		
	}

	private static void prepareResources() {
		FontSingleton.getFont();
	}

}
