package org.opennms.netmgt.collectd;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jrobin.core.RrdDb;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opennms.core.utils.LogUtils;
import org.opennms.netmgt.config.VmwareDataCollectionConfigFactory;
import org.opennms.netmgt.config.collector.CollectionSet;
import org.opennms.netmgt.config.collector.ServiceParameters;
import org.opennms.test.mock.MockLogAppender;

public class VmwareCollectorTest {
    
    private static final String COLLECTION_NAME = "default-VirtualMachine";

    @Before
    public void setUp() throws Exception {
        MockLogAppender.setupLogging();
        System.setProperty("opennms.home", "src/test/resources");
        VmwareDataCollectionConfigFactory.setInstance(new VmwareDataCollectionConfigFactory("src/test/resources/etc/vmware-datacollection-config.xml"));
        FileUtils.deleteDirectory(new File("target/1"));
    }
    
    @After
    public void tearDown() throws Exception {
        MockLogAppender.assertNoWarningsOrGreater();
    }
    
    @Test
    public void testCollectorWithStoreByGroup() throws Exception {
        System.setProperty("org.opennms.rrd.storeByGroup", "true");
        CollectionSet collectionSet = executeCollector();
        
        // Verifying collection set structure
        VmwareCollectionSetVerifier verifier = new VmwareCollectionSetVerifier();
        collectionSet.visit(verifier);

        // Persisting data
        GroupPersister persister = new GroupPersister(new ServiceParameters(new HashMap<String,Object>()), VmwareDataCollectionConfigFactory.getInstance().getRrdRepository(COLLECTION_NAME));
        collectionSet.visit(persister);

        // Verify JRBs
        int count = 0;
        for (Object o : FileUtils.listFiles(new File("target/1"), new String[] { "jrb" }, true)) {
            RrdDb jrb = new RrdDb((File) o);
//            Assert.assertTrue(jrb.getDsCount() > 1);
            count++;
        }
        Assert.assertEquals(5, count);
    }

    @Test
    public void testCollectorWithoutStoreByGroup() throws Exception {
        System.setProperty("org.opennms.rrd.storeByGroup", "false");
        CollectionSet collectionSet = executeCollector();
        
        // Persisting data
        OneToOnePersister persister = new OneToOnePersister(new ServiceParameters(new HashMap<String,Object>()), VmwareDataCollectionConfigFactory.getInstance().getRrdRepository(COLLECTION_NAME));
        collectionSet.visit(persister);

        // Verify JRBs
        int count = 0;
        for (Object o : FileUtils.listFiles(new File("target/1"), new String[] { "jrb" }, true)) {
            RrdDb jrb = new RrdDb((File) o);
            Assert.assertTrue(jrb.getDsCount() == 1);
            count++;
        }
//        Assert.assertEquals(53, count);
        Assert.assertEquals(58, count);
    }

    public CollectionSet executeCollector() throws Exception {
        // Global Properties

        String vcenterAddress = "192.168.1.23";
        String managedObjectId = "vm-1234";

        // Creating collection agent and parameters
        CollectionAgent agent = new MockCollectionAgent(1, vcenterAddress);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("collection", COLLECTION_NAME);
        parameters.put("vmwareManagementServer", vcenterAddress);
        parameters.put("vmwareManagedObjectId", managedObjectId);

        // Creating VMWare Collector and retrieving CollectionSet
        LogUtils.infof(this, "Collecting data using " + parameters);
        VmwareCollector collector = new VmwareCollector();        
        CollectionSet collectionSet = collector.collect(agent, null, parameters);
        Assert.assertNotNull(collectionSet);
        Assert.assertEquals(ServiceCollector.COLLECTION_SUCCEEDED, collectionSet.getStatus());
        return collectionSet;
    }

}
