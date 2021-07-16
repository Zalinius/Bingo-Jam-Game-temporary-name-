package com.zalinius.bingojam.controls;

import org.libsdl.SDL_Error;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.zalinius.zje.architecture.Logical;
import com.zalinius.zje.physics.Vector;

import uk.co.electronstudio.sdl2gdx.SDL2Controller;
import uk.co.electronstudio.sdl2gdx.SDL2ControllerManager;

public class RockyGamepadInput implements Logical{

	private SDL2ControllerManager manager;
	private SDL2Controller activeController;
	private float x;
	private float y;
	
	public RockyGamepadInput() {
		this.x  = 0;
		this.y   = 0;
		manager = new SDL2ControllerManager();
		manager.addListenerAndRunForConnectedControllers(new ControllerListener() {
			
			@Override
			public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean povMoved(Controller controller, int povCode, PovDirection value) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void disconnected(Controller controller) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void connected(Controller controller) {
				SDL2Controller sdl2Controller = (SDL2Controller) controller;
				if(sdl2Controller.getPlayerIndex() == 0) {
					activeController = sdl2Controller;
				}
				
			}
			
			@Override
			public boolean buttonUp(Controller controller, int buttonCode) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean buttonDown(Controller controller, int buttonCode) {
				// TODO Auto-generated method stub
				System.out.println("boop");
				return false;
			}
			
			@Override
			public boolean axisMoved(Controller controller, int axisCode, float value) {
				if(axisCode == 0) {
					x = value;
				}
				else if(axisCode == 1) {
					y = value;
				}
				return false;
			}
			
			@Override
			public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
	
	public Vector getInput() {
		if(activeController == null) {
			return new Vector();
		}
		
		
		Vector direction = new Vector(x, y);
		if(direction.isZeroVector()) {
			
		}
		else if(direction.length() > 1) {
			direction = direction.normalize();
		}
		
		return direction;
	}

	public void update(double delta) {
		try {
			manager.pollState();
		} catch (SDL_Error e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
