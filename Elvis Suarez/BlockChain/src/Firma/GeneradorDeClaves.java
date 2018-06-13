package Firma;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Clase capaz de generar un par de claves RSA y de asegurar adecuadamente la clave privada (mediante un password)
 */
public class GeneradorDeClaves 
{

	//HashMap que almacena el par de claves generado con un password (o key) de identificacion.
	private HashMap<String, KeyPair> claves;

	//generador de claves
	private KeyPairGenerator generador;


	public GeneradorDeClaves() throws NoSuchAlgorithmException
	{
		generador = KeyPairGenerator.getInstance("RSA");
		generador.initialize(1024);
		claves = new HashMap<>(100);
	}

	/**
	 * Genera una par de claves (clave privada y clave publica)
	 * @param password - password con la cual se identificara el par de claves generados
	 */
	public void generarClave(String password)
	{
		claves.put(password, generador.generateKeyPair());
	}
	
	public KeyPair getClaves(String pass) {
		KeyPair keys = this.claves.get(pass);
		return keys;
	}

	public HashMap<String, KeyPair> getClaves() {
		return claves;
	}

	public void setClaves(HashMap<String, KeyPair> claves) {
		this.claves = claves;
	}

	public KeyPairGenerator getGenerador() {
		return generador;
	}

	public void setGenerador(KeyPairGenerator generador) {
		this.generador = generador;
	}



}
