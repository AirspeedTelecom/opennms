package org.opennms.netmgt.provision.adapters.link;

import org.opennms.netmgt.provision.adapters.link.config.dao.DefaultLinkAdapterConfigurationDao;
import org.opennms.netmgt.provision.adapters.link.config.linkadapter.LinkPattern;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>DefaultLinkMatchResolverImpl class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class DefaultLinkMatchResolverImpl implements LinkMatchResolver {
    @Autowired
    private DefaultLinkAdapterConfigurationDao m_configDao;
    
    /** {@inheritDoc} */
    public String getAssociatedEndPoint(String endPoint) {
        if (m_configDao != null) {
            for (LinkPattern p : m_configDao.getPatterns()) {
                String endPointResolvedTemplate = p.resolveTemplate(endPoint);
                if (endPointResolvedTemplate != null) {
                    return endPointResolvedTemplate;
                }
            }
        }

        return null;
    }
}
