package Operaciones;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;

import Firma.Firmador;

public class Cambio extends Operacion{
	
	private String original;
	private String transaccion;
	
	public Cambio() {
		this.original = "";
		this.transaccion = "";
	}
	
	public void firmar(PrivateKey claveP,String operacion){
		this.original = operacion;
		Firmador firmar = new Firmador();
		String resultado = "";
		try {
			byte[] datos = firmar.firmarArchivo(claveP,operacion);
			resultado = datos.toString();
		} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
			e.printStackTrace();
		}
		this.transaccion = resultado;
	}
	
	public String getOriginal() {
		String or = this.original;
		return or;
	}
	
	public String getTransaccion() {
		String tran = this.transaccion;
		return tran;
	}
	
	public String obtenerUsuarioEmisor(){
		String operacion = this.original;
		String user = "";
		int pos = 0;
		while(operacion.charAt(pos) != '-') {
			user+=operacion.charAt(pos);
			pos+=1;
		}
		return user;		
	}
	
	public double obtenerCantidad() {
		String operacion = this.original;
		double saldo = 0;
		String cant = "";
		int pos = 0;
		while(operacion.charAt(pos) != '-') {
			pos+=1;
		}
		pos+=1;
		while(pos <= operacion.length()) {
			cant+=operacion.charAt(pos);
			pos+=1;
		}
		saldo = Double.parseDouble(cant);
		return saldo;
	}

}
