/* 
* ################################################################
* 
* ProActive: The Java(TM) library for Parallel, Distributed, 
*            Concurrent computing with Security and Mobility
* 
* Copyright (C) 1997-2002 INRIA/University of Nice-Sophia Antipolis
* Contact: proactive-support@inria.fr
* 
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or any later version.
*  
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
* USA
*  
*  Initial developer(s):               The ProActive Team
*                        http://www.inria.fr/oasis/ProActive/contacts.html
*  Contributor(s): 
* 
* ################################################################
*/
package org.objectweb.proactive.core.body.jini;

import org.objectweb.proactive.core.UniqueID;
import org.objectweb.proactive.core.body.UniversalBody;
import org.objectweb.proactive.core.body.reply.Reply;
import org.objectweb.proactive.core.body.request.Request;

/**
 * An object implementing this interface provides the minimum service a body offers
 * remotely. This interface is the glue with the JINI Remote interface that allow the
 * body to be accessed remotely.
 *
 * @author  ProActive Team
 * @version 1.0,  2001/10/23
 * @since   ProActive 0.9
 * @see java.rmi.Remote
 * @see org.objectweb.proactive.core.body.UniversalBody
 */
public interface JiniBody extends java.rmi.Remote {

	/**
	 * Receives a request for later processing. The call to this method is non blocking
	 * unless the body cannot temporary receive the request.
	 * @param request the request to process
	 * @exception java.io.IOException if the request cannot be accepted
	 */
	public void receiveRequest(Request r) throws java.io.IOException;

	/**
	 * Receives a reply in response to a former request.
	 * @param reply the reply received
	 * @exception java.io.IOException if the reply cannot be accepted
	 */
	public void receiveReply(Reply r) throws java.io.IOException;

	/**
	 * Returns the url of the node this body is associated to
	 * The url of the node can change if the active object migrates
	 * @return the url of the node this body is associated to
	 * @exception java.rmi.RemoteException if an exception occured during the jini communication
	 */
	public String getNodeURL() throws java.rmi.RemoteException;

	/**
	 * Returns the UniqueID of this body
	 * This identifier is unique accross all JVMs
	 * @return the UniqueID of this body
	 * @exception java.rmi.RemoteException if an exception occured during the jini communication
	 */
	public UniqueID getID() throws java.rmi.RemoteException;

	/**
	 * Signals to this body that the body identified by id is now to a new
	 * jini location. The body given in parameter is a new stub pointing
	 * to this new location. This call is a way for a body to signal to his 
	 * peer that it has migrated to a new location
	 * @param id the id of the body
	 * @param body the stub to the new location
	 * @exception java.io.IOException if an exception occured during the jini communication
	 */
	public void updateLocation(UniqueID id, UniversalBody body) throws java.io.IOException;

	
	/**
	 *  Enables automatic continuation mechanism for this body
	 */
	public void enableAC() throws java.io.IOException;

 /**
	*  Disables automatic continuation mechanism for this body
	*/
	public void disableAC() throws java.io.IOException;

}
