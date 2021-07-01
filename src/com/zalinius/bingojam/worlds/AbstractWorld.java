package com.zalinius.bingojam.worlds;

import java.util.Collection;

import com.zalinius.bingojam.plugins.FollowCam;
import com.zalinius.zje.architecture.GameObject;
import com.zalinius.zje.architecture.input.Inputtable;

public abstract class AbstractWorld implements GameObject{
	public abstract FollowCam getFollowCamera();
	public abstract Collection<Inputtable> getKeyboardControls();
}
