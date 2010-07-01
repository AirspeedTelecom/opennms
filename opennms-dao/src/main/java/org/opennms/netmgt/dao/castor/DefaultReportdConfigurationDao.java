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
 * Created: September 11, 2009
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
package org.opennms.netmgt.dao.castor;

import java.util.List;

import org.opennms.netmgt.config.reportd.ReportdConfiguration;
import org.opennms.netmgt.config.reportd.Report;
import org.opennms.netmgt.dao.ReportdConfigurationDao;
import org.springframework.dao.DataAccessResourceFailureException;

/**
 * <p>DefaultReportdConfigurationDao class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class DefaultReportdConfigurationDao extends AbstractCastorConfigDao<ReportdConfiguration, ReportdConfiguration> implements ReportdConfigurationDao {

    /**
     * <p>Constructor for DefaultReportdConfigurationDao.</p>
     */
    public DefaultReportdConfigurationDao() {
        super(ReportdConfiguration.class, "Reportd Configuration");
    }
    
    /**
     * <p>getConfig</p>
     *
     * @return a {@link org.opennms.netmgt.config.reportd.ReportdConfiguration} object.
     */
    public ReportdConfiguration getConfig() {
        return getContainer().getObject();
    }
    
    //@Override
    /**
     * <p>translateConfig</p>
     *
     * @param castorConfig a {@link org.opennms.netmgt.config.reportd.ReportdConfiguration} object.
     * @return a {@link org.opennms.netmgt.config.reportd.ReportdConfiguration} object.
     */
    public ReportdConfiguration translateConfig(ReportdConfiguration castorConfig) {
        return castorConfig;
    }
    
    
    /**
     * <p>reloadConfiguration</p>
     *
     * @throws org.springframework.dao.DataAccessResourceFailureException if any.
     */
    public void reloadConfiguration() throws DataAccessResourceFailureException {
        getContainer().reload();
    }
    
    /** {@inheritDoc} */
    public Report getReport(String reportName) {
        for (Report report : getReports()) {
            if (report.getReportName().equals(reportName)) {
                return report;
            }
        }
        return null;
    }
    
    /**
     * <p>getReports</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Report> getReports() {
        return getConfig().getReportCollection();
    }

    /**
     * <p>getPersistFlag</p>
     *
     * @return a boolean.
     */
    public boolean getPersistFlag() {
        
        //return  getConfig().getPersistReports();
        String strval = getConfig().getPersistReports();
        boolean retval=false;
        if(strval.equals("yes") || strval.equals("on") ){
            retval = true;
        }
        
        else if(strval.equals("off") ||strval.equals("no")){
          retval = false; 
        }
        
        return retval;
    }

    /**
     * <p>getStorageDirectory</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getStorageDirectory() {
        return getConfig().getStorageLocation();
    }
    
    /** {@inheritDoc} */
    public boolean deleteReport(String report){
        return getConfig().removeReport(getReport(report));
    }
    
        
}
