/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2010 The OpenNMS Group, Inc.  All rights reserved.
 * OpenNMS(R) is a derivative work, containing both original code, included code and modified
 * code that was published under the GNU General Public License. Copyrights for modified
 * and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
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
package org.opennms.jicmp;

import java.net.Inet4Address;
import java.net.InetAddress;

import org.opennms.jicmp.v6.V6Pinger;


/**
 * Main
 *
 * @author brozow
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("java -jar jna-jicmp-VERSION.jar <hostname or ip address>");
            System.exit(1);
        }
        new Main().ping(args[0]);
    }
    
    public void ping(String hostname) throws Exception {

        InetAddress host = InetAddress.getByName(hostname);
        
        Pinger listener = host instanceof Inet4Address ? new V4Pinger() : new V6Pinger();
        listener.start();
        
        listener.ping(host, 12345, 10, 1000);
        
        listener.stop();
    }
    
}
