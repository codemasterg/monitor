package org.cmg.observer;

import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.cmg.data.MonitorDBKey;
import org.cmg.data.MonitorData;
import org.cmg.data.MonitorStatus;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mapdb.BTreeMap;
import org.mapdb.DB;

import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;


/**
 * This class is responsible for sending emails to a distribution list defined by property org.cmg.notify.email
 * @author greg
 *
 */
public class EmailNotifier implements Observer {

	private static final Logger logger = Logger.getLogger(EmailNotifier.class.getName());
	
	// Injected database the map are obtained from it.
	private DB database;
	private BTreeMap<MonitorDBKey,MonitorData> monitorDataMap;
	
	// Injected props
	private Properties props;
	
	@PostConstruct
	public void init() throws Exception {
		  this.monitorDataMap = database.getTreeMap("monitorDataMap");
	}
	
	@Override
	public void update(Observable o, Object arg) {
		GpioPinDigitalStateChangeEvent event = (GpioPinDigitalStateChangeEvent)arg;
		
		if (event.getState() == PinState.HIGH)
		{
			MonitorData monitorData = monitorDataMap.get(MonitorDBKey.MONITOR_DATA);
			int numDetection = monitorData.getNumDetection();
			monitorData.setNumDetection(++numDetection);
			DateTime now = new DateTime();

			DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM dd, yyyy HH:mm:ss.SSS");  // August 20, 2014 22:08:13.123
			monitorData.setMostRecentDetectionDate(fmt.print(now));
			monitorDataMap.put(MonitorDBKey.MONITOR_DATA, monitorData);
			database.commit();
			logger.log(Level.INFO, "Motion detected, updated DB: " + monitorData);

			// only send notification if enabled
			if (monitorData.getStatus() == MonitorStatus.ENABLED)
			{
				// set any needed mail.smtps.* properties here
				Session session = Session.getInstance(props);
				MimeMessage msg = new MimeMessage(session);

				// set the message content here
				Transport t = null;
				try
				{
					t = session.getTransport("smtps");

					t.connect(props.getProperty("mail.smtp.host"), props.getProperty("mail.sender.account"), props.getProperty("mail.sender.passwd"));
					msg.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(props.getProperty("mail.distro.list")));
					msg.setSubject("Motion  Detected");
					msg.setText("Woof!"
							+ "\n\n Woof!  Woof!!");
					t.sendMessage(msg, msg.getAllRecipients());
				} 
				catch(Exception e)
				{
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
				finally 
				{
					try 
					{
						if (t != null)
						{
							t.close();
						}
					} catch (MessagingException e) {
						logger.log(Level.SEVERE, e.getMessage(), e);
					}
				}
			}
		}
		else 
		{
			logger.log(Level.WARNING, "Motion detected, but notifications are currently disabled.");
		}
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public void setDatabase(DB database) {
		this.database = database;
	}
}
