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
// Modifications:
//
// 2008 May 31: Make marshalDataSourceFromConfig public and static and inline
//              a few one-line methods that don't need to be there since
//              Castor classes are now genericized. - dj@opennms.org 
// 2007 Aug 02: Prepare for Castor 1.0.5, Java 5 generics and loops. - dj@opennms.org
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
//   OpenNMS Licensing       <license@opennms.org>
//   http://www.opennms.org/
//   http://www.opennms.com/
//
// Tab Size = 8

package org.opennms.netmgt.config;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.config.opennmsDataSources.JdbcDataSource;

import snaq.db.DBPoolDataSource;

/**
 * <p>C3P0ConnectionFactory class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class DBPoolConnectionFactory extends BaseConnectionFactory {

	private DBPoolDataSource m_dataSource;

    public DBPoolConnectionFactory(final InputStream stream, final String dsName) throws MarshalException, ValidationException, PropertyVetoException, SQLException {
    	super(stream, dsName);
    }
    /**
     * @deprecated Use code for InputStream instead to avoid character set issues
     */
    public DBPoolConnectionFactory(Reader rdr, String dsName) throws MarshalException, ValidationException, PropertyVetoException, SQLException {
    	super(rdr, dsName);
    }

    public DBPoolConnectionFactory(final String configFile, final String dsName) throws IOException, MarshalException, ValidationException, PropertyVetoException, SQLException {
    	super(configFile, dsName);
    }

    protected void initializePool(final JdbcDataSource dataSource) throws SQLException {
    	m_dataSource = new DBPoolDataSource();
    	m_dataSource.setName(dataSource.getName());
    	m_dataSource.setDriverClassName(dataSource.getClassName());
    	m_dataSource.setUrl(dataSource.getUrl());
    	m_dataSource.setUser(dataSource.getUserName());
    	m_dataSource.setPassword(dataSource.getPassword());
    }

    public Connection getConnection() throws SQLException {
    	return m_dataSource.getConnection();
    }

    public String getUrl() {
    	return m_dataSource.getUrl();
    }

    public void setUrl(final String url) {
    	m_dataSource.setUrl(url);
    }

    public String getUser() {
    	return m_dataSource.getUser();
    }

    public void setUser(final String user) {
    	m_dataSource.setUser(user);
    }

    public DataSource getDataSource() {
    	return m_dataSource;
    }

    public Connection getConnection(final String username, final String password) throws SQLException {
    	return m_dataSource.getConnection(username, password);
    }

    public PrintWriter getLogWriter() throws SQLException {
    	return m_dataSource.getLogWriter();
    }

    public void setLogWriter(final PrintWriter out) throws SQLException {
        m_dataSource.setLogWriter(out);
    }

    public void setLoginTimeout(final int seconds) throws SQLException {
        m_dataSource.setLoginTimeout(seconds);
    }

    public int getLoginTimeout() throws SQLException {
        return m_dataSource.getLoginTimeout();
    }

    public void close() throws SQLException {
    	super.close();
    	LogUtils.infof(this, "Closing DBPool pool.");
    	m_dataSource.release();
    }

	public void setIdleTimeout(final int idleTimeout) {
		m_dataSource.setIdleTimeout(idleTimeout);
	}

	public void setMinPool(final int minPool) {
		m_dataSource.setMinPool(minPool);
	}

	public void setMaxPool(final int maxPool) {
		m_dataSource.setMaxPool(maxPool);
	}

	public void setMaxSize(final int maxSize) {
		m_dataSource.setMaxSize(maxSize);
	}
}
