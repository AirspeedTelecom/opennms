/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.enlinkd;

import java.util.Date;
import java.util.List;

import org.opennms.netmgt.model.topology.BridgeDot1dTpFdbLink;
import org.opennms.netmgt.model.topology.BridgeDot1qTpFdbLink;
import org.opennms.netmgt.model.topology.BridgeStpLink;
import org.opennms.netmgt.model.topology.CdpLink;
import org.opennms.netmgt.model.topology.LldpLink;
import org.opennms.netmgt.model.topology.MacAddrEndPoint;
import org.opennms.netmgt.model.topology.OspfLink;
import org.opennms.netmgt.model.topology.PseudoBridgeLink;
import org.opennms.netmgt.model.topology.PseudoMacLink;

/**
 * <p>QueryManager interface.</p>
 *
 * @author antonio
 * @version $Id: $
 */
public interface EnhancedLinkdService {

    /**
     * <p>getSnmpNodeList</p>
     *
     * @return a {@link java.util.List} object.
     * @throws java.sql.SQLException if any.
     */
    List<LinkableNode> getSnmpNodeList();

    /**
     * <p>getSnmpNode</p>
     *
     * @param nodeid a int.
     * @return a {@link org.opennms.netmgt.enlinkd.LinkableNode} object.
     * @throws java.sql.SQLException if any.
     */
    LinkableNode getSnmpNode(int nodeid);

    /**
     * <p>reconcile</p>
     * <p>This reconcile topology
     *    with opennms, remove any
     *    reference to deleted
     *    nodeid into topology objects
     * </p> 
     */
    void reconcile();
        
    /**
     * <p>reconcile</p>
     *
     * @param nodeid a int.
     * 
     * <p>Remove any reference in topology
     *    for nodeid
     * </p>   
     *     
     */
    void reconcile(int nodeid);
    
	void reconcileLldp(int nodeId, Date now);

	void reconcileCdp(int nodeId, Date now);

	void reconcileOspf(int nodeId, Date now);

	void reconcileIpNetToMedia(int nodeId, Date now);

	void reconcileBridge(int nodeId, Date now);

	void store(LldpLink link);

	void store(CdpLink link);

	void store(OspfLink link);

	void store(MacAddrEndPoint macep);

	void store(BridgeStpLink link);

	void store(BridgeDot1dTpFdbLink link);

	void store(BridgeDot1qTpFdbLink link);

	void store(PseudoBridgeLink link);

	void store(PseudoMacLink pseudoMacLink);
	
	boolean ready();

}
