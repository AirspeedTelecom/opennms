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
// 2008 Feb 10: Use default configs. - dj@opennms.org
// 2007 Aug 25: Use AbstractTransactionalTemporaryDatabaseSpringContextTests
//              and new Spring context files. - dj@opennms.org
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
package org.opennms.netmgt.provision.service;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.net.InetAddress;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.joda.time.Duration;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.concurrent.PausibleScheduledThreadPoolExecutor;
import org.opennms.core.tasks.Task;
import org.opennms.mock.snmp.JUnitSnmpAgent;
import org.opennms.mock.snmp.JUnitSnmpAgentExecutionListener;
import org.opennms.netmgt.config.SnmpPeerFactory;
import org.opennms.netmgt.dao.DistPollerDao;
import org.opennms.netmgt.dao.IpInterfaceDao;
import org.opennms.netmgt.dao.MonitoredServiceDao;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.ServiceTypeDao;
import org.opennms.netmgt.dao.SnmpInterfaceDao;
import org.opennms.netmgt.dao.db.JUnitTemporaryDatabase;
import org.opennms.netmgt.dao.db.OpenNMSConfigurationExecutionListener;
import org.opennms.netmgt.dao.db.TemporaryDatabaseExecutionListener;
import org.opennms.netmgt.provision.persist.ForeignSourceRepository;
import org.opennms.netmgt.provision.persist.MockForeignSourceRepository;
import org.opennms.netmgt.provision.persist.OnmsAssetRequisition;
import org.opennms.netmgt.provision.persist.OnmsIpInterfaceRequisition;
import org.opennms.netmgt.provision.persist.OnmsMonitoredServiceRequisition;
import org.opennms.netmgt.provision.persist.OnmsNodeCategoryRequisition;
import org.opennms.netmgt.provision.persist.OnmsNodeRequisition;
import org.opennms.netmgt.provision.persist.OnmsServiceCategoryRequisition;
import org.opennms.netmgt.provision.persist.RequisitionVisitor;
import org.opennms.netmgt.provision.persist.foreignsource.ForeignSource;
import org.opennms.netmgt.provision.persist.requisition.Requisition;
import org.opennms.test.mock.MockLogAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.style.ToStringCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit test for ModelImport application.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
    OpenNMSConfigurationExecutionListener.class,
    TemporaryDatabaseExecutionListener.class,
    JUnitSnmpAgentExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class
})
@ContextConfiguration(locations={
        "classpath:/META-INF/opennms/applicationContext-soa.xml",
        "classpath:/META-INF/opennms/applicationContext-dao.xml",
        "classpath:/META-INF/opennms/applicationContext-daemon.xml",
        "classpath:/META-INF/opennms/applicationContext-proxy-snmp.xml",
        "classpath:/META-INF/opennms/mockEventIpcManager.xml",
        "classpath:/META-INF/opennms/applicationContext-setupIpLike-enabled.xml",
        "classpath:/META-INF/opennms/applicationContext-provisiond.xml",
        "classpath*:/META-INF/opennms/component-dao.xml",
        "classpath*:/META-INF/opennms/detectors.xml",
        "classpath:/importerServiceTest.xml"
})
@JUnitTemporaryDatabase()
public class NewSuspectScanTest {
    
    @Autowired
    private Provisioner m_provisioner;
    
    @Autowired
    private ServiceTypeDao m_serviceTypeDao;
    
    @Autowired
    private MonitoredServiceDao m_monitoredServiceDao;
    
    @Autowired
    private IpInterfaceDao m_ipInterfaceDao;
    
    @Autowired
    private SnmpInterfaceDao m_snmpInterfaceDao;
    
    @Autowired
    private NodeDao m_nodeDao;

    @Autowired
    private DistPollerDao m_distPollerDao;
    
    @Autowired
    private ProvisionService m_provisionService;
    
    @Autowired
    private PausibleScheduledThreadPoolExecutor m_pausibleExecutor;
    
    private ForeignSourceRepository m_foreignSourceRepository;
    
    private ForeignSource m_foreignSource;

    static private String s_initialDiscoveryEnabledValue;
    
    @BeforeClass
    public static void setUpSnmpConfig() {
        SnmpPeerFactory.setFile(new File("src/test/proxy-snmp-config.xml"));

        Properties props = new Properties();
        props.setProperty("log4j.logger.org.hibernate", "INFO");
        props.setProperty("log4j.logger.org.springframework", "INFO");
        props.setProperty("log4j.logger.org.hibernate.SQL", "DEBUG");

        MockLogAppender.setupLogging(props);
        
        //System.setProperty("mock.debug", "false");
        
    }

    @BeforeClass
    public static void setEnableDiscovery() {
        s_initialDiscoveryEnabledValue = System.getProperty("org.opennms.provisiond.enableDiscovery");
        System.setProperty("org.opennms.provisiond.enableDiscovery", "true");
        
    }
    
    @AfterClass
    public static void resetEnableDiscovery() {
        if (s_initialDiscoveryEnabledValue == null) {
            System.getProperties().remove("org.opennms.provisiond.enableDiscovery");
        } else {
            System.setProperty("org.opennms.provisiond.enableDiscovery", s_initialDiscoveryEnabledValue);
        }
    }
    
    @Before
    public void setUp() throws Exception {
        
        m_provisioner.start();
        
        m_foreignSource = new ForeignSource();
        m_foreignSource.setName("imported:");
        m_foreignSource.setScanInterval(Duration.standardDays(1));
        
        m_foreignSourceRepository = new MockForeignSourceRepository();
        m_foreignSourceRepository.save(m_foreignSource);
        
        m_provisionService.setForeignSourceRepository(m_foreignSourceRepository);
        
        m_pausibleExecutor.pause();

    }

    @Test(timeout=300000)
    @Transactional
    @JUnitSnmpAgent(resource="classpath:snmpTestData3.properties")
    public void testScanNewSuspect() throws Exception {
        
        //Verify empty database
        assertEquals(1, getDistPollerDao().countAll());
        assertEquals(0, getNodeDao().countAll());
        assertEquals(0, getInterfaceDao().countAll());
        assertEquals(0, getMonitoredServiceDao().countAll());
        assertEquals(0, getServiceTypeDao().countAll());
        assertEquals(0, getSnmpInterfaceDao().countAll());


        NewSuspectScan scan = m_provisioner.createNewSuspectScan(InetAddress.getByName("172.20.2.201"));
        runScan(scan);

        // wait for NodeScan triggered by NodeAdded to complete
        Thread.sleep(100000);

        //Verify distpoller count
        assertEquals(1, getDistPollerDao().countAll());

        //Verify node count
        assertEquals(1, getNodeDao().countAll());

        //Verify ipinterface count
        assertEquals(2, getInterfaceDao().countAll());

        //Verify ifservices count - discover snmp service on other if
        assertEquals("Unexpected number of services found: "+getMonitoredServiceDao().findAll(), 2, getMonitoredServiceDao().countAll());

        //Verify service count
        assertEquals(1, getServiceTypeDao().countAll());

        //Verify snmpInterface count
        assertEquals(6, getSnmpInterfaceDao().countAll());

    }
    
    @Test(timeout=300000)
    @Transactional
    public void testScanNewSuspectNoSnmp() throws Exception {

        //Verify empty database
        assertEquals(1, getDistPollerDao().countAll());
        assertEquals(0, getNodeDao().countAll());
        assertEquals(0, getInterfaceDao().countAll());
        assertEquals(0, getMonitoredServiceDao().countAll());
        assertEquals(0, getServiceTypeDao().countAll());
        assertEquals(0, getSnmpInterfaceDao().countAll());
        
        
        NewSuspectScan scan = m_provisioner.createNewSuspectScan(InetAddress.getByName("172.20.2.201"));
        runScan(scan);
        

        //Verify distpoller count
        assertEquals(1, getDistPollerDao().countAll());
        
        //Verify node count
        assertEquals(1, getNodeDao().countAll());
        
        //Verify ipinterface count
        assertEquals(1, getInterfaceDao().countAll());
        
        //Verify ifservices count - discover snmp service on other if
        assertEquals("Unexpected number of services found: "+getMonitoredServiceDao().findAll(), 0, getMonitoredServiceDao().countAll());
        
        //Verify service count
        assertEquals(0, getServiceTypeDao().countAll());

        //Verify snmpInterface count
        assertEquals(0, getSnmpInterfaceDao().countAll());
        
    }
    
    public void runScan(NewSuspectScan scan) throws InterruptedException, ExecutionException {
        Task t = scan.createTask();
        t.schedule();
        t.waitFor();
    }

    
    private DistPollerDao getDistPollerDao() {
        return m_distPollerDao;
    }


    private NodeDao getNodeDao() {
        return m_nodeDao;
    }


    private IpInterfaceDao getInterfaceDao() {
        return m_ipInterfaceDao;
    }


    private SnmpInterfaceDao getSnmpInterfaceDao() {
        return m_snmpInterfaceDao;
    }


    private MonitoredServiceDao getMonitoredServiceDao() {
        return m_monitoredServiceDao;
    }


    private ServiceTypeDao getServiceTypeDao() {
        return m_serviceTypeDao;
    }
    
    static class CountingVisitor implements RequisitionVisitor {
        private int m_modelImportCount;
        private int m_modelImportCompleted;
        private int m_nodeCount;
        private int m_nodeCompleted;
        private int m_nodeCategoryCount;
        private int m_nodeCategoryCompleted;
        private int m_ifaceCount;
        private int m_ifaceCompleted;
        private int m_svcCount;
        private int m_svcCompleted;
        private int m_svcCategoryCount;
        private int m_svcCategoryCompleted;
        private int m_assetCount;
        private int m_assetCompleted;
        
        public int getModelImportCount() {
            return m_modelImportCount;
        }
        
        public int getModelImportCompletedCount() {
            return m_modelImportCompleted;
        }
        
        public int getNodeCount() {
            return m_nodeCount;
        }
        
        public int getNodeCompletedCount() {
            return m_nodeCompleted;
        }
        
        public int getInterfaceCount() {
            return m_ifaceCount;
        }
        
        public int getInterfaceCompletedCount() {
            return m_ifaceCompleted;
        }
        
        public int getMonitoredServiceCount() {
            return m_svcCount;
        }
        
        public int getMonitoredServiceCompletedCount() {
            return m_svcCompleted;
        }
        
        public int getNodeCategoryCount() {
            return m_nodeCategoryCount;
        }

        public int getNodeCategoryCompletedCount() {
            return m_nodeCategoryCompleted;
        }

        public int getServiceCategoryCount() {
            return m_svcCategoryCount;
        }

        public int getServiceCategoryCompletedCount() {
            return m_svcCategoryCompleted;
        }

        public int getAssetCount() {
            return m_assetCount;
        }
        
        public int getAssetCompletedCount() {
            return m_assetCompleted;
        }
        
        public void visitModelImport(Requisition req) {
            m_modelImportCount++;
        }

        public void visitNode(OnmsNodeRequisition nodeReq) {
            m_nodeCount++;
            assertEquals("apknd", nodeReq.getNodeLabel());
            assertEquals("4243", nodeReq.getForeignId());
        }

        public void visitInterface(OnmsIpInterfaceRequisition ifaceReq) {
            m_ifaceCount++;
        }

        public void visitMonitoredService(OnmsMonitoredServiceRequisition monSvcReq) {
            m_svcCount++;
        }

        public void visitNodeCategory(OnmsNodeCategoryRequisition catReq) {
            m_nodeCategoryCount++;
        }
        
        public void visitServiceCategory(OnmsServiceCategoryRequisition catReq) {
            m_svcCategoryCount++;
        }
        
        public void visitAsset(OnmsAssetRequisition assetReq) {
            m_assetCount++;
        }
        
        public String toString() {
            return (new ToStringCreator(this)
                .append("modelImportCount", getModelImportCount())
                .append("modelImportCompletedCount", getModelImportCompletedCount())
                .append("nodeCount", getNodeCount())
                .append("nodeCompletedCount", getNodeCompletedCount())
                .append("nodeCategoryCount", getNodeCategoryCount())
                .append("nodeCategoryCompletedCount", getNodeCategoryCompletedCount())
                .append("interfaceCount", getInterfaceCount())
                .append("interfaceCompletedCount", getInterfaceCompletedCount())
                .append("monitoredServiceCount", getMonitoredServiceCount())
                .append("monitoredServiceCompletedCount", getMonitoredServiceCompletedCount())
                .append("serviceCategoryCount", getServiceCategoryCount())
                .append("serviceCategoryCompletedCount", getServiceCategoryCompletedCount())
                .append("assetCount", getAssetCount())
                .append("assetCompletedCount", getAssetCompletedCount())
                .toString());
        }

        public void completeModelImport(Requisition req) {
            m_modelImportCompleted++;
        }

        public void completeNode(OnmsNodeRequisition nodeReq) {
            m_nodeCompleted++;
        }

        public void completeInterface(OnmsIpInterfaceRequisition ifaceReq) {
            m_ifaceCompleted++;
        }

        public void completeMonitoredService(OnmsMonitoredServiceRequisition monSvcReq) {
            m_svcCompleted++;
        }

        public void completeNodeCategory(OnmsNodeCategoryRequisition catReq) {
            m_nodeCategoryCompleted++;
        }
        
        public void completeServiceCategory(OnmsServiceCategoryRequisition catReq) {
            m_nodeCategoryCompleted++;
        }
        
        public void completeAsset(OnmsAssetRequisition assetReq) {
            m_assetCompleted++;
        }
        
    }
    

}
