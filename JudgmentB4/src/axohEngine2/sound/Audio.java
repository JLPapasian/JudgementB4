package axohEngine2.sound;

import java.io.*;

import axohEngine2.Judgement;
import sun.audio.*;
 

public class Audio {

	private static AudioStream stream;
	private static boolean muted = false;
	
	
	public static void PlaySound(String file )
	{
		if(!muted){
			try{
				// open the sound file as a Java input stream
			    String gongFile = "audioclips/"+file;
			    InputStream in = new FileInputStream(gongFile);
			 
			    // create an audiostream from the inputstream
			    stream = new AudioStream(in);
			    AudioPlayer.player.start(stream);			    
			}
			catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			    
			}
	}
	
	
	public static void StartTitleMusic(String file)
	{
		if(!muted){
			try{
				// open the sound file as a Java input stream
			    String gongFile = "audioclips/"+file;
			    InputStream in = new FileInputStream(gongFile);
			 
			    // create an audiostream from the inputstream
			    Judgement.titleMusic = new AudioStream(in);
			    
			    AudioPlayer.player.start(Judgement.titleMusic);
			    
			}
			catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			    
			}
	}
	public static void StopTitleMusic()
	{
		if(!muted){
	
		AudioPlayer.player.stop(Judgement.titleMusic);			    
			}
	}
	
	
	public static void ToggleAudio()
	{
	muted=!muted;
	}
	
	public static boolean getMuted()
	{
		return muted;
	}
	
}


