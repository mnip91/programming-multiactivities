/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2012 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.objectweb.proactive.core.component.componentcontroller.execution;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.proactive.core.component.Utils;
import org.objectweb.proactive.core.component.componentcontroller.AbstractPAComponentController;
import org.objectweb.proactive.core.component.factory.PAGenericFactory;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactory;


public class ExecutionControllerImpl extends AbstractPAComponentController implements ExecutionController {

	private static final long serialVersionUID = 1L;

	private PAGCMTypeFactory tf;
	private PAGenericFactory cf;

	// action name --> action
	private Map<String, Action> actions = new HashMap<String, Action>();
	
	@Override
	public boolean addAction(String name, Action action) {
		if (actions.containsKey(name)) return false;
		return actions.put(name, action) == null;
	}

	@Override
	public void removeAction(String name) {
		actions.remove(name);
	}

	@Override
	public void execute(String name) {
		checkFactories();
		try {
			actions.get(name).execute(this.hostComponent, tf, cf);
		} catch (NullPointerException npe) {
			(new Exception("Action \"" + name + "\" not found.")).printStackTrace();
		}
	}

	@Override
	public void execute(Action action) {
		checkFactories();
		action.execute(this.hostComponent, tf, cf);
	}

	private void checkFactories() {
		if (tf == null || cf == null) {
			try {
				Component boot = Utils.getBootstrapComponent();
				tf = Utils.getPAGCMTypeFactory(boot);
				cf = Utils.getPAGenericFactory(boot);
			} catch (InstantiationException | NoSuchInterfaceException e) {
				e.printStackTrace();
			}
		}
	}
}
