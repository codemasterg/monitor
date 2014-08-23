package org.cmg.web;

import java.util.List;
import java.util.logging.Level;

import javax.inject.Inject;

import org.cmg.data.MonitorData;
import org.cmg.service.ControlAction;
import org.cmg.service.MonitorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
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
	public String serveHomePage(Model model)
	{
		
		
		MonitorData historyData = monitorService.getMonitorData();
		
		model.addAttribute("monitorData", historyData);
		return "index";
	}
	
	/**
	 * Populate monitor data bean and return home page.
	 */
	@RequestMapping(value="/status", method=RequestMethod.GET)
	public String serveControlPage(Model model)
	{
		
		
		MonitorData historyData = monitorService.getMonitorData();
		
		model.addAttribute("monitorData", historyData);
		return "control";
	}
	
	/**
	 * Execute requested control action (e.g enable / disable notifications)
	 * 
	 * @param actionString see ControlAtion enum for allowable action values
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/action", method=RequestMethod.POST)
	public String performControlAction(@RequestParam("action") String actionString) throws Exception
	{
		if (StringUtils.isEmpty(actionString) == false)
		{
			ControlAction action = ControlAction.getEnumFromAction(actionString);
			monitorService.performMonitorControlAction(action);
		}
		
		return "index";
	}

	/**
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/log", method=RequestMethod.GET)
	public String loadLogRecords(Model model)
	{
	
		List<String> logRecs = monitorService.getLogRecords(Level.ALL);
		model.addAttribute("logRecord", logRecs);
		
		return "log";
	}
}
