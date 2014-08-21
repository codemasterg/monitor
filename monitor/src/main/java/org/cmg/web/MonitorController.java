package org.cmg.web;

import java.util.Map;

import javax.inject.Inject;
import javax.print.attribute.standard.DateTimeAtCompleted;

import org.cmg.data.MonitorData;
import org.cmg.service.ControlAction;
import org.cmg.service.MonitorService;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Single controller that serves up home page as well as other pages for scheduling, on/off control, and log viewing
 * @author greg
 *
 */
@Controller()
public class MonitorController {
	
	@Inject 
	private MonitorService monitorService;
	
	/**
	 * Populate monitor data bean and return home page.
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String serveHomePage(Map<String,Object> model)
	{
		
		
		MonitorData historyData = monitorService.getMonitorData();
		
		model.put("monitorData", historyData);
		return "index";
	}
	
	@RequestMapping(value="/action", method=RequestMethod.POST)
	public String performControlAction(@RequestParam("action") String actionString)
	{
		if (StringUtils.isEmpty(actionString) == false)
		{
			ControlAction action = ControlAction.getEnumFromAction(actionString);
			monitorService.performMonitorControlAction(action);
		}
		
		return "index";
	}

}
