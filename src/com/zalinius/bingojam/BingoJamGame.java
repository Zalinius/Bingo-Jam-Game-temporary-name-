package com.zalinius.bingojam;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.zalinius.bingojam.levels.AbstractWorld;
import com.zalinius.bingojam.levels.DemoLand;
import com.zalinius.bingojam.plugins.Axes;
import com.zalinius.bingojam.plugins.FollowCam;
import com.zalinius.bingojam.resources.FontFactory;
import com.zalinius.bingojam.resources.Palette;
import com.zalinius.zje.architecture.GameContainer;
import com.zalinius.zje.plugins.AbstractPlugin;
import com.zalinius.zje.plugins.BackgroundColor;

public class BingoJamGame extends GameContainer{
	public static void main(String[] args) {
		BingoJamGame game = new BingoJamGame();
		game.startGame();
	}

	private AbstractWorld gameWorld;

	public BingoJamGame() {
		super("Bingo jam game (temp name)", 1000, 1000);
		this.gameWorld = new DemoLand();
		addControls(this.gameWorld.getKeyboardControls(this), null);
		prepareResources();
	}


	@Override
	public List<AbstractPlugin> getPlugins() {
		List<AbstractPlugin> plugins = new ArrayList<>();
		plugins.add(new BackgroundColor(Palette.GROUND));
		FollowCam cam = this.gameWorld.getFollowCamera();
		plugins.add(cam);
		plugins.add(new Axes(cam));
		return plugins;
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
	
	private void prepareResources() {
		FontFactory.getFont();
	}

}
