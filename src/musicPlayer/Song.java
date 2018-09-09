package musicPlayer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Song implements LineListener, Runnable{
	boolean playCompleted;
	private InputStream audioFilePath;
	public boolean isRunning(){
		return !playCompleted;
	}
	public Song(InputStream songFile){
		this.audioFilePath = songFile;
	}
	public void stop(){
		playCompleted=true;
	}
	public void run(){
		try{
			//add buffer for mark/reset support
			InputStream bufferedIn = new BufferedInputStream(audioFilePath);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class,format);
			Clip audioClip = (Clip)AudioSystem.getLine(info);
			audioClip.addLineListener(this);
			audioClip.open(audioStream);
			audioClip.start();
			while(!playCompleted){
				// wait for the playback completes
				try{
					Thread.sleep(10);
				}catch(InterruptedException ex){
					ex.printStackTrace();
				}
			}
			audioClip.close();
		}catch(UnsupportedAudioFileException ex){
			System.err.println("The specified audio file is not supported.");
			ex.printStackTrace();
		}catch(LineUnavailableException ex){
			System.err.println("Audio line for playing back is unavailable.");
			ex.printStackTrace();
		}catch(IOException ex){
			System.err.println("Error playing the audio file.");
			ex.printStackTrace();
		}
	}

	public void play(){
		playCompleted = false;
		new Thread(this).start();
	}

	@Override
	public void update(LineEvent event){
		if(event.getType()==LineEvent.Type.STOP){
			playCompleted = true;
		}
	}
}