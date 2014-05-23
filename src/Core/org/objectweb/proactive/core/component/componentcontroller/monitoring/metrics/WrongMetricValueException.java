package org.objectweb.proactive.core.component.componentcontroller.monitoring.metrics;

public class WrongMetricValueException extends Exception {

	private static final long serialVersionUID = 1L;

	public WrongMetricValueException(String msg) {
		super(msg);
	}
	
    public WrongMetricValueException(Throwable cause) {
        super(cause);
    }
    
    public WrongMetricValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
