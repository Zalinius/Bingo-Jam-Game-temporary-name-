package com.zalinius.bingojam.audio;

import com.zalinius.zje.music.pitch.AbsolutePitch;
import com.zalinius.zje.music.pitch.EightPitchScale;
import com.zalinius.zje.music.pitch.ScaleFactory;
import com.zalinius.zje.music.synths.SynthFactory;

import net.beadsproject.beads.data.Buffer;

public class AreaInfo {

	private double beatLength;
	private int root;
	private EightPitchScale scale;
	private Buffer bassInstrument;
	private Buffer mainInstrument;
	
	
	
	public AreaInfo(double beatLength, int root, EightPitchScale scale, Buffer bassInstrument, Buffer mainInstrument) {
		this.bassInstrument = bassInstrument;
		this.root = root;
		this.scale = scale;
		this.beatLength = beatLength;
		this.mainInstrument = mainInstrument;
	}
	
	private static AreaInfo RED_AREA_INFO;
	private static AreaInfo GREEN_AREA_INFO;
	private static AreaInfo BLUE_AREA_INFO;
	private static AreaInfo GRAY_AREA_INFO;
	
	
	public static AreaInfo red() {
		if(RED_AREA_INFO == null) {
			RED_AREA_INFO = new AreaInfo(750, AbsolutePitch.MIDDLE_C.midiPitch() - 1*AbsolutePitch.OCTAVE_LENGTH, ScaleFactory.minorHarmonicScale(), SynthFactory.RESONANT_SHARP, SynthFactory.SAW_STRONG);
		}
		
		return RED_AREA_INFO;
	}

	public static AreaInfo green() {
		if(GREEN_AREA_INFO == null) {
			GREEN_AREA_INFO = new AreaInfo(1000, AbsolutePitch.MIDDLE_C.midiPitch() - 1*AbsolutePitch.OCTAVE_LENGTH, ScaleFactory.minorScale(), SynthFactory.RESONANT_STRONG, SynthFactory.SAW_SOFT);
		}
		
		return GREEN_AREA_INFO;
	}

	public static AreaInfo blue() {
		if(BLUE_AREA_INFO == null) {
			BLUE_AREA_INFO = new AreaInfo(1250, AbsolutePitch.MIDDLE_C.midiPitch() - 1*AbsolutePitch.OCTAVE_LENGTH, ScaleFactory.minorScale(), SynthFactory.WIND_SOFT, SynthFactory.WIND_GLASS);
		}
		
		return BLUE_AREA_INFO;
	}

	public static AreaInfo gray() {
		if(GRAY_AREA_INFO == null) {
			GRAY_AREA_INFO = new AreaInfo(1500,AbsolutePitch.MIDDLE_C.midiPitch() - 1*AbsolutePitch.OCTAVE_LENGTH, ScaleFactory.majorScale(), SynthFactory.RESONANT, SynthFactory.STRING_STRONG);
		}
		
		return GRAY_AREA_INFO;
	}

	
	
	
	
	public double beatLength() {return beatLength;}
	public int root() {return root;}
	public EightPitchScale getScale() {return scale;}
	public Buffer bassInstrument() {return bassInstrument;}
	public Buffer mainInstrument() {return mainInstrument;}
}
