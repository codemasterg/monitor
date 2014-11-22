package com.demo.webservice;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.TimerService;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;

import com.demo.CountdownTimer;
import com.demo.DateFormatter;
import com.demo.qualifier.LongForm;
import com.demo.qualifier.ShortForm;
import com.demo.qualifier.TimeZone;

/**
 * Demonstrates the use of basic CDI with qualifiers
 * 
 * @author greg
 *
 */
@WebService   
public class CurrentDate {   
	
	@Inject @LongForm DateFormatter longDate;
	
	@Inject @ShortForm DateFormatter shortDate;  
	
	@Inject DateFormatter defaultDate;  		// demonstrate Default (no qualifier), MEDIUM date format in this case
	
	DateFormatter formalDateFormatter;			// demonstrates method parameter injection (see setter)
	
	@Inject @TimeZone Instance <String> timeZone;	// demonstrates use of @Producer dynamic values (injection of non managed bean whose value changes are runtime)
	
	@EJB CountdownTimer timer;

	/**
	 * WS operation to return a formatted date string given a DateFormat.
	 * 
	 * @param format
	 * BASIC - 05/23/12
	 * FORMAL - September 21, 2012
	 * UNDEFINED - Aug 15, 2015
	 * 
	 * @return current date, formatted
	 */
	@WebMethod
	public String getDate(DateFormat format)
	{
		if (format == null)
		{
			return "Invalid format specified " + format ;
		}
		
		String dateString = null;
				
		switch(format)
		{
		case BASIC:
			dateString = shortDate.getDate();
			break;
			
		case FORMAL:
			dateString = longDate.getDate();
			break;
			
		default:
			dateString = defaultDate.getDate();
			break;
			
		}
		
		return dateString;
	}
	
	/**
	 * WS operation to get a formal date string.
	 * 
	 * @return e.g. January 21, 1995
	 */
	@WebMethod
	public String getFormalDate()
	{
		
		return this.formalDateFormatter.getDate();
	}
	
	/**
	 * Demonstrates method parameter injection
	 * 
	 * @param formatter
	 * @return
	 */
	@SuppressWarnings("unused")
	@Inject 
	private void setFormalDateFormatter(@LongForm DateFormatter formatter)
	{
		this.formalDateFormatter = formatter;
	}
	
	/**
	 * Demonstrates use of a CDI Producer 
	 * @return
	 */
	@WebMethod
	public String getTimeZone()
	{
		return this.timeZone.get();
	}
	
	@WebMethod
	public void setCountdownAlarm(int countdownInSeconds)
	{
		timer.startCountdown(countdownInSeconds);
	}
	
	
	public static enum DateFormat
	{
		DEFAULT,	// format example: Jan 12, 2012
		BASIC,		// format example: 06/23/12
		FORMAL;		// format example, January 13. 2011
	}
}
