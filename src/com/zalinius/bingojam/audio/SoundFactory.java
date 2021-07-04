package com.zalinius.bingojam.audio;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

public class SoundFactory {

	public static UGen bass(int midiNote, double intensity, Buffer instrument) {
		float freq = Pitch.mtof(midiNote);
		WavePlayer wp = new WavePlayer(freq, instrument);
		Gain g = new Gain(1, new Envelope(0));
		g.addInput(wp);
		((Envelope)g.getGainUGen()).addSegment((float)intensity, 50);
		((Envelope)g.getGainUGen()).addSegment(0, 200, new KillTrigger(g));
		
		return g;
	}
	
	public static UGen mainNote(int midiNote, Buffer buffer, double beatLength) {
		float freq = Pitch.mtof(midiNote);
		WavePlayer wp = new WavePlayer(freq, buffer);
		Gain g = new Gain(1, new Envelope(0));
		g.addInput(wp);
		
		((Envelope)g.getGainUGen()).addSegment(0.1f, (float)beatLength/4);
		((Envelope)g.getGainUGen()).addSegment(0.1f, (float)beatLength/2);
		((Envelope)g.getGainUGen()).addSegment(0, (float)beatLength/4, new KillTrigger(g));
		
		return g;
	}

}
