/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2009 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * Modifications:
 * 
 * Created: February 3rd 2010 jonathan@opennms.org
 *
 * Copyright (C) 2009 The OpenNMS Group, Inc.  All rights reserved.
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
package org.opennms.reporting.core.svclayer;

import java.io.OutputStream;
import java.util.List;

import org.opennms.api.reporting.ReportFormat;
import org.opennms.api.reporting.parameter.ReportParameters;
import org.opennms.reporting.core.DeliveryOptions;

/**
 * Interface that finds and executes individual reportServices.
 * Always run a report service via this wrapper as the implementation will find
 * the correct service for the reportId and wrap it as necessary.
 *
 * @author <a href="mailto:jonathan@opennms.org">Jonathan Sartin</a>
 * @version $Id: $
 */
public interface ReportWrapperService {

    /**
     * This method validates that the map of report parameters matches the report
     * parameters accepted by the report. Used by the web interface.
     *
     * @param parameters runtime report parameters
     * @param reportId reportId as defined in database-reports.xml
     * @return true if the reportParms supplied match those in the report definition.
     */
    public abstract boolean validate(ReportParameters parameters,
            String reportId);
    
    /**
     * This method runs the report
     *
     * @param parameters runtime report parameters
     * @param deliveryOptions delivery options for the report
     * @param reportId reportId as defined in database-reports.xml
     */
    public abstract void run(ReportParameters parameters, DeliveryOptions deliveryOptions,
            String reportId);
    
    /**
     * This method returns the delivery options for the report. Providing a userID will
     * allow the report service to pre-populate the destination address
     *
     * @param userId a {@link java.lang.String} object.
     * @param reportId a {@link java.lang.String} object.
     * @return a delivery options object containing information that describes how the report might
     *         be delivered.
     */
    public abstract DeliveryOptions getDeliveryOptions(String userId, String reportId);
    
    /**
     * This method provides a list of formats supported by the report
     *
     * @param reportId reportId as defined in database-reports.xml
     * @return a list of supported formats
     */
    public abstract List<ReportFormat> getFormats(String reportId);
    
    /**
     * This method runs the report and renders in into the given output stream
     * with no intermediate steps
     *
     * @param parameters runtime report parameters
     * @param outputStream stream to render the resulting report
     */
    public abstract void runAndRender(ReportParameters parameters, OutputStream outputStream);
    
    /**
     * This method renders the report into a given output stream.
     *
     * @param ReportId reportId as defined in database-reports.xml
     * @param location location of the report on disk
     * @param format format to render the report
     * @param outputStream stream to render the resulting report
     */
    public abstract void render(String ReportId, String location, ReportFormat format, OutputStream outputStream);
    
    
    /**
     * This method is used to determine whether the report takes any parameters
     *
     *  @return true if the report takes parameters, false if not.
     * @param ReportId a {@link java.lang.String} object.
     */
    public abstract Boolean hasParameters(String ReportId);
    
    /**
     * This method retrieves the runtime parameters taken by the report
     *
     * @return a ReportParameters object containing the parameters taken by the report
     * @param ReportId a {@link java.lang.String} object.
     */
    public abstract ReportParameters getParameters(String ReportId);
    
}
