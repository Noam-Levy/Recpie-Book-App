package model;

import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Properties;

import org.jasypt.util.text.StrongTextEncryptor;

public class PasswordManager {
	
	private static PasswordManager _instance = null;
	
	private String salt;
	private final int DEFAULT_SIZE = 32;
	private StrongTextEncryptor manager;
	
	private PasswordManager() throws IOException {
		this.manager = new StrongTextEncryptor();
		// get encryption key from config file
		FileReader reader;
		Properties p = new Properties();
		reader = new FileReader("/src/config.properties");
		p.load(reader); 
		reader.close();
		// set encryptor's key
		setSalt(p.getProperty("saltKey"));
		this.manager.setPassword(this.salt);
		//save key to config file
		//if key was read from file - it will stay the same, otherwise a new key will be set.
		p.setProperty("saltKey", salt);
	}

	private void setSalt(String property) {
		if (property.equals(null))
		{
			SecureRandom generator = new SecureRandom();
			byte[] saltBytes = new byte[DEFAULT_SIZE];
			generator.nextBytes(saltBytes);
			this.salt = Arrays.toString(saltBytes).replaceAll(",", "").replaceAll("[", "").replaceAll("]", "");
		}
		this.salt = property;
	}
	
	public static PasswordManager getInstance() throws IOException {
		if (_instance == null)
			_instance = new PasswordManager();
		return _instance;
	}
	
	public String decrypt(String message) {
		return manager.decrypt(message);
	}
	
	public String encrypt(String message) {
		return manager.encrypt(message);
	}

}
