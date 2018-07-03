package Firma;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;

/**
 * Clase que firma documentos 
 */

public class Firmador{
		
	public byte[] firmarArchivo(PrivateKey clavePrivada,String transaccion) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		byte[] data = transaccion.getBytes();
		Signature dsa = Signature.getInstance("SHA1withRSA"); 
		dsa.initSign(clavePrivada);
		dsa.update(data);
		return data;
	}

	
}
