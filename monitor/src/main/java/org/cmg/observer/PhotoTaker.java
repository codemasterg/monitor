package org.cmg.observer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.cmg.data.MonitorDBKey;
import org.cmg.data.MonitorData;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
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

	// Injected database the map is obtained from it.
	private DB database;
	private BTreeMap<MonitorDBKey,MonitorData> monitorDataMap;
	
	@Value(value="${org.cmg.camera.cmd}")
	private String cameraExec;
	
	@Value(value="${org.cmg.camera.output.dir}")
	private String  photoDirectoryString;
	
	private File photoDirectory;
	private String cameraCommandPrefix;

	@PostConstruct
	public void init() throws Exception {
		// make photo dir if it does not already exist
		this.photoDirectory = new File(this.photoDirectoryString);
		if (photoDirectory.isDirectory() == false)
		{
			photoDirectory.mkdir();
		}
		this.cameraCommandPrefix = cameraExec + " ";
		
		this.monitorDataMap = database.getTreeMap("monitorDataMap");
	}

	@Override
	public void update(Observable o, Object arg) {
		GpioPinDigitalStateChangeEvent event = (GpioPinDigitalStateChangeEvent)arg;

		if (event.getState() == PinState.HIGH)
		{
			try 
			{
				// use date time in file name that photo should be written to so prior photos not overwritten
				DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM-dd-yyyy-HH:mm:ss");  // August-20-2014-22:08:13
				DateTime now = new DateTime();
				File photoFile = new File(photoDirectory + "/" + fmt.print(now) + ".jpg");
				
				String cameraCommand = cameraCommandPrefix + photoFile;
				logger.log(Level.INFO, "executing: " + cameraCommand);
				
				// Execute the command that takes a picture
				Process p = Runtime.getRuntime().exec(cameraCommand);
				p.waitFor();
				
				// if photo file written update DB with photo file path
				if (photoFile.isFile())
				{
					MonitorData monitorData = this.monitorDataMap.get(MonitorDBKey.MONITOR_DATA);
					monitorData.getPhotoList().add(photoFile);
					database.commit();
				}

				this.logCommandOutput(p);
			}
			catch (Exception e)
			{
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		}
	}

	private void logCommandOutput(Process p) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

		String line = "";
		StringBuffer sb = new StringBuffer();
		while ((line = reader.readLine())!= null) {
			sb.append(line + "\n");
		}

		logger.log(Level.INFO, "Photo taken " + sb);
	}

	public void setDatabase(DB database) {
		this.database = database;
	}
	
}
