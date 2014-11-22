package com.demo;

import java.text.DateFormat;
import java.util.Date;

import com.demo.qualifier.ShortForm;


/**
 * Demonstrates basic concepts of CDI, this is a injectable component qualified as a "short form" date, e.g. 06/23/12
 * 
 * @author greg
 *
 */
@ShortForm
public class BasicDate implements DateFormatter { 

	@Override
	public String getDate()
	{
		Date currentDate = new Date();
		DateFormat dateFormatter;
 
		dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
		
		return dateFormatter.format(currentDate);
	}
	
}
