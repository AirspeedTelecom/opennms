/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2007-2008 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * Modifications:
 * 
 * 2009 Jan 26: added getResourcesFromGraphs - part of ksc performance improvement. - ayres@opennms.org
 * Created: January 2, 2007
 *
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
package org.opennms.web.svclayer;

import java.util.List;
import java.util.Map;

import org.opennms.netmgt.config.kscReports.Graph;
import org.opennms.netmgt.config.kscReports.Report;
import org.opennms.netmgt.model.OnmsResource;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>KscReportService interface.</p>
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @version $Id: $
 * @since 1.8.1
 */
@Transactional(readOnly = true)
public interface KscReportService {
    /**
     * <p>buildNodeReport</p>
     *
     * @param nodeId a int.
     * @return a {@link org.opennms.netmgt.config.kscReports.Report} object.
     */
    public Report buildNodeReport(int nodeId);
    /**
     * <p>buildDomainReport</p>
     *
     * @param domain a {@link java.lang.String} object.
     * @return a {@link org.opennms.netmgt.config.kscReports.Report} object.
     */
    public Report buildDomainReport(String domain);
    /**
     * <p>getResourceFromGraph</p>
     *
     * @param graph a {@link org.opennms.netmgt.config.kscReports.Graph} object.
     * @return a {@link org.opennms.netmgt.model.OnmsResource} object.
     */
    public OnmsResource getResourceFromGraph(Graph graph);
    /**
     * <p>getResourcesFromGraphs</p>
     *
     * @param graphs a {@link java.util.List} object.
     * @return a {@link java.util.List} object.
     */
    public List<OnmsResource>getResourcesFromGraphs(List<Graph> graphs);
    /**
     * <p>getTimeSpans</p>
     *
     * @param includeNone a boolean.
     * @return a java$util$Map object.
     */
    public Map<String, String> getTimeSpans(boolean includeNone);
    /**
     * <p>getReportList</p>
     *
     * @return a java$util$Map object.
     */
    public Map<Integer, String> getReportList();
}
