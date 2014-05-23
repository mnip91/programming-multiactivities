package org.objectweb.proactive.core.component.componentcontroller.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.proactive.core.component.componentcontroller.AbstractPAComponentController;
import org.objectweb.proactive.core.component.componentcontroller.execution.ExecutionController;
import org.objectweb.proactive.core.component.componentcontroller.monitoring.MonitorController;
import org.objectweb.proactive.core.component.componentcontroller.remmos.Remmos;


public class AnalysisControllerImpl extends AbstractPAComponentController
		implements AnalysisController, BindingController {

	private static final long serialVersionUID = 1L;

	private AnalysisController mySelf;
	private ExecutionController executionController;
	private MonitorController monitorController;
	
	// delay between each rules analysis
	private long delay = 30000;
	
	// rule name --> rule
	private Map<String, Rule> rules = new HashMap<String, Rule>();
	
	// rule name --> action name
	private Map<String, String> actions = new HashMap<String, String>();

	
	@Override
	public void setDelay(long time) {
		delay = time;
	}

	@Override
	public void addRule(String name, Rule rule, String actionName) {
		rules.put(name, rule);
		actions.put(name, actionName);
	}
	
	@Override
	public void addRule(String name, Rule rule) {
		this.addRule(name, rule, null);
	}

	@Override
	public void removeRule(String name) {
		rules.remove(name);
		actions.remove(name);
	}

	@Override
	public void analyze() {

		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) { e.printStackTrace(); }

		for(String ruleName : rules.keySet()) {
			Rule rule = rules.get(ruleName);
			if(!rule.isSatisfied(monitorController)) {
				if (rule.shouldPrintAlarm()) {
					System.out.println(rule.getAlarm());
				}
				String actionName = actions.get(ruleName);
				if (actionName != null) {
					executionController.execute(actionName);
				}
			}
		}

		mySelf.analyze();
	}


	@Override
	public void bindFc(String name, Object itf) throws NoSuchInterfaceException {
		if (name.equals(AnalysisController.AC_CLIENT_ITF_NAME)) {
			mySelf = (AnalysisController) itf;
		} else if (name.equals(ExecutionController.ITF_NAME)) {
			executionController = (ExecutionController) itf;
		} else if (name.equals(MonitorController.ITF_NAME)) {
			monitorController = (MonitorController) itf;
		} else {
			throw new NoSuchInterfaceException("AnalysisController:" + name);
		}
	}

	@Override
	public String[] listFc() {
		return new String[] { AC_CLIENT_ITF_NAME, MonitorController.ITF_NAME, ExecutionController.ITF_NAME };
	}

	@Override
	public Object lookupFc(String name) throws NoSuchInterfaceException {
		if(name.equals(MonitorController.ITF_NAME)) {
			return monitorController;
		} else if (name.equals(ExecutionController.ITF_NAME)) {
			return executionController;
		} else if (name.equals(AC_CLIENT_ITF_NAME)) {
			return mySelf;
		}
		throw new NoSuchInterfaceException("AnalysisController:" + name);
	}

	@Override
	public void unbindFc(String name) throws NoSuchInterfaceException {
		if(name.equals(MonitorController.ITF_NAME)) {
			monitorController = null;
		} else if (name.equals(ExecutionController.ITF_NAME)) {
			executionController = null;
		} else if (name.equals(AC_CLIENT_ITF_NAME)) {
			mySelf = null;
		} else {
			throw new NoSuchInterfaceException("AnalysisController:" + name);
		}
	}

}
