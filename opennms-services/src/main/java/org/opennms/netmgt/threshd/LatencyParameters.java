//
// This file is part of the OpenNMS(R) Application.
//
// OpenNMS(R) is Copyright (C) 2006 The OpenNMS Group, Inc.  All rights reserved.
// OpenNMS(R) is a derivative work, containing both original code, included code and modified
// code that was published under the GNU General Public License. Copyrights for modified
// and included code are below.
//
// OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
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
package org.opennms.netmgt.threshd;

import java.util.Map;

import org.opennms.core.utils.ParameterMap;

/**
 * <p>LatencyParameters class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class LatencyParameters {

	private Map m_parameters;
	private String m_svcName;

	/**
	 * <p>Constructor for LatencyParameters.</p>
	 *
	 * @param parameters a {@link java.util.Map} object.
	 * @param svcName a {@link java.lang.String} object.
	 */
	public LatencyParameters(Map parameters, String svcName) {

		m_parameters = parameters;
		m_svcName = svcName;
	}

	/**
	 * <p>getParameters</p>
	 *
	 * @return a {@link java.util.Map} object.
	 */
	public Map getParameters() {
		return m_parameters;
	}
	

	/**
	 * <p>getServiceName</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getServiceName() {
		return m_svcName;
	}

	int getInterval() {
		Map parameters = getParameters();
	    int interval = ParameterMap.getKeyedInteger(parameters, "interval", LatencyThresholder.DEFAULT_INTERVAL);
	    return interval;
	}

	String getGroupName() {
		Map parameters = getParameters();
	    String groupName = ParameterMap.getKeyedString(parameters, "thresholding-group", "default");
	    return groupName;
	}
	
	int getRange() {
		Map parameters = getParameters();
	    int range = ParameterMap.getKeyedInteger(parameters, "range", LatencyThresholder.DEFAULT_RANGE);
	    return range;
	}
	

}
