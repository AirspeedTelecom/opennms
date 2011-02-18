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
// 2007 Jun 24: Use Java 5 generics. - dj@opennms.org
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
// OpenNMS Licensing       <license@opennms.org>
//     http://www.opennms.org/
//     http://www.opennms.com/
//
package org.opennms.netmgt.provision.service.operations;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.opennms.netmgt.config.modelimport.types.InterfaceSnmpPrimaryType;
import org.opennms.netmgt.model.OnmsCategory;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsMonitoredService;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.model.OnmsServiceType;
import org.opennms.netmgt.model.OnmsIpInterface.PrimaryType;
import org.opennms.netmgt.provision.service.ProvisionService;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

/**
 * <p>Abstract SaveOrUpdateOperation class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public abstract class SaveOrUpdateOperation extends ImportOperation {

    private final OnmsNode m_node;
    private OnmsIpInterface m_currentInterface;
    
    private ScanManager m_scanManager;
    
    /**
     * <p>Constructor for SaveOrUpdateOperation.</p>
     *
     * @param foreignSource a {@link java.lang.String} object.
     * @param foreignId a {@link java.lang.String} object.
     * @param nodeLabel a {@link java.lang.String} object.
     * @param building a {@link java.lang.String} object.
     * @param city a {@link java.lang.String} object.
     * @param provisionService a {@link org.opennms.netmgt.provision.service.ProvisionService} object.
     */
    public SaveOrUpdateOperation(String foreignSource, String foreignId, String nodeLabel, String building, String city, ProvisionService provisionService) {
		this(null, foreignSource, foreignId, nodeLabel, building, city, provisionService);
	}

	/**
	 * <p>Constructor for SaveOrUpdateOperation.</p>
	 *
	 * @param nodeId a {@link java.lang.Integer} object.
	 * @param foreignSource a {@link java.lang.String} object.
	 * @param foreignId a {@link java.lang.String} object.
	 * @param nodeLabel a {@link java.lang.String} object.
	 * @param building a {@link java.lang.String} object.
	 * @param city a {@link java.lang.String} object.
	 * @param provisionService a {@link org.opennms.netmgt.provision.service.ProvisionService} object.
	 */
	public SaveOrUpdateOperation(Integer nodeId, String foreignSource, String foreignId, String nodeLabel, String building, String city, ProvisionService provisionService) {
	    super(provisionService);
	    
        m_node = new OnmsNode();
        m_node.setId(nodeId);
		m_node.setLabel(nodeLabel);
		m_node.setLabelSource("U");
		m_node.setType("A");
        m_node.setForeignSource(foreignSource);
        m_node.setForeignId(foreignId);
        m_node.getAssetRecord().setBuilding(building);
        m_node.getAssetRecord().setCity(city);
	}
	
	/**
	 * <p>getScanManager</p>
	 *
	 * @return a {@link org.opennms.netmgt.provision.service.operations.ScanManager} object.
	 */
	public ScanManager getScanManager() {
	    return m_scanManager;
	}

	/**
	 * <p>foundInterface</p>
	 *
	 * @param ipAddr a {@link java.lang.String} object.
	 * @param descr a {@link java.lang.Object} object.
	 * @param snmpPrimary a {@link InterfaceSnmpPrimaryType} object.
	 * @param managed a boolean.
	 * @param status a int.
	 */
	public void foundInterface(String ipAddr, Object descr, InterfaceSnmpPrimaryType snmpPrimary, boolean managed, int status) {
		
		if (ipAddr == null || "".equals(ipAddr.trim())) {
		    log().error(String.format("Found interface on node %s with an empty ipaddr! Ignoring!", m_node.getLabel()));
			return;
		}

        m_currentInterface = new OnmsIpInterface(ipAddr, m_node);
        m_currentInterface.setIsManaged(status == 3 ? "U" : "M");
        m_currentInterface.setIsSnmpPrimary(PrimaryType.get(snmpPrimary.toString()));
        
        if (InterfaceSnmpPrimaryType.P.equals(snmpPrimary)) {

            try {
                m_scanManager = new ScanManager(InetAddress.getByName(ipAddr));
            } catch (UnknownHostException e) {
                String msg = String.format("Unable to resolve address of snmpPrimary interface for node %s with address '%s'",m_node.getLabel(), ipAddr);
                log().error(msg, e);
            }

        }
        
        //FIXME: verify this doesn't conflict with constructor.  The constructor already adds this
        //interface to the node.
        m_node.addIpInterface(m_currentInterface);
    }
	
    /**
     * <p>scan</p>
     */
    public void scan() {
    	updateSnmpData();
	}
	
    /**
     * <p>updateSnmpData</p>
     */
    protected void updateSnmpData() {
        if (m_scanManager != null) {
            m_scanManager.updateSnmpData(m_node);
        }
	}

    /**
     * <p>foundMonitoredService</p>
     *
     * @param serviceName a {@link java.lang.String} object.
     */
    public void foundMonitoredService(String serviceName) {
        // current interface may be null if it has no ipaddr
        if (m_currentInterface != null) {
            OnmsServiceType svcType = getProvisionService().createServiceTypeIfNecessary(serviceName);
            OnmsMonitoredService service = new OnmsMonitoredService(m_currentInterface, svcType);
            service.setStatus("A"); // DbIfServiceEntry.STATUS_ACTIVE
            m_currentInterface.getMonitoredServices().add(service);
        }
    
    }

    /**
     * <p>foundCategory</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void foundCategory(String name) {
        OnmsCategory category = getProvisionService().createCategoryIfNecessary(name);
        m_node.getCategories().add(category);
    }

    /**
     * <p>getNode</p>
     *
     * @return a {@link org.opennms.netmgt.model.OnmsNode} object.
     */
    protected OnmsNode getNode() {
        return m_node;
    }

    /**
     * <p>foundAsset</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     */
    public void foundAsset(String name, String value) {
        BeanWrapper w = new BeanWrapperImpl(m_node.getAssetRecord());
        try {
            w.setPropertyValue(name, value);
        } catch (BeansException e) {
            log().error("Could not set property on object of type " + m_node.getClass().getName() + ": " + name, e);
        }
    }
}
