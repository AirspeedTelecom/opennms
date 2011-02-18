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
package org.opennms.jicmp.jna;

import static org.junit.Assert.assertEquals;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * NativeSocketTest
 *
 * @author brozow
 */
public class NativeSocketTest {
    
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    
    Server m_server;
    
    
    @Before
    public void setUp() throws Exception {
        m_server = new Server(7777);
        m_server.start();
        m_server.waitForStart();
    }
    
    @After
    public void tearDown() throws InterruptedException {
        m_server.stop();
    }
    
    public void printf(String fmt, Object... args) {
        System.err.print(String.format(fmt, args));
    }
    
    @Test
    public void testServer() throws Exception  {
        String[] cmds = new String[] { "echo", "echo2", "quit" };
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        
            for(String cmd : cmds) {
                
                printf("Sending cmd: %s\n", cmd);

                byte[] data = cmd.getBytes("UTF-8");

                DatagramPacket p = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 7777);

                socket.send(p);

                DatagramPacket r = new DatagramPacket(new byte[128], 128);
                socket.receive(r);
                
                String response = new String(r.getData(), r.getOffset(), r.getLength(), "UTF-8");
                
                printf("Received Response: %s from %s:%d\n", response, r.getAddress().getHostAddress(), r.getPort());

                assertEquals(cmd, response);
            }
        
        } finally {
            if (socket != null) socket.close();
        }
    }
    
    @Test
    public void testNativeV4() throws Exception {
        testNative(NativeDatagramSocket.PF_INET, InetAddress.getByName("127.0.0.1"));
    }

    @Test
    public void testNativeV6() throws Exception {
        testNative(NativeDatagramSocket.PF_INET6, InetAddress.getByName("::1"));
    }

    private void testNative(int family, InetAddress address) throws Exception {
        String[] cmds = new String[] { "nativeEcho", "nativeEcho2", "quitNative" };
        NativeDatagramSocket socket = null;

        try {
            socket = NativeDatagramSocket.create(family, NativeDatagramSocket.SOCK_DGRAM,
                    NativeDatagramSocket.IPPROTO_UDP);

            for(String cmd : cmds) {

                printf("Sending cmd: %s\n", cmd);
                
                ByteBuffer buf = UTF_8.encode(cmd);

                NativeDatagramPacket p = new NativeDatagramPacket(buf, address, 7777); 

                socket.send(p);

                NativeDatagramPacket r = new NativeDatagramPacket(128);
                socket.receive(r);
                
                String response = UTF_8.decode(r.getContent()).toString();

                printf("Received Response: %s from %s:%d\n", response, r.getAddress().getHostAddress(), r.getPort());

                assertEquals(cmd, response);
            }

        } finally {
            if (socket != null) socket.close();
        }
    }
    
    @Test(timeout=10000)
    @Ignore("This is ignored since I haven't found a way to interrupt a socket blocked on recvfrom in linux")
    public void testCloseInReceive() throws Exception {
        final NativeDatagramSocket socket = NativeDatagramSocket.create(NativeDatagramSocket.PF_INET, NativeDatagramSocket.SOCK_DGRAM,
                NativeDatagramSocket.IPPROTO_UDP);
        
        Thread t = new Thread("Listener") {
            public void run() {
                try {
                    while(true) {
                        NativeDatagramPacket r = new NativeDatagramPacket(128);
                        System.err.println("About to receive");
                        socket.receive(r);
                        System.err.println("Returned from receive");
                        String response = UTF_8.decode(r.getContent()).toString();

                        printf("Received Response: %s from %s:%d\n", response, r.getAddress().getHostAddress(), r.getPort());
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        
        ByteBuffer buf = UTF_8.encode("msg1");

        NativeDatagramPacket p = new NativeDatagramPacket(buf, InetAddress.getLocalHost(), 7777); 

        socket.send(p);
        
        Thread.sleep(1000);
        
        socket.close();
        
        t.join();
        
        
    }

}
