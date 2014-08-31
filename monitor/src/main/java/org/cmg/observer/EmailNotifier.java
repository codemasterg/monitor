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
			
			Session session = Session.getInstance(this.props,
			  new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(props.getProperty("mail.sender.account"), password);
				}
			  });
	 
			try {
	 
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("from-email@gmail.com"));
				message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("to-email@gmail.com"));
				message.setSubject("Testing Subject");
				message.setText("Dear Mail Crawler,"
					+ "\n\n No spam to my email, please!");
	 
				Transport.send(message);
	 
				System.out.println("Done");
	 
			} catch (MessagingException e) {
				throw new RuntimeException(e);
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
