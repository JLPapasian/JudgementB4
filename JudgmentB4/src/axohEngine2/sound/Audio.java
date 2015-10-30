package axohEngine2.sound;

import java.io.*;
import sun.audio.*;
 

public class Audio {

	 private static AudioStream audioStream;
	 
	 
	public static void PlayAudio(boolean stop) 
	throws Exception
	{
		
		// open the sound file as a Java input stream
	    String gongFile = "audioclips/2.au";
	    InputStream in = new FileInputStream(gongFile);
	 
	    // create an audiostream from the inputstream
	    audioStream = new AudioStream(in);
	 
	    // play the audio clip with the audioplayer class
	    if(stop == false){
	    	AudioPlayer.player.start(audioStream);
	    }
	   	if(stop == true){
	   		AudioPlayer.player.stop(audioStream);
	    }
	}
	
}


