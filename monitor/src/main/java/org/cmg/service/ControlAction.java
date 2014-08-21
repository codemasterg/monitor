package org.cmg.service;

public enum ControlAction {
	ENABLE("Enable"),
	DISABLE("Disable");
	
	private String action;
	
	private ControlAction(String action)
	{
		this.action = action;
	}
	
	public static ControlAction getEnumFromAction(String action)
	{
		for(ControlAction enumVal : ControlAction.values())
		{
			if (enumVal.action.equalsIgnoreCase(action))
			{
				return enumVal;
			}
		}
		return null;
	}

}
