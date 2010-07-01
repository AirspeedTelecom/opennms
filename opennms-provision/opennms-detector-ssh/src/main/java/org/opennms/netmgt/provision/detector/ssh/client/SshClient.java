/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2008 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
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
 * OpenNMS Licensing       <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 */
package org.opennms.netmgt.provision.detector.ssh.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.opennms.netmgt.provision.detector.ssh.request.NullRequest;
import org.opennms.netmgt.provision.detector.ssh.response.SshResponse;
import org.opennms.netmgt.provision.support.Client;
import org.opennms.netmgt.provision.support.ssh.SshMonitor;

/**
 * <p>SshClient class.</p>
 *
 * @author thedesloge
 * @version $Id: $
 */
public class SshClient implements Client<NullRequest, SshResponse> {
    
    private boolean m_isAvailable = false;
    private Map<String, Object> m_parameters = new HashMap<String, Object>();
    
    /**
     * <p>close</p>
     */
    public void close() {
        
    }

    /** {@inheritDoc} */
    public void connect(InetAddress address, int port, int timeout) throws IOException, Exception {
        SshMonitor m = new SshMonitor();
        m_parameters.put("port", port);
        
        m_isAvailable = m.poll(address, m_parameters).isAvailable();
    }

    /**
     * <p>receiveBanner</p>
     *
     * @return a {@link org.opennms.netmgt.provision.detector.ssh.response.SshResponse} object.
     * @throws java.io.IOException if any.
     * @throws java.lang.Exception if any.
     */
    public SshResponse receiveBanner() throws IOException, Exception {
        SshResponse response = new SshResponse();
        response.receive(m_isAvailable);
        return response;
    }

    /**
     * <p>sendRequest</p>
     *
     * @param request a {@link org.opennms.netmgt.provision.detector.ssh.request.NullRequest} object.
     * @return a {@link org.opennms.netmgt.provision.detector.ssh.response.SshResponse} object.
     * @throws java.io.IOException if any.
     * @throws java.lang.Exception if any.
     */
    public SshResponse sendRequest(NullRequest request) throws IOException, Exception {
        return null;
    }
    
    /**
     * <p>setBanner</p>
     *
     * @param banner a {@link java.lang.String} object.
     */
    public void setBanner(String banner) {
        m_parameters.put("banner", banner);
    }
    
    /**
     * <p>setMatch</p>
     *
     * @param match a {@link java.lang.String} object.
     */
    public void setMatch(String match) {
        m_parameters.put("match", match);
    }
    
    /**
     * <p>setClientBanner</p>
     *
     * @param clientBanner a {@link java.lang.String} object.
     */
    public void setClientBanner(String clientBanner) {
        m_parameters.put("client-banner", clientBanner);
    }

}
