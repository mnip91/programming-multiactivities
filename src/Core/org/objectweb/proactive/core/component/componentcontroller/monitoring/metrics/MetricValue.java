package org.objectweb.proactive.core.component.componentcontroller.monitoring.metrics;

import java.io.Serializable;


public interface MetricValue extends Serializable {

	public Object getValue() throws WrongMetricValueException;

	public boolean isValid();
	public boolean isMulticast();

}
