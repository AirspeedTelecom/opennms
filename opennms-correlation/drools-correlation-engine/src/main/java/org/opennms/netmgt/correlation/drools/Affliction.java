/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2007 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * Modifications:
 * 
 * Created: January, 31 2007
 *
 * Copyright (C) 2007 The OpenNMS Group, Inc.  All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 *      OpenNMS Licensing       <license@opennms.org>
 *      http://www.opennms.org/
 *      http://www.opennms.com/
 */

package org.opennms.netmgt.correlation.drools;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Affliction class.</p>
 *
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 * @version $Id: $
 */
public class Affliction {
    Long m_nodeid;
    String m_ipAddr;
    String m_svcName;
    
    public static enum Type {
        UNDECIDED,
        ISOLATED,
        WIDE_SPREAD
    }
    
    List<Integer> m_reporters = new ArrayList<Integer>();
    private Type m_type  = Type.UNDECIDED;
    
    /**
     * <p>Constructor for Affliction.</p>
     *
     * @param nodeId a {@link java.lang.Long} object.
     * @param ipAddr a {@link java.lang.String} object.
     * @param svcName a {@link java.lang.String} object.
     * @param reporter a {@link java.lang.Integer} object.
     */
    public Affliction(Long nodeId, String ipAddr, String svcName, Integer reporter) {
        m_nodeid = nodeId;
        m_ipAddr = ipAddr;
        m_svcName = svcName;
        m_reporters.add(reporter);
    }

    /**
     * <p>getIpAddr</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getIpAddr() {
        return m_ipAddr;
    }

    /**
     * <p>setIpAddr</p>
     *
     * @param ipAddr a {@link java.lang.String} object.
     */
    public void setIpAddr(String ipAddr) {
        m_ipAddr = ipAddr;
    }

    /**
     * <p>getNodeid</p>
     *
     * @return a {@link java.lang.Long} object.
     */
    public Long getNodeid() {
        return m_nodeid;
    }

    /**
     * <p>setNodeid</p>
     *
     * @param nodeid a {@link java.lang.Long} object.
     */
    public void setNodeid(Long nodeid) {
        m_nodeid = nodeid;
    }

    /**
     * <p>getReporters</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Integer> getReporters() {
        return m_reporters;
    }

    /**
     * <p>setReporters</p>
     *
     * @param reporters a {@link java.util.List} object.
     */
    public void setReporters(List<Integer> reporters) {
        m_reporters = reporters;
    }

    /**
     * <p>getSvcName</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSvcName() {
        return m_svcName;
    }

    /**
     * <p>setSvcName</p>
     *
     * @param svcName a {@link java.lang.String} object.
     */
    public void setSvcName(String svcName) {
        m_svcName = svcName;
    }
    
    /**
     * <p>getReporterCount</p>
     *
     * @return a int.
     */
    public int getReporterCount() {
        return m_reporters.size();
    }
    
    /**
     * <p>addReporter</p>
     *
     * @param reporter a {@link java.lang.Integer} object.
     */
    public void addReporter(Integer reporter) {
        m_reporters.add( reporter );
    }
    
    /**
     * <p>removeReporter</p>
     *
     * @param reporter a {@link java.lang.Integer} object.
     */
    public void removeReporter(Integer reporter) {
        m_reporters.remove(reporter);
    }
    
    /**
     * <p>getType</p>
     *
     * @return a {@link org.opennms.netmgt.correlation.drools.Affliction.Type} object.
     */
    public Type getType() {
        return m_type;
    }
    
    /**
     * <p>setType</p>
     *
     * @param type a {@link org.opennms.netmgt.correlation.drools.Affliction.Type} object.
     */
    public void setType(Type type) {
        m_type = type;
    }
    
}
