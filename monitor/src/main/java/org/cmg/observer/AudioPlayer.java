package org.cmg.observer;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;


/**
 * This class implements the Observer interface, it's responsible for triggering the play of an audio file.
 * 
 * @author greg
 *
 */
public class AudioPlayer implements Observer {

	private static final Logger logger = Logger.getLogger(AudioPlayer.class.getName());
	
	@Value(value="${org.cmg.audio.cmd}")
	private String audioExec;
	
	@Value(value="${org.cmg.audio.file}")
	private String  audioFilePathString;
	
	private String audioCommand;

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	@PostConstruct
	public void init() throws Exception {
		
		File audioFile = new File(this.audioFilePathString);
		
		if (audioFile.isFile() == false)
		{
			logger.log(Level.SEVERE, "The property org.cmg.audio.file is set to the value " + this.audioFilePathString + " but that is not a file");
		}
		else
		{
			this.audioCommand = audioExec + " " + audioFilePathString + "&";  // note command run in background since audio can play well after motion event
		}
	}

	/**
	 * Spawns a process defined by the property org.cmg.audo.cmd to play the audio file defined by org.cmg.audio.file.
	 */
	@Override
	public void update(Observable o, Object arg) {
		GpioPinDigitalStateChangeEvent event = (GpioPinDigitalStateChangeEvent)arg;

		if (event.getState() == PinState.HIGH)
		{
			logger.log(Level.INFO, "executing: " + this.audioCommand);
			
			// Execute the command that plays an audio file.  It's intended to run in the background so don't block.
			this.playAudio();
		}
	}
	
	/**
	 * Execute command to play audio
	 */
	public void playAudio()
	{
		// Execute the command that plays an audio file.  It's intended to run in the background so don't block.
		try {
			Process p = Runtime.getRuntime().exec(this.audioCommand);
		}
		catch (Exception e)
		{
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
}
