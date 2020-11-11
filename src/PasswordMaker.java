import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**Class for generating hashes from plaintext passwords and verifying them.
 * This class DOES NOT store or retrieve passwords.
 * Each password is generated using a specific hashing algorithms and variables defined within 
 * this class.
 * When a user creates a password for the first time, they will enter a plaintext password to be registered.
 * For security, the plaintext password should be hashed, and the hash is stored instead.
 * @author Lai Ming Hui
 * @since 23/10/2020
 * @version 1.0
 */

public class PasswordMaker {

	private final static SecureRandom sr = new SecureRandom();
	
	//Parameters for hashing.
	private final static int iterations = 65536; //any big number
	private final static int keyLength = 256;
	private final static int saltLength = 32;
	private final static String algorithm = "PBKDF2WithHmacSHA512";


	/**Generates salt for stronger hashing.
	 * @param length: Length of the returned byte array.
	 *@return A randomly generated byte array
	 */
	private static byte[] generateSalt(int length)
	{
		byte[] salt = new byte[length];
		sr.nextBytes(salt);
		return salt;
	}

	/**Create a hash from a plaintext string pwd and a salt.
	 * This function only returns the hash of pwd. This hash should not be stored on its own
	 * in a password database. See generatePasswordHash() to generate a complete hash for a plaintext password.
	 * @param length: Length of the returned byte array.
	 *@return A randomly generated byte array
	 */
	private static String hashWithSalt( String pwd, byte[] salt )
	{
		SecretKeyFactory skf = null;
		SecretKey key = null;

		try {
			skf = SecretKeyFactory.getInstance(algorithm);
			KeySpec keyspec = new PBEKeySpec(pwd.toCharArray(),salt, iterations, keyLength);
			key = skf.generateSecret(keyspec);

		} catch (NoSuchAlgorithmException|InvalidKeySpecException  e) {
			e.printStackTrace();
		}

		return Base64.getEncoder().encodeToString( key.getEncoded() );
	}

	/**Verifies an input password with a hashed password stored in database.
	 *@param pwd: Input password from console input 
	 *@param storedString: A string created from hashing a registered password.
	 *@return True if the hash of pwd and storedString is the same.
	 */
	public static boolean verifyPassword(String pwd, String storedString )
	{
		String[] hashAndSalt = storedString.split("\\$");
		String hashed = hashWithSalt(pwd, Base64.getDecoder().decode(hashAndSalt[1]));
		if( hashed.equals(hashAndSalt[0]))return true;
		return false;
	}
		
	/**Generates a random string by hashing String pwd. This function should be used when creating hashes of new passwords. 
	 * (Passwords that has yet to be stored in database.)
	 * This function generates a salt and a hashed string and combines them into a string that should be stored in a database and
	 * used for authentication.
	 * @param pwd: Plain text input from user
	 * @return A randomly generated string to be stored for future authentication.
	 */
	public static String generatePasswordHash(String pwd)
	{
		byte[] salt = generateSalt(saltLength);
		String hashed = hashWithSalt(pwd, salt);
		String encodedSalt = Base64.getEncoder().encodeToString(salt);
		byte[] decoded =  Base64.getDecoder().decode(encodedSalt);
		String finalString = hashed + "$" + encodedSalt;

		return finalString;

	}
	
}