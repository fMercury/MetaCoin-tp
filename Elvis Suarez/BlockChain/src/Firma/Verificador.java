package Firma;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

/**
 * Clase capaz de validar una firma digital
 */
public class Verificador 
{
	
	/**
	 * Metodo encargado de validar una firma
	 */
	
	public boolean validarFirma(String original, String firma, PublicKey clavePublica) throws Exception
	{
		byte[] f = firma.getBytes();
		byte[] datos = original.getBytes();
		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initVerify(clavePublica);
		signature.update(datos);
		
		return signature.verify(f);
	}

}
