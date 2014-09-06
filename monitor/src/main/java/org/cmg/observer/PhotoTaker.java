package org.cmg.observer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;

import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;


/**
 * This class implements the Observer interface, it's responsible for triggering an attached camera to take a picture.
 * 
 * @author greg
 *
 */
public class PhotoTaker implements Observer {

	private static final Logger logger = Logger.getLogger(PhotoTaker.class.getName());

	@Value(value="${org.cmg.camera.cmd}")
	private String cameraCommand;

	@PostConstruct
	public void init() throws Exception {

	}

	@Override
	public void update(Observable o, Object arg) {
		GpioPinDigitalStateChangeEvent event = (GpioPinDigitalStateChangeEvent)arg;

		if (event.getState() == PinState.HIGH)
		{
			try 
			{
				Process p = Runtime.getRuntime().exec(cameraCommand);
				p.waitFor();

				BufferedReader reader = 
						new BufferedReader(new InputStreamReader(p.getInputStream()));

				String line = "";
				StringBuffer sb = new StringBuffer();
				while ((line = reader.readLine())!= null) {
					sb.append(line + "\n");
				}

				logger.log(Level.INFO, "Photo taken " + sb);
			}
			catch (Exception e)
			{
				logger.log(Level.WARNING, e.getMessage(), e);
			}

		}
	}
}
