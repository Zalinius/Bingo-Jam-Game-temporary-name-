package com.zalinius.bingojam;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.zalinius.bingojam.plugins.Axes;
import com.zalinius.bingojam.plugins.FollowCam;
import com.zalinius.zje.architecture.GameContainer;
import com.zalinius.zje.plugins.AbstractPlugin;
import com.zalinius.zje.plugins.BackgroundColor;

public class BingoJamGame extends GameContainer{
	public static void main(String[] args) {
		BingoJamGame game = new BingoJamGame();
		game.startGame();
	}

	private World gameWorld;

	public BingoJamGame() {
		super("Bingo jam game (temp name)", 1000, 1000);
		gameWorld = new World();
		addControls(gameWorld.getKeyboardControls(this), null);
	}


	@Override
	public List<AbstractPlugin> getPlugins() {
		List<AbstractPlugin> plugins = new ArrayList<>();
		plugins.add(new BackgroundColor(Color.DARK_GRAY.darker().darker()));
		FollowCam cam = gameWorld.getFollowCamera();
		plugins.add(cam);
		plugins.add(new Axes(cam));
		return plugins;
	}


	@Override
	public void render(Graphics2D g) {
		gameWorld.render(g);
	}

	@Override
	public void update(double delta) {
		gameWorld.update(delta);
	}

	@Override
	public void shutdownActions() {
		System.out.println("Goodbye Bingo");		
	}

}
