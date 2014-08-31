package org.cmg.observer;

import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;


/**
 * This class is responsible for sending emails to a distribution list defined by property org.cmg.notify.email
 * @author greg
 *
 */
public class EmailNotifier implements Observer {

	private static final Logger logger = Logger.getLogger(EmailNotifier.class.getName());
	
	// Injected props
	private Properties props;
	
	@Override
	public void update(Observable o, Object arg) {
		GpioPinDigitalStateChangeEvent event = (GpioPinDigitalStateChangeEvent)arg;
		
		if (event.getState() == PinState.HIGH)
		{
			logger.log(Level.INFO, "Motion detected, sending email to ..");


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

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

}
