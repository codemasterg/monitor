package org.cmg.web;

import java.util.Map;

import javax.print.attribute.standard.DateTimeAtCompleted;

import org.cmg.data.MonitorData;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Single controller that serves up home page as well as other pages for scheduling, on/off control, and log viewing
 * @author greg
 *
 */
@Controller()
public class MonitorController {
	
	/**
	 * Populate monitor data bean and return home page.
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String serveHomePage(Map<String,Object> model)
	{
		// TODO replace dummies with service call
		
		MonitorData historyData = new MonitorData();
		DateTime now = new DateTime();
		DateTime startTime = now.minusMonths(2);
		
		 DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM, yyyy HH:mm:ss.SSS");  // 2014, August 20 22:08:13.123
		 String str = fmt.print(now);
		 
		historyData.setMostRecentDetectionDate(str);
		historyData.setNumDetection(3);
		historyData.setDaysUp(Days.daysBetween(startTime, now).getDays());
		
		model.put("monitorData", historyData);
		return "index";
	}

}
