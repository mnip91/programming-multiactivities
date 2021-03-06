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
package org.objectweb.proactive.extensions.gcmdeployment.GCMDeployment.bridge;

import java.io.Serializable;
import java.util.List;

import org.objectweb.proactive.extensions.gcmdeployment.GCMApplication.GCMApplicationInternal;
import org.objectweb.proactive.extensions.gcmdeployment.GCMApplication.commandbuilder.CommandBuilder;
import org.objectweb.proactive.extensions.gcmdeployment.GCMDeployment.group.Group;
import org.objectweb.proactive.extensions.gcmdeployment.GCMDeployment.hostinfo.HostInfo;


/**
 *
 * A Bridge can have Bridges, Groups and HostInfo as children
 *
 */
public interface Bridge extends Serializable {

    /**
     * Set environment variables for this cluster
     * @param env environment variables
     */
    public void setEnvironment(String env);

    /**
     * Set the destination host
     * @param hostname destination host as FQDN or IP address
     */
    public void setHostname(String hostname);

    /**
     * Username to be used on the destination host
     * @param username an username
     */
    public void setUsername(String username);

    /**
     * Set the command path to override the default one
     * @param commandPath path to the command
     */
    public void setCommandPath(String commandPath);

    public String getId();

    /**
     * Add a bridge to children elements
     *
     * @param bridge The child bridge
     */
    public void addBridge(Bridge bridge);

    /**
     * Returns all children of type Bridge
     *
     * @return
     */
    public List<Bridge> getBridges();

    /**
     * Add a group to children elements
     *
     * @param group The child group
     */
    public void addGroup(Group group);

    /**
     * Returns all children of type Group
     *
     * @return
     */
    public List<Group> getGroups();

    /**
     * Set the HostInfo
     *
     * @param hostInfo
     */
    public void setHostInfo(HostInfo hostInfo);

    /**
     * Get the HostInfo
     *
     * @return if set the HostInfo is returned. null is returned otherwise
     */
    public HostInfo getHostInfo();

    /**
     * Check that this bridge is in a consistent state and is ready to be
     * used.
     *
     * @throws IllegalStateException thrown if anything is wrong
     */
    public void check() throws IllegalStateException;

    public List<String> buildCommands(CommandBuilder commandBuilder, GCMApplicationInternal gcma);
}
