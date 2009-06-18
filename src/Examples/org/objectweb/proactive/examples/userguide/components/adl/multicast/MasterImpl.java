//@tutorial-start
/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2009 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version
 * 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.objectweb.proactive.examples.userguide.components.adl.multicast;

import java.util.List;

import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;


/**
 * @author The ProActive Team
 */
public class MasterImpl implements Runner, BindingController {
    public static String ITF_CLIENT_1 = "i1";
    public static String ITF_CLIENT_2 = "i2";
    private Itf1Multicast i1;
    private Itf2 i2;

    public void run(List<String> arg) {
        i1.compute(arg);
        i2.doNothing();
    }

    public void bindFc(String clientItfName, Object serverItf) throws NoSuchInterfaceException,
            IllegalBindingException, IllegalLifeCycleException {
        if (ITF_CLIENT_1.equals(clientItfName)) {
            i1 = (Itf1Multicast) serverItf;
        } else if (ITF_CLIENT_2.equals(clientItfName)) {
            i2 = (Itf2) serverItf;
        } else {
            throw new NoSuchInterfaceException(clientItfName);
        }
    }

    public String[] listFc() {
        return new String[] { ITF_CLIENT_1, ITF_CLIENT_2 };
    }

    public Object lookupFc(String clientItfName) throws NoSuchInterfaceException {
        if (ITF_CLIENT_1.equals(clientItfName)) {
            return i1;
        } else if (ITF_CLIENT_2.equals(clientItfName)) {
            return i2;
        } else {
            throw new NoSuchInterfaceException(clientItfName);
        }
    }

    public void unbindFc(String clientItfName) throws NoSuchInterfaceException, IllegalBindingException,
            IllegalLifeCycleException {
        if (ITF_CLIENT_1.equals(clientItfName)) {
            i1 = null;
        } else if (ITF_CLIENT_2.equals(clientItfName)) {
            i2 = null;
        } else {
            throw new NoSuchInterfaceException(clientItfName);
        }
    }

}
//@tutorial-end