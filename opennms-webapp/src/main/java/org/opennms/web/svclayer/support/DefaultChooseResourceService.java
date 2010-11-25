
/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2006-2008 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * Modifications:
 *
 * 2008 Oct 22: Use new loadResourceById method. - dj@opennms.org
 * 
 * Created: November 8, 2006
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
package org.opennms.web.svclayer.support;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.dao.ResourceDao;
import org.opennms.netmgt.model.OnmsResource;
import org.opennms.netmgt.model.OnmsResourceType;
import org.opennms.web.Util;
import org.opennms.web.svclayer.ChooseResourceService;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>DefaultChooseResourceService class.</p>
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @version $Id: $
 * @since 1.8.1
 */
public class DefaultChooseResourceService implements ChooseResourceService, InitializingBean {

    public ResourceDao m_resourceDao;

    /** {@inheritDoc} */
    public ChooseResourceModel findChildResources(String resourceId, String endUrl) {
        if (resourceId == null) {
            throw new IllegalArgumentException("resourceId parameter may not be null");
        }
        
        if (endUrl == null) {
            throw new IllegalArgumentException("endUrl parameter may not be null");
        }

        ChooseResourceModel model = new ChooseResourceModel();
        model.setEndUrl(endUrl);
        
        OnmsResource resource = m_resourceDao.getResourceById(resourceId);
        if (resource == null) {
            throw new IllegalArgumentException("resource \"" + resourceId + "\" could not be found");
        }

        model.setResource(resource);
        Map<OnmsResourceType, List<OnmsResource>> resourceTypeMap = new LinkedHashMap<OnmsResourceType, List<OnmsResource>>();
        
        ThreadCategory log = ThreadCategory.getInstance(this.getClass());
        for (OnmsResource childResource : resource.getChildResources()) {
            if (!resourceTypeMap.containsKey(childResource.getResourceType())) {
                resourceTypeMap.put(childResource.getResourceType(), new LinkedList<OnmsResource>());
            }
            // See bug 3760: These values have been known to contain a % sign so they are 
            // not safe to pass to LogUtils.infof()
            // http://bugzilla.opennms.org/show_bug.cgi?id=3760
            if (log.isInfoEnabled()) {
                log.info("getId(): " + childResource.getId());
                log.info("getName(): " + childResource.getName());
            }
            //checkLabelForQuotes(
            resourceTypeMap.get(childResource.getResourceType()).add(checkLabelForQuotes(childResource));
        }
        
        model.setResourceTypes(resourceTypeMap);

        return model;
    }
    
    private OnmsResource checkLabelForQuotes(OnmsResource childResource) {
        
        String lbl  = Util.convertToJsSafeString(childResource.getLabel());
        
        OnmsResource resource = new OnmsResource(childResource.getName(), lbl, childResource.getResourceType(), childResource.getAttributes());
        resource.setParent(childResource.getParent());
        resource.setEntity(childResource.getEntity());
        resource.setLink(childResource.getLink());
        return resource;
    }

    /**
     * <p>afterPropertiesSet</p>
     */
    public void afterPropertiesSet() {
        if (m_resourceDao == null) {
            throw new IllegalStateException("resourceDao property not set");
        }
    }

    /**
     * <p>getResourceDao</p>
     *
     * @return a {@link org.opennms.netmgt.dao.ResourceDao} object.
     */
    public ResourceDao getResourceDao() {
        return m_resourceDao;
    }

    /**
     * <p>setResourceDao</p>
     *
     * @param resourceDao a {@link org.opennms.netmgt.dao.ResourceDao} object.
     */
    public void setResourceDao(ResourceDao resourceDao) {
        m_resourceDao = resourceDao;
    }

}
