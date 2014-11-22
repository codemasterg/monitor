package com.demo;

import java.text.DateFormat;
import java.util.Date;

import com.demo.qualifier.LongForm;

/**
 * Demonstrates basic concepts of CDI, this is a injectable component qualified as a "long form" date, e.g. January 13, 2012
 * 
 * @author greg
 *
 */
@LongForm
public class FormalDate implements DateFormatter {
	
	@Override
	public String getDate()
	{
		Date currentDate = new Date();
		DateFormat dateFormatter;

		dateFormatter = DateFormat.getDateInstance(DateFormat.LONG);
		return dateFormatter.format(currentDate);
	}

}
