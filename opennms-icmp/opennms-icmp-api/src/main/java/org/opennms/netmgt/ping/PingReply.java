//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2002-2003 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified 
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
//
// Modifications:
//
// 2003 Mar 05: Cleaned up some ICMP related code.
// 2002 Nov 13: Added response time stats for ICMP requests.
// 
// Original code base Copyright (C) 1999-2001 Oculan Corp.  All rights reserved.
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.                                                            
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//       
// For more information contact: 
//      OpenNMS Licensing       <license@opennms.org>
//      http://www.opennms.org/
//      http://www.opennms.com/
//
// Tab Size = 8
//
// Reply.java,v 1.1.1.1 2001/11/11 17:34:37 ben Exp

package org.opennms.netmgt.ping;

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.opennms.protocols.icmp.ICMPEchoPacket;
import org.opennms.protocols.rt.ResponseWithId;

/**
 * <p>
 * This class is use to encapsulate an ICMP reply that conforms to the
 * {@link ICMPEchoPacket packet}class. The reply must be of type ICMP Echo Reply and be
 * the correct length.
 * </p>
 *
 * <p>
 * When constructed by the <code>create</code> method the returned reply
 * encapsulates the sender's address and the received packet as final,
 * non-mutable values for the instance.
 * </p>
 *
 * @author <a href="mailto:weave@oculan.com">Brian Weaver </a>
 * @author <a href="mailto:sowmya@opennms.org">Sowmya </a>
 * @author <a href="http://www.opennms.org">OpenNMS </a>
 * @author <a href="mailto:weave@oculan.com">Brian Weaver </a>
 * @author <a href="mailto:sowmya@opennms.org">Sowmya </a>
 * @author <a href="http://www.opennms.org">OpenNMS </a>
 * @author <a href="mailto:weave@oculan.com">Brian Weaver </a>
 * @author <a href="mailto:sowmya@opennms.org">Sowmya </a>
 * @author <a href="http://www.opennms.org">OpenNMS </a>
 * @version $Id: $
 */
public final class PingReply implements ResponseWithId<PingRequestId> {
    /**
     * The sender's address.
     */
    private final InetAddress m_address;

    /**
     * The received packet.
     */
    private final ICMPEchoPacket m_packet;

    /**
     * Constructs a new reply with the passed address and packet as the contents
     * of the reply.
     *
     * @param addr
     *            The address of the ICMP sender.
     * @param pkt
     *            The received packet.
     */
    public PingReply(InetAddress addr, ICMPEchoPacket pkt) {
        m_packet = pkt;
        m_address = addr;
    }

    /**
     * Returns true if the recovered packet is an echo reply.
     *
     * @return a boolean.
     */
    public boolean isEchoReply() {
        return m_packet.isEchoReply();
    }

    /**
     * Returns the identity of the packet.
     *
     * @return a short.
     */
    public final short getIdentity() {
        return m_packet.getIdentity();
    }

    /**
     * Returns the internet address of the host that sent the reply.
     *
     * @return a {@link java.net.InetAddress} object.
     */
    public final InetAddress getAddress() {
        return m_address;
    }

    /**
     * Returns the ICMP packet for the reply.
     *
     * @return a {@link org.opennms.protocols.icmp.ICMPEchoPacket} object.
     */
    public final ICMPEchoPacket getPacket() {
        return m_packet;
    }
    
    /**
     * <p>getRequestId</p>
     *
     * @return a {@link org.opennms.netmgt.ping.PingRequestId} object.
     */
    public PingRequestId getRequestId() {
        return new PingRequestId(this);
    }

    /**
     * <p>
     * Creates a new instance of the class using the passed datagram as the data
     * source. The address and ping packet are extracted from the datagram and
     * returned as a new instance of the class. In addition to extracting the
     * packet, the packet's received time is updated to the current time.
     * </p>
     *
     * <p>
     * If the received datagram is not an echo reply or an incorrect length then
     * an exception is generated to alert the caller.
     * </p>
     *
     * @param packet
     *            The packet with the ICMP datagram.
     * @throws java.lang.IllegalArgumentException
     *             Throw if the datagram is not the correct length or type.
     * @throws java.lang.IndexOutOfBoundsException
     *             Thrown if the datagram does not contain sufficent data.
     * @return a {@link org.opennms.netmgt.ping.PingReply} object.
     */
    public static PingReply create(DatagramPacket packet) {
        // Check the packet length
        //
        if (packet.getData().length != ICMPEchoPacket.getNetworkSize()) {
            throw new IllegalArgumentException("The packet is not the correct network size");
        }

        // Construct a new packet
        //
        ICMPEchoPacket pkt = new ICMPEchoPacket(packet.getData());
        if (pkt.getReceivedTime() == 0)
            pkt.setReceivedTime();

        // Construct and return the new reply
        //
        return new PingReply(packet.getAddress(), pkt);
    }
}
