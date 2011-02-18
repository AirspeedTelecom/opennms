package org.opennms.netmgt.provision;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opennms.core.test.JUnitDNSServerExecutionListener;
import org.opennms.core.test.annotations.DNSEntry;
import org.opennms.core.test.annotations.DNSZone;
import org.opennms.core.test.annotations.JUnitDNSServer;
import org.opennms.netmgt.dao.NodeDao;
import org.opennms.netmgt.dao.db.JUnitTemporaryDatabase;
import org.opennms.netmgt.dao.db.OpenNMSConfigurationExecutionListener;
import org.opennms.netmgt.dao.db.TemporaryDatabaseExecutionListener;
import org.opennms.netmgt.model.NetworkBuilder;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.provision.SimpleQueuedProvisioningAdapter.AdapterOperation;
import org.opennms.netmgt.provision.SimpleQueuedProvisioningAdapter.AdapterOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({
    OpenNMSConfigurationExecutionListener.class,
    TemporaryDatabaseExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    JUnitDNSServerExecutionListener.class
})
@ContextConfiguration(locations= {
        "classpath:/META-INF/opennms/applicationContext-dao.xml",
        "classpath*:/META-INF/opennms/component-dao.xml",
        "classpath:/META-INF/opennms/applicationContext-daemon.xml",
        "classpath:/META-INF/opennms/mockEventIpcManager.xml",
        "classpath:/META-INF/opennms/applicationContext-databasePopulator.xml",
        "classpath:/META-INF/opennms/provisiond-extensions.xml"})
public class DnsProvisioningAdapterTest {
    @Autowired
    private DnsProvisioningAdapter m_adapter;
    
    @Autowired
    private NodeDao m_nodeDao;

    private AdapterOperation m_adapterOperation;
    
    @Before
    @Transactional
    public void setUp() throws Exception {
        NetworkBuilder nb = new NetworkBuilder();
        nb.addNode("test.example.com").setForeignSource("dns").setForeignId("1");
        nb.addInterface("192.168.0.1");
        m_nodeDao.save(nb.getCurrentNode());
        m_nodeDao.flush();

        m_adapterOperation = m_adapter.new AdapterOperation(
            m_nodeDao.findByForeignId("dns", "1").getId(),
            AdapterOperationType.ADD,
            new SimpleQueuedProvisioningAdapter.AdapterOperationSchedule(0, 1, 1, TimeUnit.SECONDS)
        );
        
        System.setProperty("importer.adapter.dns.server", "127.0.0.1:9153");
        System.setProperty("importer.adapter.dns.privatekey", "hmac-md5/test.example.com./QBMBi+8THN8iyAuGIhniB+fiURwQjrrpwFuq1L6NmHcya7QdKqjwp6kLIczPjsAUDcqiLAdQJnQUhCPThA4XtQ==");
    }

    @Test
    @Transactional
    public void testAdd() throws Exception {
        OnmsNode n = m_nodeDao.findByForeignId("dns", "1");
        m_adapter.addNode(n.getId());
        m_adapter.processPendingOperationForNode(m_adapterOperation);
        Thread.sleep(3);
    }
    
    @Test
    @Transactional
    @JUnitTemporaryDatabase()
    @JUnitDNSServer(port=9153, zones={
            @DNSZone(name="example.com", entries={
                    @DNSEntry(hostname="test", address="192.168.0.1")
            })
    })
    public void testDelete() throws Exception {
        OnmsNode n = m_nodeDao.findByForeignId("dns", "1");
        m_adapter.deleteNode(n.getId());
        m_adapter.processPendingOperationForNode(m_adapterOperation);
        Thread.sleep(3);
    }
}
