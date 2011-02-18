package org.opennms.netmgt.scriptd.ins.events;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.xml.event.Event;




/**
 * The InsServerListener will accept input from a socket, create a InsSession in
 * which communicate with an InsClient for the alarm synchronization.
 * 
 * @see main method for usage example 
 */

public class InsServerListener extends InsServerAbstractListener {
	
	private ServerSocket listener;
	
	private Set<InsSession> activeSessions=new HashSet<InsSession>();

	/**
	 * listens for incoming connection on defined port (default is 8154)
	 */
	public void run() {
	    ThreadCategory log = getLog();
		if(criteriaRestriction ==null)
			throw new IllegalStateException("The property criteriaRestriction cannot be null!");
		log.info("InsServerListener started: listening on port "+listeningPort);
		try {
			listener = new ServerSocket(listeningPort);
			Socket server;

			while (true) {
				// when accepts an incoming connection, create an InsSession for
				// alarms exchange
				server = listener.accept();
				InsSession session = new InsSession(server);
				//only if the sharedASCIIString is valorized, requires authentication
				if(sharedAuthAsciiString!=null)
					session.setSharedASCIIString(sharedAuthAsciiString);
				session.setCriteriaRestriction(criteriaRestriction);
				session.start();
				activeSessions.add(session);
			}
		} catch (IOException ioe) {
			log.info("Socket closed." );
		}
	}

	@Override
	/**
	 * Stops the listener
	 */
	public void interrupt() {
        ThreadCategory log = getLog();
		try {
			listener.close();
		} catch (IOException e) {
		    log.error("Gor Error closing listener: " + e.getLocalizedMessage());
		}
		super.interrupt();
        log.info("InsServerListener Interrupted!");
	}
	
	private synchronized void cleanActiveSessions(){
        ThreadCategory log = getLog();
		synchronized (activeSessions){
			Iterator<InsSession> it = activeSessions.iterator();
			while(it.hasNext()){
				InsSession insSession = it.next();
				if(insSession==null || !insSession.isAlive()){
					log.debug("removing "+insSession);
					it.remove();
				}
			}
		}
		log.debug("active sessions are: "+activeSessions);
	}

	/**
	 * Flushes the event in input to all active sessions with clients
	 * @param event
	 */
	public void flushEvent(Event event){
	      ThreadCategory log = getLog();
	      log.debug("Flushing uei: "+event.getUei());
	      log.debug("Flushing ifindex: " + event.getIfIndex());
	      log.debug("Flushing ifAlias: " + event.getIfAlias());
	      
		synchronized (activeSessions){
			cleanActiveSessions();
			Iterator<InsSession> it = activeSessions.iterator();
			while(it.hasNext()){			
				InsSession insSession = it.next();
				PrintStream ps = insSession.getStreamToClient();
				synchronized (ps) {
					if(ps!=null){
						try {
							event.marshal(new PrintWriter(ps));
						} catch (Throwable e) {
							log.error("Error while sending current event to client"+e);
						}
					}	
				}
				
			}
		}
	}

	public static void main(String[] args) {
		InsServerListener isl = new InsServerListener();
		isl.setListeningPort(8155);
		//optional (if not setted, no authentication is required)
		isl.setSharedASCIIString("1234567890");
		
		isl.setCriteriaRestriction("eventuei is not null");
		//required properties
		
		
		isl.start();

	}

}


