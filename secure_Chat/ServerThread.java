/* Secure Client-Server Chat Program,
 * encrypted with AES 128 encryption
 * standard, using the core Java 
 * libraries. Adapted from code found at
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

public class  ServerThread extends Thread {
	private Server server;
	private Socket socket;
	
	public ServerThread (Server server, Socket socket) {
		//save parameters
		this.server = server;
		this.socket = socket; 
		
		start();
	}
	//Runs in a separate thread when start() is called in the constructor:
	public void run() {
		
		try {
			//DataInputStream for communication; DataOutput Stream sends to us
			DataInputStream dIn = new DataInputStream (socket.getInputStream());
			//Continuously receive:
			while (true) {
				//read next msg
				int byteCount = dIn.readInt();
				byte[] bytes = new byte[byteCount];
				dIn.readFully(bytes);
			    //show encryption in console
				System.out.println("Sending AES data: " + bytes.toString());
				//send to all clients
				server.sendToAll(bytes);	
			}
		} catch( EOFException ie ) {
		// This doesn't need an error message
		} catch( IOException ie ) {
		ie.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		// The connection is closed,
		// so have the server deal with it
		server.removeConnection (socket);
		}
	}
}

