package model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.jasypt.util.text.StrongTextEncryptor;

public class PasswordManager {

	private static PasswordManager _instance = null;

	private String salt;
	private final int DEFAULT_SIZE = 128;
	private StrongTextEncryptor manager;

	private PasswordManager() throws IOException, NoSuchAlgorithmException {
		this.manager = new StrongTextEncryptor();
		// get encryption key from config file
		FileReader reader;
		Properties p = new Properties();
		String path = System.getProperty("user.dir");
		reader = new FileReader(path + "/src/config.properties");
		p.load(reader); 
		// set encryptor's key
		setSalt(p.getProperty("saltKey"));
		this.manager.setPassword(this.salt);
		//save key to config file
		//if key was read from file - it will stay the same, otherwise a new key will be set.
		p.setProperty("saltKey", salt);
	    p.store(new FileWriter(path + "/src/config.properties"),"Properties Data");
		reader.close();
	}

	private void setSalt(String property) throws NoSuchAlgorithmException {
		if (property.isEmpty())
		{
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			generator.init(DEFAULT_SIZE);
			SecretKey key = generator.generateKey();
			byte[] saltBytes = key.getEncoded();
			this.salt = Base64.getEncoder().encodeToString(saltBytes);
		}
		else
			this.salt = property;
	}

	public static PasswordManager getInstance() throws IOException, NoSuchAlgorithmException {
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
