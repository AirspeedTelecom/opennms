/*
 * This file is part of the OpenNMS(R) Application.
 *
 * OpenNMS(R) is Copyright (C) 2011 The OpenNMS Group, Inc.  All rights reserved.
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
package org.opennms.jicmp.jna;

import java.net.InetAddress;
import java.nio.ByteBuffer;

/**
 * NativeDatagramPacketBase
 *
 * @author brozow
 */
public class NativeDatagramPacket {

    private ByteBuffer m_data;
    private InetAddress m_address;
    private int m_port;
    
    public NativeDatagramPacket(ByteBuffer data, InetAddress address, int port) {
        m_data = data;
        m_address = address;
        m_port = port;
    }
    
    public NativeDatagramPacket(int size) {
        this(ByteBuffer.allocate(size), null, -1);
    }
    
    public NativeDatagramPacket(byte[] data, InetAddress host, int port) {
        this(ByteBuffer.wrap(data), host, port);
    }

    public InetAddress getAddress() {
        return m_address;
    }

    public void setAddress(InetAddress addr) {
        m_address = addr;
    }

    public int getPort() {
        return m_port;
    }

    public void setPort(int port) {
        m_port = port;
    }

    public int getLength() {
        return m_data.limit();
    }

    public void setLength(int length) {
        m_data.limit(length);
    }

    public ByteBuffer getContent() {
        return m_data.duplicate();
    }

    @Override
    public String toString() {
    
        StringBuilder buf = new StringBuilder();
        
        buf.append("Address: ");
        buf.append(m_address);
        buf.append(" Port: ");
        buf.append(m_port);
        buf.append("\nData: ");
        
        ByteBuffer data = m_data.duplicate();
        
        buf.append(data.limit());
        buf.append(" Bytes\n");
        
        final int bytesPerRow = 4;
        final int limit = data.limit();
        final int rows = (limit + bytesPerRow) / bytesPerRow; 
        int index = 0;
        for(int i = 0; i < rows && index < limit; i++) {
            for(int j = 0; j < bytesPerRow && index < limit; j++) {
                buf.append(String.format("%02X", data.get(index++)));
            }
            buf.append("\n");
        }
        
        buf.append("\n");
            
        return buf.toString();
    }

}
