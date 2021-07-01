package com.zalinius.bingojam.pieces;

import java.awt.Graphics2D;
import java.awt.Shape;

import com.zalinius.bingojam.resources.Palette;
import com.zalinius.zje.architecture.GameObject;
import com.zalinius.zje.physics.Locatable;

public class RespawnPoint implements GameObject{

	private Shape respawnAreaTrigger;
	
	private Locatable player;
	private Runnable setRespawnPointAction;
		
	public RespawnPoint(Shape respawnAreaTrigger, Locatable player,	Runnable setRespawnPointAction) {
		this.respawnAreaTrigger = respawnAreaTrigger;
		this.player = player;
		this.setRespawnPointAction = setRespawnPointAction;
	}

	@Override
	public void update(double delta) {
		if(respawnAreaTrigger.contains(player.position().point2D())){
			setRespawnPointAction.run();
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(Palette.DEBUG);
		g.draw(respawnAreaTrigger);
	}
}
