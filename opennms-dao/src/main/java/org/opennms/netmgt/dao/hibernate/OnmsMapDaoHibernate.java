/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2009-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.netmgt.dao.hibernate;

import java.util.Collection;
import java.util.Date;

import org.opennms.netmgt.dao.OnmsMapDao;
import org.opennms.netmgt.model.OnmsMap;
import org.springframework.orm.jpa.JpaCallback;

import javax.persistence.EntityManager;

public class OnmsMapDaoHibernate extends AbstractDaoHibernate<OnmsMap, Integer> implements OnmsMapDao {
    public OnmsMapDaoHibernate() {
        super(OnmsMap.class);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<OnmsMap> findMapsLike(String mapLabel) {
        return find("from OnmsMap as map where map.name like ?", "%" + mapLabel + "%");
    }

    /** {@inheritDoc} */
    @Override
    public Collection<OnmsMap> findMapsByName(String mapLabel) {
        return find("from OnmsMap as map where map.name = ?", mapLabel);
    }

    /** {@inheritDoc} */
    @Override
    public OnmsMap findMapById(int id) {
        return findUnique("from OnmsMap as map where map.id = ?", id);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<OnmsMap> findMapsByNameAndType(String mapName, String mapType) {
        Object[] values = {mapName, mapType};
        return find("from OnmsMap as map where map.name = ? and map.type = ?", values);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<OnmsMap> findMapsByType(String mapType) {
        return find("from OnmsMap as map where map.type = ?", mapType);
    }
    @Override
    public Collection<OnmsMap> findAutoMaps() {
        return findMapsByType(OnmsMap.AUTOMATICALLY_GENERATED_MAP);
    }

    @Override
    public Collection<OnmsMap> findUserMaps() {
        return findMapsByType(OnmsMap.USER_GENERATED_MAP);
    }

    @Override
    public Collection<OnmsMap> findSaveMaps() {
        return findMapsByType(OnmsMap.AUTOMATIC_SAVED_MAP);    
    }

    /** {@inheritDoc} */
    @Override
    public Collection<OnmsMap> findMapsByGroup(String group) {
        return find("from OnmsMap as map where map.mapGroup = ?", group);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<OnmsMap> findMapsByOwner(String owner) {
        return find("from OnmsMap as map where map.owner = ?", owner);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<OnmsMap> findVisibleMapsByGroup(String group) {
        Object[] values = {OnmsMap.ACCESS_MODE_ADMIN, OnmsMap.ACCESS_MODE_USER,OnmsMap.ACCESS_MODE_GROUP,group};
        return find("from OnmsMap as map where map.accessMode = ? or map.accessMode = ? or " +
        		"(map.accessMode = ? and map.mapGroup = ?)", values);
    }

    @Override
    public Collection<OnmsMap> findAutoAndSaveMaps() {
        Object[] values = {OnmsMap.AUTOMATIC_SAVED_MAP, OnmsMap.AUTOMATICALLY_GENERATED_MAP};
        return find("from OnmsMap as map where map.type = ? or map.type = ? ", values);
    }
    
    /** {@inheritDoc} */
    @Override
    public int updateAllAutomatedMap(final Date time) {
        return getJpaTemplate().execute(
            new JpaCallback<Integer>() {
                @Override
                public Integer doInJpa(EntityManager em) {
                    String jql = "update OnmsMap as map set map.lastModifiedTime = :time where map.type = :type";
                    return em.createQuery(jql)
                            .setParameter("time", time)
                            .setParameter("type", OnmsMap.AUTOMATICALLY_GENERATED_MAP)
                            .executeUpdate();
                }
        }).intValue();
    }
}
