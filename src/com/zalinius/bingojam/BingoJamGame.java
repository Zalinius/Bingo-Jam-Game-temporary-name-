package com.zalinius.bingojam;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.zalinius.bingojam.resources.FontSingleton;
import com.zalinius.bingojam.worlds.World;
import com.zalinius.bingojam.worlds.WorldFactory;
import com.zalinius.zje.architecture.GameContainer;
import com.zalinius.zje.architecture.input.actions.Axisable;
import com.zalinius.zje.architecture.input.actions.Inputtable;
import com.zalinius.zje.architecture.input.types.BinaryInput;
import com.zalinius.zje.plugins.AbstractPlugin;
import com.zalinius.zje.plugins.RuntimePlugin;
import com.zalinius.zje.plugins.configuration.ScaleWhenResized;
import com.zalinius.zje.plugins.configuration.ScreenStrategy;

public class BingoJamGame extends GameContainer{

	public static void main(String[] args) {
		BingoJamGame game = new BingoJamGame();
		game.startGame();
	}

	private World gameWorld;

	public BingoJamGame() {
		super("Bingo jam game (temp name)", 1000, 1000);
		this.gameWorld = WorldFactory.theWorld(getRumbleListener());
		addControls(getControls(), null, getAxisControls());
		prepareResources();
		this.gameWorld.startMusic();
	}


	@Override
	public List<AbstractPlugin> getPlugins() {
		List<AbstractPlugin> plugins = new ArrayList<>();
		plugins.add(gameWorld.getBackground(this::exit, getRumbleListener()));
		
		ScreenStrategy screenStrategy = new ScaleWhenResized();
		RuntimePlugin cam = this.gameWorld.getCamera(screenStrategy);
		plugins.add(screenStrategy);
		plugins.add(cam);
		
		//plugins.add(new Axes(cam));
		return plugins;
	}

	private List<Inputtable> getControls(){
		List<Inputtable> inputs = new ArrayList<>();
		inputs.add(exitGameInput(BinaryInput.KEY_ESCAPE));
		inputs.add(exitGameInput(BinaryInput.BUTTON_BACK));
		inputs.addAll(gameWorld.getKeyboardControls());

		return inputs;
	}
	
	private List<Axisable> getAxisControls(){
		List<Axisable> axisInputs = new ArrayList<>();
		axisInputs.addAll(gameWorld.getAxisControls());
		return axisInputs;
	}
	
	private Inputtable exitGameInput(BinaryInput input) {
		return new Inputtable() {

			@Override
			public void released() {
				//Do nothing
			}

			@Override
			public void pressed() {
				exit();
			}

			@Override
			public BinaryInput binaryInput() {
				return input;
			}
		};
	}

	@Override
	public void render(Graphics2D g) {
		this.gameWorld.render(g);
	}

	@Override
	public void update(double delta) {
		this.gameWorld.update(delta);
	}

	private static void prepareResources() {
		FontSingleton.getFont();
	}

}
