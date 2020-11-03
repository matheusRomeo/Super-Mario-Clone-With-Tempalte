package com.azuryPlays.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/* para arquivos de audios grandes e pesados*/
public class Music {
	
	public static class Clips {
		public Clip[] clips;
		private int p;
		private int count;
		
		public Clips(byte[] buffer, int count) throws LineUnavailableException, IOException, UnsupportedAudioFileException  {
			if(buffer == null) 
				return;
						
			clips = new Clip[count];
			this.count = count; 
			
			for (int i =0; i<count; i++) {
				clips[i] = AudioSystem.getClip();
				clips[i].open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer)));
			}
			
		}
		public void play() {
			if (clips ==null) 
				return;
			clips[p].stop();
			clips[p].setFramePosition(0);
			clips[p].start();
			p++;
			if(p> count)
				p=0;			
		}
		
		public void loop() {
			if (clips ==null) 
				return;
			clips[p].loop(300);
		}
		
	}// fim do clips
	
	
	private static Clips load(String name, int count) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataInputStream dis = new DataInputStream(Music.class.getResourceAsStream(name));
			
			byte[] buffer = new byte[1024];
			int read = 0;
			while((read = dis.read(buffer)) >=0) {
				baos.write(buffer,0,read);				
			}
			dis.close();
			byte[] data = baos.toByteArray();
			
			return new Clips(data, count);
					
		}catch(Exception e) {
			try {
				return new Clips(null, 0);
			}
			catch(Exception ee) {
				return null;
		}
			
		}
	}
	
	/*Métodos de tocar sons
	 * chamar por ->  Sound.ClipsName.play()  ou Sound.ClipsName.loop()
	 * */
	
	public static Clips music = load("/PirateHarbor.wav", 1);
	public static Clips hurt = load("/hurt.wav", 1);
}
