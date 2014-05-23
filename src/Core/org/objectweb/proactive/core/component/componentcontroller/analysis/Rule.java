package org.objectweb.proactive.core.component.componentcontroller.analysis;

import java.io.Serializable;

import org.objectweb.proactive.core.component.componentcontroller.monitoring.MonitorController;

public interface Rule extends Serializable {

	public boolean isSatisfied(MonitorController monitor);

	public boolean shouldPrintAlarm();
	public String getAlarm();
	
}
