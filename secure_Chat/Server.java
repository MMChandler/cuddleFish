/* Secure Client-Server Chat Program,
 * encrypted with AES 128 encryption
 * standard, using the core Java 
 * libraries. Adapted from code found at:
 * 
 * http://www.cn-java.com/download/data/book/socket_chat.pdf,
 * https://gist.github.com/bricef/2436364, and
 * http://stackoverflow.com/questions/10344132/read-byte-from-server,
 * 
 * for a Senior Project at Maharishi University of Management,
 * Fairfield, Iowa, Fall 2014 Semester
 * 
 * By Michael M. Chandler, BSCS Candidate
 */

package secure_Chat;

import java.io.*;
import java.net.*;
import java.util.*;


public class Server {
	
	
	private ServerSocket ss;
	
	// A mapping from sockets to DataOutputStreams. This will
	// help us avoid having to create a DataOutputStream each time
	// we want to write to a stream.
	@SuppressWarnings("rawtypes")
	private Hashtable outputStreams = new Hashtable();
	
	public Server (int port) throws IOException {
		listen (port);
	}
	
	@SuppressWarnings("unchecked")
	private void listen (int port) throws IOException {
		//create server socket:
		ss = new ServerSocket( port);
		System.out.println("Listening on " + ss);
		//Continue accepting connections forever:
		while (true) {
			
			//next connection:
			Socket s = ss.accept();
			System.out.println("Connection from " + s);
			
			//Data output stream for writing to the other side:
			DataOutputStream dOut = new DataOutputStream (s.getOutputStream());
			//Save dOut for future use:
			outputStreams.put (s, dOut);
			
			//new thread for this connection:
			new ServerThread (this, s);
		}
	}
	// Get an enumeration of all the OutputStreams, one for each client
	// connected to us
	@SuppressWarnings("rawtypes")
	Enumeration getOutputStreams() {
	return outputStreams.elements();
	}
	// Send a message to all clients (utility routine)
	void sendToAll( byte[] bytes )  {
	// We synchronize on this because another thread might be
	// calling removeConnection() and this would screw us up
	// as we tried to walk through the list
	synchronized (outputStreams) {
	// For each client ...
	for (@SuppressWarnings("rawtypes")
	Enumeration e = getOutputStreams(); e.hasMoreElements(); ) {
		// ... get the output stream ...
		DataOutputStream dOut = (DataOutputStream)e.nextElement();
		// ... and send the message
		try {
			
			//byte [] msg = Crypt.encrypt(bytes, Crypt.getEncryptionKey());
			dOut.writeInt (bytes.length);
			dOut.write (bytes);
			} catch( IOException ie ) { System.out.println( ie ); 
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
		// Remove a socket, and it's corresponding output stream, from our
		// list. This is usually called by a connection thread that has
		// discovered that the connectin to the client is dead.
		void removeConnection( Socket s ) {
			// Synchronize so we don't mess up sendToAll() while it walks
			// down the list of all output streamsa
			synchronized( outputStreams ) {
				// Tell the world
				System.out.println( "Removing connection to " + s );
				// Remove it from our hashtable/list
				outputStreams.remove( s );
				// Make sure it's closed
				try {
					s.close();
				} catch( IOException ie ) {
					System.out.println( "Error closing "+s );
					ie.printStackTrace();
				}
			}
		
		}
	
	//Main--use as java Server >port<
	public static void main (String[] args) throws Exception {
		
		//port # from command line:
		int port = Integer.parseInt (args[0]);
		
		//Server object that automatically accepts connections:
		new Server (port);
	}
}
