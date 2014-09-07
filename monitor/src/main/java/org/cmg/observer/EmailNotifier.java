package org.cmg.observer;

import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.cmg.data.MonitorDBKey;
import org.cmg.data.MonitorData;
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

	// Injected database the map is obtained from it.
	private DB database;
	private BTreeMap<MonitorDBKey,MonitorData> monitorDataMap;

	// Injected props
	private Properties props;

	@PostConstruct
	public void init() throws Exception {
		this.monitorDataMap = database.getTreeMap("monitorDataMap");
	}

	/**
	 * Construct an email message indicating motion has been detected and send to email server and distribution as defined by 
	 * the mail.smtp.* properties of this application's property file (monitor.properties).  The most recent photo taken before  
	 * this update is called, see ({@link org.cmg.observer.PhotoTaker} is attached to the email message.
	 * 
	 */
	@Override
	public void update(Observable o, Object arg) {
		GpioPinDigitalStateChangeEvent event = (GpioPinDigitalStateChangeEvent)arg;

		if (event.getState() == PinState.HIGH)
		{
			// only send notification if tripped (i.e. time since most recent exceeds configurable threshold).  
			MonitorData monitorData = monitorDataMap.get(MonitorDBKey.MONITOR_DATA);

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
				msg.addHeader("X-Priority", "1");   // 1 = high, 3 = normal, 5 = low.  gmail marks as 'important'

				DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM dd, yyyy HH:mm:ss");  // August 20, 2014 22:08:13
				DateTime now = new DateTime();
				
				Multipart multipart = new MimeMultipart();

				// creates body part for the message
				MimeBodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setContent(msg, "text/html");
				messageBodyPart.setText("Attention!"
						+ "\n\n Montion Detected on: " + fmt.print(now));

				// if photo exists, creates body part for the attachment 
				if (monitorData.getPhotoList().isEmpty() == false)
				{
					MimeBodyPart attachPart = new MimeBodyPart();
					attachPart.attachFile(monitorData.getPhotoList().get(monitorData.getPhotoList().size() -1));  // end of list has latest photo
					multipart.addBodyPart(attachPart);
				}

				// adds parts to the multipart
				multipart.addBodyPart(messageBodyPart);
				
				// sets the multipart as message's content
				msg.setContent(multipart);
				
				// sends the e-mail
				Thread.currentThread().setContextClassLoader( getClass().getClassLoader() );  // hate that this is needed, but needed when using java mail with MIME types
				t.sendMessage(msg, msg.getAllRecipients());
				
				logger.log(Level.INFO, "email sent to: " +  props.getProperty("mail.distro.list"));
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
