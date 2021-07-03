package com.zalinius.bingojam.puzzle;

import java.util.List;

import com.zalinius.zje.architecture.Logical;

public class PlateAnd implements Logical{
	
	private List<BarrelPlate> plates;
	private Runnable trueAction;
	private Runnable falseAction;
	
	public PlateAnd(List<BarrelPlate> plates, Runnable trueAction, Runnable falseAction) {
		this.plates = plates;
		this.trueAction = trueAction;
		this.falseAction = falseAction;
	}
	
	@Override
	public void update(double delta) {
		if(plates.stream().allMatch(b -> b.isPressed())) {
			trueAction.run();
		}
		else {
			falseAction.run();
		}
	}
}
