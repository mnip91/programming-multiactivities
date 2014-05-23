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
package org.objectweb.proactive.core.component.componentcontroller.monitoring;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.proactive.core.ProActiveRuntimeException;
import org.objectweb.proactive.core.component.componentcontroller.monitoring.metrics.Metric;
import org.objectweb.proactive.core.component.componentcontroller.monitoring.metrics.MetricValue;
import org.objectweb.proactive.core.component.control.MethodStatistics;
import org.objectweb.proactive.core.component.exceptions.NoSuchComponentException;

public interface MonitorController  {

	public static final String ITF_NAME = "monitor-controller-nf";
	
	//-------------------------------------------------------------------------
	// GCM Monitoring API
	
	void startGCMMonitoring();
	void stopGCMMonitoring();
	void resetGCMMonitoring();
	Boolean isGCMMonitoringStarted();
	Map<String, Object> getAllGCMStatistics();
	//MethodStatistics getGCMStatistics(String itfName, String methodName, Class<?>[] parametersTypes) throws ProActiveRuntimeException;
	
	//-------------------------------------------------------------------------
	// Adaptation for the GCM Monitoring API
	
	void startMonitoring();
	void stopMonitoring();
	void resetMonitoring();
	Boolean isMonitoringStarted();
	public Map<String, MethodStatistics> getAllStatistics();
	public MethodStatistics getStatistics(String itfName, String methodName) throws ProActiveRuntimeException;
	//public MethodStatistics getStatistics(String itfName, String methodName, Class<?>[] parametersTypes) throws ProActiveRuntimeException;
	
    //-------------------------------------------------------------------------
    // Extensions for the Monitoring Framework
    //
    
    /**
     * Get the list of all requests that have been entered this component
     * 
     */
    List<ComponentRequestID> getListOfIncomingRequestIDs();
    List<ComponentRequestID> getListOfOutgoingRequestIDs();
    
    /** 
     * Get the path followed by an specific request
     * 
     * @param id
     * @return
     */
    RequestPath getPathForID(ComponentRequestID id);
    RequestPath getPathForID(ComponentRequestID id, ComponentRequestID rootID, Set<String> visited);
    
    /**
     * Same from above, but with statistical information attached
     * 
     * @param id
     * @return
     */
    RequestPath getPathStatisticsForId(ComponentRequestID id);
    
    /**
     * Get the list of entries in the Incoming Request Log
     * @return
     */
    Map<ComponentRequestID, IncomingRequestRecord> getIncomingRequestLog();
    
    /**
     * Get the list of entries in the Outgoing Request Log
     * @return
     */
    Map<ComponentRequestID, OutgoingRequestRecord> getOutgoingRequestLog();
    
    List<String> getNotificationsReceived(); 
    
    String getMonitoredComponentName();
    
    /**
     * Add a metric on this monitor.
     * @param name		the name of the metric
     * @param metric	the metric
     */
    void addMetric(String name, Metric<?> metric);

    /**
     * Return the metric value of the named metric
     * @param name	the name of the metric
     * @return		an object if the metric was founded, null otherwise
     */
    MetricValue getMetricValue(String name);   
    void setMetricValue(String name, Object value);

    MetricValue runMetric(String name);
    //Object runMetric(String name, Object[] params);
    
    List<String> getMetricList();
	
    // REMOTE METRICS API

    MetricValue getMetricList(String itfPath);
   
    MetricValue getMetricValue(String name, String itfPath);
    void setMetricValue(String name, Object value, String itfPath);
    
    MetricValue runMetric(String name, String itfPath);
    // Object runMetric(String name, Object[] params, String itfPath);
    
}
