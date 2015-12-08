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

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Cipher;

public class Crypt {
	static String IV = "1234567890123456";
	static String plaintext;
	static String encryptionKey = "0987654321123456";

	
		public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
			plainText = (Client.name.getText() + ": " + Client.tf.getText());
			Cipher cipher = Cipher.getInstance ("AES/CBC/PKCS5Padding", "SunJCE");
			SecretKeySpec key = new SecretKeySpec (encryptionKey.getBytes("UTF-8"), "AES");
			cipher.init (Cipher.ENCRYPT_MODE, key, new IvParameterSpec (IV.getBytes ("UTF-8")));
			return cipher.doFinal (plainText.getBytes("UTF-8"));
		}
		public static String decrypt (byte[] cipherText, String encryptionKey) throws Exception {
			Cipher cipher = Cipher.getInstance ("AES/CBC/PKCS5Padding", "SunJCE");
			SecretKeySpec key = new SecretKeySpec (encryptionKey.getBytes("UTF-8"), "AES");
			cipher.init (Cipher.DECRYPT_MODE, key, new IvParameterSpec (IV.getBytes("UTF-8")));
			return new String (cipher.doFinal(cipherText), "UTF-8");
		}
		public static String getEncryptionKey (){
			return encryptionKey;
		}
	
	}