package com.zalinius.bingojam.audio;

import java.util.ArrayList;
import java.util.List;

import com.zalinius.bingojam.worlds.World;
import com.zalinius.zje.music.Track;
import com.zalinius.zje.music.pitch.AbsolutePitch;
import com.zalinius.zje.music.pitch.EightPitchScale;
import com.zalinius.zje.music.pitch.IndexList;
import com.zalinius.zje.music.pitch.Melody;

import net.beadsproject.beads.core.UGen;

public class MusicTrack extends Track {

	private Melody bassMelody;
	private Melody mainMelody;
	
	private World world;
	
	public MusicTrack(World world) {
		super(1000);
		
		this.world = world;
		bassMelody = createBassMelody(AreaInfo.gray().getScale());
		mainMelody = createMainMelody(AreaInfo.gray().getScale(), AreaInfo.gray().root());
	}

	@Override
	protected void update(int beats, long ticks) {
		AreaInfo info = getAreaInfo();
		bassMelody.setScale(info.getScale());
		mainMelody.setScale(info.getScale());
		mainMelody.setRoot(info.root());
		if(ticks % 16 == 0) {
			AbsolutePitch pitch = mainMelody.next();
			
			UGen bass = SoundFactory.mainNote(pitch.midiPitch(), info.mainInstrument(), getBeatLength());
			ac.out.addInput(bass);
		}
		if(ticks % 16 == 8) {
			AbsolutePitch pitch = bassMelody.next();
			float intensity = 0.2f;
			
			UGen bass = SoundFactory.bass(pitch.midiPitch(), intensity, info.bassInstrument());
			ac.out.addInput(bass);
		}
	}

	@Override
	public double getBeatLength() {
		return getAreaInfo().beatLength();
	}
	
	public static Melody createBassMelody(EightPitchScale scale) {
		List<Integer> indices = new ArrayList<>();
		indices.add(5);
		indices.add(3);
		indices.add(5);
		indices.add(7);

		indices.add(5);
		indices.add(3);
		indices.add(5);
		indices.add(4);

		return new Melody(new AbsolutePitch(AbsolutePitch.MIDDLE_C.midiPitch() - 3*AbsolutePitch.OCTAVE_LENGTH), scale, new IndexList(indices));
	}

	public static Melody createMainMelody(EightPitchScale scale, int root) {
		List<Integer> indices = new ArrayList<>();
		indices.add(0);
		indices.add(2);
		indices.add(4);
		indices.add(4);

		indices.add(0);
		indices.add(2);
		indices.add(4);
		indices.add(7);

		indices.add(4);
		indices.add(7);
		indices.add(4);
		indices.add(7);

		indices.add(6);
		indices.add(5);
		indices.add(6);
		indices.add(5);
		
		indices.add(4);
		indices.add(3);
		indices.add(2);
		indices.add(0);
		
		indices.add(1);
		indices.add(0);
		indices.add(1);
		indices.add(2);
		
		indices.add(5);
		indices.add(3);
		indices.add(1);
		indices.add(0);

		return new Melody(new AbsolutePitch(AbsolutePitch.MIDDLE_C.midiPitch() + root), scale, new IndexList(indices));
	}

	
	
	public AreaInfo getAreaInfo() {
		if(world.inRedZone()) {
			return AreaInfo.red();
		}
		else if(world.inGreenZone()) {
			return AreaInfo.green();
		}
		else if(world.inBlueZone()) {
			return AreaInfo.blue();
		}
		else {
			return AreaInfo.gray();
		}
	}


}
