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
package org.objectweb.proactive.examples.components.c3d;

import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.LifeCycleController;
import org.objectweb.proactive.Body;
import org.objectweb.proactive.Service;
import org.objectweb.proactive.core.ProActiveException;
import org.objectweb.proactive.core.component.Fractive;
import org.objectweb.proactive.core.component.body.ComponentBody;
import org.objectweb.proactive.core.component.body.ComponentRunActive;
import org.objectweb.proactive.core.component.body.NFRequestFilterImpl;
import org.objectweb.proactive.core.util.log.Loggers;
import org.objectweb.proactive.core.util.log.ProActiveLogger;
import org.objectweb.proactive.examples.c3d.C3DDispatcher;
import org.objectweb.proactive.examples.c3d.Dispatcher;
import org.objectweb.proactive.examples.c3d.DispatcherLogic;
import org.objectweb.proactive.examples.c3d.RenderingEngine;


/**
 * The Component wrapper class for our Dispatcher.
 * Most interesting bit of this code is the runComponentActivity redefinition.
 */
public class DispatcherImpl extends C3DDispatcher implements Dispatcher, DispatcherLogic,
        DispatcherAttributes, BindingController, ComponentRunActive {
    static Logger logger = ProActiveLogger.getLogger(Loggers.EXAMPLES);

    // Engine component bindings
    private Map<String, Object> engines = new Hashtable<String, Object>();

    /** The no-argument Constructor as commanded by ProActive; otherwise unused */
    public DispatcherImpl() {
    }

    // binding control

    /** Returns the name of all the interfaces that have been bound.*/
    public String[] listFc() {
        return engines.keySet().toArray(new String[engines.size()]);
    }

    /** Find a possible bound on this Component.
     * @return the component that is linked to this through the binding labeled interfaceName */
    public Object lookupFc(final String interfaceName) {
        if (interfaceName.startsWith("dispatcher2engine")) {
            return engines.get(interfaceName);
        }

        return null;
    }

    /** Attach a component to this dispatcher, expecting the name "dispatcher2engine" */
    public void bindFc(final String interfaceName, final Object serverInterface) {
        if (interfaceName.startsWith("dispatcher2engine")) {
            this.engines.put(interfaceName, serverInterface);

            String name = interfaceName.substring("dispatcher2".length()) + "@" +
                Integer.toHexString(serverInterface.hashCode());
            addEngine((RenderingEngine) serverInterface, name);
            turnOnEngine(name);
        }
    }

    /** Detach a component from this dispatcher, expecting the interface name "dispatcher2engine" */
    public void unbindFc(final String interfaceName) {
        if (interfaceName.startsWith("dispatcher2engine")) {
            Object engine = this.engines.remove(interfaceName);
            removeEngine((RenderingEngine) engine);
        }
    }

    /** This is the activity of the component.
     * the activity of the component has been initialized and started, now
     * what we have to do is to manage the life cycle, i.e. start and stop the activity
     * that can be redefined on the reified object.
     * In this redefinition, initActivity and endActivity of the Active Object
     * are only called once. The default behavior is to call them each time the component
     * is stopped, and restarted. */
    public void runComponentActivity(Body body) {
        boolean initActivityHasBeenRun = false;

        try {
            Service componentService = new Service(body);
            NFRequestFilterImpl nfRequestFilter = new NFRequestFilterImpl();

            while (body.isActive()) {
                ComponentBody componentBody = (ComponentBody) body;

                // treat non functional requests before component is started
                while (LifeCycleController.STOPPED.equals(GCM.getGCMLifeCycleController(
                        componentBody.getPAComponentImpl()).getFcState())) {
                    componentService.blockingServeOldest(nfRequestFilter);
                }

                // init object Activity, which is never called more than once!
                if (!initActivityHasBeenRun) {
                    initActivity(body);

                    try {
                        //@snippet-start C3D_Components_2
                        Fractive.registerByName(Fractive.getComponentRepresentativeOnThis(), "Dispatcher");
                        //@snippet-end C3D_Components_2
                    } catch (ProActiveException e) {
                        System.err.println("HEY, couldn't register dispatcher");
                        e.printStackTrace();
                    }

                    initActivityHasBeenRun = true;
                }

                // this is a marker saying we're in functional activity.
                ((ComponentBody) body).startingFunctionalActivity();
                // while active object activity, serve requests
                runActivity(body);
                ((ComponentBody) body).finishedFunctionalActivity();
            }

            // destroying active object activity, done only once!
            // there is no need to call this, as there is no endActivity defined
            //            if (initActivityHasBeenRun) {
            //          endActivity(body);
            //          }
        } catch (NoSuchInterfaceException e) {
            logger
                    .error("could not retreive an interface, probably the life cycle controller of this component; terminating the component. Error message is : " +
                        e.getMessage());
        }
    }

    // attributes control (methods of DispatcherAttributes)
    public int getLastUserId() {
        return this.lastUserID;
    }
}
