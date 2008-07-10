/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2007 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@objectweb.org
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
 */
package org.objectweb.proactive.examples.masterworker;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;


public abstract class AbstractExample {
    protected static Options command_options = new Options();
    protected static URL descriptor_url = null;
    protected static String vn_name = null;
    protected static CommandLine cmd = null;
    protected static String master_vn_name = null;
    public static final String DEFAULT_DESCRIPTOR = "MWApplication.xml";

    static {
        command_options.addOption("d", true, "descriptor in use");
        command_options.addOption("n", true, "virtual node name");
        command_options.addOption("m", true, "master virtual node name");
    }

    /**
     * Returns the url of the descriptor which defines the workers
     * @return descriptor url
     */
    public URL getDescriptor_url() {
        return descriptor_url;
    }

    /**
     * Sets the url of the descriptors which defines the workers
     * @param descriptor_url
     */
    public void setDescriptor_url(URL descriptor_url) {
        this.descriptor_url = descriptor_url;
    }

    /**
     * Returns the virtual node name of the workers
     * @return virtual node name
     */
    public String getVn_name() {
        return vn_name;
    }

    /**
     * Sets the virtual node name of the workers
     * @param vn_name virtual node name
     */
    public void setVn_name(String vn_name) {
        this.vn_name = vn_name;
    }

    /**
     * Initializing the example with command line arguments
     * @param args command line arguments
     * @throws MalformedURLException
     */
    protected static void init(String[] args) throws MalformedURLException {
        CommandLineParser parser = new PosixParser();

        try {
            cmd = parser.parse(command_options, args);
        } catch (ParseException e) {
            System.err.println("Parsing failed, reason, " + e.getMessage());
            System.exit(1);
        }

        // Finding current os
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            System.setProperty("os", "windows");
        } else {
            System.setProperty("os", "unix");
        }

        // get descriptor option value
        String descPath = cmd.getOptionValue("d");

        if (descPath == null) {
            descriptor_url = AbstractExample.class.getResource(DEFAULT_DESCRIPTOR);
            if (descriptor_url == null) {
                System.err.println("Couldn't find internal ressource: " + DEFAULT_DESCRIPTOR);
                System.exit(1);
            }
        } else {
            // check provided descriptor
            File descriptorFile = new File(descPath);
            if (!descriptorFile.exists()) {
                System.err.println("" + descriptorFile + " does not exist");
                System.exit(1);
            } else if (!descriptorFile.canRead()) {
                System.err.println("" + descriptorFile + " can't be read");
                System.exit(1);
            } else if (!descriptorFile.isFile()) {
                System.err.println("" + descriptorFile + " is not a regular file");
                System.exit(1);
            }
            descriptor_url = descriptorFile.toURI().toURL();
        }

        // get vn option value
        vn_name = cmd.getOptionValue("n");

        master_vn_name = cmd.getOptionValue("m");
    }

    /**
     * Register a shutdown hook on this example which will terminate the master
     */
    protected static void registerShutdownHook(Runnable shHook) {
        Runtime.getRuntime().addShutdownHook(new Thread(shHook));
    }
}
