package com.demo;

import java.text.DateFormat;
import java.util.Date;

import javax.enterprise.inject.Produces;

import com.demo.qualifier.TimeZone;


/**
 * Demonstrates basic concepts of CDI, this is a injectable component qualified as a "default form" date, e.g. Jan 13, 2012
 * Also demonstrates use of a @Producer (for method that returns objects that are not beans, objects whose values may vary at runtime)
 * 
 * @author greg
 *
 */
public class DefaultDate implements DateFormatter {
	
	private java.util.Random random = new java.util.Random( System.currentTimeMillis() );

	@Override
	public String getDate()
	{
		Date currentDate = new Date();
		DateFormat dateFormatter;

		dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
		return dateFormatter.format(currentDate);
	}
	
	@Produces @TimeZone
	public String getTimeZone()
	{
		/**
		 * Randomize timezone style to demonstrate how a producer method that returns dynamic values can be used.
		 */
		int style = (random.nextInt() %2 == 0) ? java.util.TimeZone.LONG : java.util.TimeZone.SHORT;
		
		return DateFormat.getDateInstance().getTimeZone().getDisplayName(true, style);
	}
}
