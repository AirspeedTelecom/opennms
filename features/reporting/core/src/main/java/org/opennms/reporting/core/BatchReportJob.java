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
 * Created: November 17, 2009 jonathan@opennms.org
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

package org.opennms.reporting.core;

import org.opennms.api.reporting.parameter.ReportParameters;
import org.opennms.reporting.core.svclayer.ReportWrapperService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * <p>BatchReportJob class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class BatchReportJob extends QuartzJobBean {
    
    private ApplicationContext m_context;

    /** {@inheritDoc} */
    @Override
    protected void executeInternal(JobExecutionContext jobContext)
            throws JobExecutionException {
        
        JobDataMap dataMap = jobContext.getMergedJobDataMap();
       
        // TODO this needs the reportServiceName in the criteria 
        
//        
//        ReportServiceLocator reportServiceLocator =
//            (ReportServiceLocator)m_context.getBean("reportServiceLocator");
//        
//        ReportService reportService = reportServiceLocator.getReportService((String)dataMap.get("reportServiceName"));
//        
//        reportService.run(criteria.getReportParms(), 
//                          deliveryOptions, 
//                          (String)dataMap.get("reportId"));
        
        ReportWrapperService reportWrapperService = 
            (ReportWrapperService)m_context.getBean("reportWrapperService");
        
        reportWrapperService.run((ReportParameters) dataMap.get("criteria"),
                                 (DeliveryOptions) dataMap.get("deliveryOptions"),
                                 (String)dataMap.get("reportId"));
        
    }
    
    /**
     * <p>setApplicationContext</p>
     *
     * @param applicationContext a {@link org.springframework.context.ApplicationContext} object.
     */
    public void setApplicationContext(ApplicationContext applicationContext) {
        m_context = applicationContext;
    }
    
    

}
