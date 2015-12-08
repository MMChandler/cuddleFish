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

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;


@SuppressWarnings("serial")
public class Client extends Panel implements Runnable {
		
	static TextField tf = new TextField("Type a message and hit [return].");
	static TextField name = new TextField("Type your name before chatting (no [return])");
	private TextArea ta = new TextArea();
	
	private Socket socket;
	
	private DataOutputStream dOut;
	private DataInputStream dIn;
	
//	static String encryptedString;
//	static String IV = "1234567890123456";
//	static String encryptionKey = "0987654321123456";
	

	
	public Client (String host, int port) {
		
		//set up window
		setLayout (new BorderLayout());
		//add tf2 name, modify "message" to include to include name
		
		add ("North", name);
		add ("South", tf);
		add ("Center", ta);
		//add (nLabel, BorderLayout.PAGE_START);
		//send and receive when someone types and hits return
		//using anonymous class as a callback
		tf.addActionListener ( new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				processMessage (e.getActionCommand());
			}
		});
//		tf2.addActionListener ( new ActionListener() {
//			public void actionPerformed (ActionEvent e) {
//				processMessage (e.getActionCommand());
//			}
//		});
		
		
		//connect to server
		try {
			socket = new Socket (host, port);
			System.out.println("Connected to " + socket);
			
			//get streams and make them into data-in and -out streams:
			dIn = new DataInputStream(socket.getInputStream());
			dOut = new DataOutputStream(socket.getOutputStream());
			
			//new background thread for receiving
			new Thread(this).start();

		}catch (IOException ie) {System.out.println(ie);}
	}
	//called when user types something:
	private void processMessage (String message) {
		try {
			
			byte[] sent = Crypt.encrypt ((name.getText() + ": " + message), Crypt.getEncryptionKey());
			//send to server
			dOut.writeInt (sent.length);
			dOut. write (sent);
			//clear text input field
			tf.setText("");
			
		}catch (IOException ie) {System.out.println(ie);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//background thread to show messages from other window
	public void run() {
		try {
			//continue receiving messages one by one
			while (true) {
			
				//get next message
				int byteCount = dIn.readInt();
				byte[] msg = new byte[byteCount];
				dIn.readFully(msg);
				String message = Crypt.decrypt(msg, Crypt.getEncryptionKey());
				//...and print it
				ta.append(message + "\n");
				tf.setText("");
			}
		}catch (IOException ie) {System.out.println(ie);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
