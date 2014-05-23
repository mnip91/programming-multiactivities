package org.objectweb.proactive.core.component.componentcontroller.execution;


public interface ExecutionController {

	public static final String ITF_NAME = "execution-controller-nf";

	public boolean addAction(String name, Action action);
	public void removeAction(String name);

	public void execute(String actionName);
	public void execute(Action action);
}
