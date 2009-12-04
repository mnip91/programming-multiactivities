/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2009 INRIA/University of 
 * 						   Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
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
 * If needed, contact us to obtain a release under GPL Version 2. 
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.objectweb.proactive.examples.webservices.helloWorld;

import org.objectweb.proactive.extensions.webservices.client.AbstractClientFactory;
import org.objectweb.proactive.extensions.webservices.client.Client;
import org.objectweb.proactive.extensions.webservices.client.ClientFactory;
import org.objectweb.proactive.extensions.webservices.exceptions.UnknownFrameWorkException;
import org.objectweb.proactive.extensions.webservices.exceptions.WebServicesException;


public class HelloWorldClient {

    /**
     * @param args
     * @throws UnknownFrameWorkException
     * @throws Exception
     */
    public static void main(String[] args) throws UnknownFrameWorkException {

        String url = "";
        String wsFrameWork = "";
        if (args.length == 1) {
            url = "http://localhost:8080/";
            wsFrameWork = args[0];
        } else if (args.length == 2) {
            url = args[0];
            wsFrameWork = args[1];
        } else {
            System.out.println("Wrong number of arguments:");
            System.out.println("Usage: java HelloWorldClient [url] wsFrameWork");
            System.out.println("where wsFrameWork should be either 'axis2' or 'cxf'");
            return;
        }

        ClientFactory cf = AbstractClientFactory.getClientFactory(wsFrameWork);
        Client client = null;
        try {
            client = cf.getClient(url, "HelloWorld", HelloWorld.class);
        } catch (WebServicesException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            System.exit(-1);
        }

        Object[] res;
        Object[] arg = new Object[] {};
        try {

            res = client.call("sayText", arg, String.class);
            System.out.println(res[0]);

            client.oneWayCall("putTextToSay", new Object[] { "Hello ProActive Team" });

            res = client.call("sayText", arg, String.class);
            System.out.println(res[0]);

            client.oneWayCall("putHelloWorld", arg);

            res = client.call("sayText", arg, String.class);
            System.out.println(res[0]);

            res = client.call("putTextToSayAndConfirm", new Object[] { "Good Bye ProActive Team" },
                    String.class);
            System.out.println(res[0]);

            res = client.call("sayText", new Object[] { "Good Bye ProActive Team" }, String.class);
            System.out.println(res[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}