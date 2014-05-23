package org.objectweb.proactive.core.component.componentcontroller.analysis;

public interface AnalysisController {

	public final static String ITF_NAME = "analysis-controller-nf";
	public final static String AC_CLIENT_ITF_NAME = "analysis-controller-client-nf";


	public void setDelay(long time);

	public void addRule(String name, Rule rule, String actionName);
	public void addRule(String name, Rule rule);
	
	public void removeRule(String name);

	public void analyze();

}
