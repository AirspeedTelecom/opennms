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
 * Created: November 17, 2006
 *
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
package org.opennms.web.svclayer;

/**
 * <p>SurveillanceViewError class.</p>
 *
 * @author <a href="mailto:jeffg@opennms.org">Jeff Gehlbach</a>
 * @version $Id: $
 * @since 1.8.1
 */
public class SurveillanceViewError {
	
	private String m_shortDescr;
	private String m_longDescr;

	/**
	 * <p>getShortDescr</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getShortDescr() {
		return m_shortDescr;
	}

	/**
	 * <p>setShortDescr</p>
	 *
	 * @param shortDescr a {@link java.lang.String} object.
	 */
	public void setShortDescr(String shortDescr) {
		m_shortDescr = shortDescr;
	}
	
	/**
	 * <p>getLongDescr</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getLongDescr() {
		return m_longDescr;
	}

	/**
	 * <p>setLongDescr</p>
	 *
	 * @param longDescr a {@link java.lang.String} object.
	 */
	public void setLongDescr(String longDescr) {
		m_longDescr = longDescr;
	}
	
}
