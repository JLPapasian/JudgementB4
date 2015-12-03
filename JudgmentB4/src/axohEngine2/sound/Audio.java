package axohEngine2.sound;

import java.io.*;
import java.util.Scanner;

import axohEngine2.Judgement;
import sun.audio.*;

public class Audio {

	private static AudioStream stream;
	private static boolean muted;

	public static void PlaySound(String file) {
		if (!muted) {
			try {
				// open the sound file as a Java input stream
				String gongFile = "audioclips/" + file;
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
		//if(!muted){
	
		AudioPlayer.player.stop(Judgement.titleMusic);			    
		//	}
	}
	
	
	public static void ToggleAudio() {
		muted = !muted;
		try {
			saveMuted();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean getMuted() {
		return muted;
	}
	
	public static void saveMuted() throws IOException {

		try {
			// Whatever the file path is.
			File statText = new File("audioclips/muted.txt");
			FileOutputStream is = new FileOutputStream(statText);
			OutputStreamWriter osw = new OutputStreamWriter(is);
			Writer w = new BufferedWriter(osw);

			if (!muted)
				w.write("False");
			if (muted)
				w.write("True");

			w.close();
		} catch (IOException e) {
			System.err.println("Problem writing to the file statsTest.txt");
		}
	}


	//Called when the program starts. Checks the file to see what the muted option is
	public static void loadMuted() throws IOException {
		try {
			File inFile = new File("audioclips/muted.txt");
			Scanner fileInput = new Scanner(inFile);
			String mutedRead = fileInput.next();
			if (mutedRead.contains("True"))
				muted = true;
			else
				muted = false;

			fileInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}