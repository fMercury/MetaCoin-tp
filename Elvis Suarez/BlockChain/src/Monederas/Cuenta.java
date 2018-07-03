package Monederas;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import Firma.GeneradorDeClaves;

public abstract class Cuenta {
		
	public abstract String getNombre();
	
	public abstract String getClave();
	
	public abstract PublicKey getClavePublica();
	
	public abstract void generarClaves();

}
