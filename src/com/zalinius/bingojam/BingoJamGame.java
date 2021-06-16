package com.zalinius.bingojam;

import java.awt.Graphics2D;

import com.zalinius.zje.architecture.GameContainer;

public class BingoJamGame extends GameContainer{
	
	public static void main(String[] args) {
		BingoJamGame game = new BingoJamGame();
		game.startGame();
	}

	
	public BingoJamGame() {
		super("Bingo jam game (temp name)", 400, 400);
	}


	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub
		
	}

}
