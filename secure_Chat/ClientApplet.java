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

import java.applet.*;
import java.awt.*;

@SuppressWarnings("serial")
public class ClientApplet extends Applet {
	
	public void init() {
		String host = getParameter ("host");
		int port = Integer.parseInt(getParameter ("port"));
		setLayout (new BorderLayout());
		add ("Center", new Client (host, port));
	}
}
