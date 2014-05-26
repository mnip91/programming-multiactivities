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
package org.objectweb.proactive.core.component.componentcontroller.monitoring.metrics;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.proactive.core.component.componentcontroller.monitoring.MetricStore;
import org.objectweb.proactive.core.component.componentcontroller.monitoring.event.RemmosEventType;
import org.objectweb.proactive.core.component.componentcontroller.monitoring.records.RecordStore;

/**
 * Parent class for all metrics, which produce a value of type T.
 * @author cruz
 *
 * @param <T>
 */
public abstract class Metric<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The record source */
	protected RecordStore recordStore = null;

	/** The metrics source */
	protected MetricStore metricStore = null;
	
	// The listener (SLA Monitor) interested in this metric's updates
	private Set<MetricListener> metricListeners = new HashSet<MetricListener>();

	// Indicates if metric must notify its listener or not. If the notifications are disable, the metric will
	// ignore any listener and will not notify them on future changes.
	private boolean notifyListeners = true;

	// Set of subscribed events
	private Set<RemmosEventType> subscribedEvents = new HashSet<RemmosEventType>();

	// Indicates if metric is enable or not. A disabled metric will ignore any RemmosEvent subscription and
	// will not upgrade its value on new events.
	private boolean enable = true;


	public void setRecordSource(RecordStore rs) {
		recordStore = rs;
	}
	
	public void setMetricSource(MetricStore ms) {
		metricStore = ms;
	}
	

	/**
	 * Accepts a new metric listener to be notify after every calculation() method call.
	 * @param listener the listener to be added
	 */
	public void acceptMetricListener(MetricListener listener) {
		metricListeners.add(listener);
	}

	/**
	 * Remove a metric listener
	 * @param listener the listener to be removed
	 */
	public void removeMetricListener(MetricListener listener) {
		metricListeners.remove(listener);
	}

	/**
	 * Enables the notification to this metric listeners. If enabled, all the MetricListeners will
	 * be notified when the metric recalculates its value. The default is enable.
	 */
	public void enableNotifications() {
		notifyListeners = true;
	}

	/**
	 * Disable the notification to this metric listeners. If disabled, all the MetricListeners will
	 * be ignored, they will not be notified when the metric recalculates its value. The default is enable.
	 */
	public void disableNotifications() {
		notifyListeners = false;
	}

	/**
	 * Shows if this metric is notifying its metric listeners after a metric value  recalculation
	 * @return
	 */
	public boolean isNotificationEnable() {
		return notifyListeners;
	}

	public void subscribeTo(RemmosEventType ret) {
		subscribedEvents.add(ret);
	}

	public void unsubscribeFrom(RemmosEventType ret) {
		subscribedEvents.remove(ret);
	}

	public boolean isSubscribedTo(RemmosEventType ret) {
		return subscribedEvents.contains(ret);
	}

	/**
	 * Enables the RemmosEvent subscription. If enabled, the {@link #calculate()} method will be call after every
	 * notification of a subscribed RemmosEvent. Enabled by default. 
	 */
	public void enableEventSubscription() {
		this.enable = true;
	}
	
	/**
	 * Disables the RemmosEvent subscription. If disables, the metric will ignore any RemmosEvent subscription and
	 * the method {@link #calculate()} will not be called after event notifications.
	 */
	public void disableEventSubsctiption() {
		this.enable = false;
	}

	/**
	 * Shows if this metric is being recalculated after every notification of a subscribed RemmosEvent.
	 * @return
	 */
	public boolean isEventSubscriptionEnable() {
		return this.enable;
	}

	/**
	 * Calculates the value of the metric, using the parameters provided
	 * @return
	 */
	public abstract T calculate();

	/**
	 * Returns the current value of the metric, without any recalculation
	 * @return
	 */
	public abstract T getValue();

	/**
	 * Sets arbitrarily the value of the metric
	 * @param value
	 */
	public abstract void setValue(T value);

}
