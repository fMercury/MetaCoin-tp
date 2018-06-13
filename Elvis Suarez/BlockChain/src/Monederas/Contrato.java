package Monederas;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import Firma.GeneradorDeClaves;
import Moneda.ZinCoin;

public class Contrato extends Cuenta{

	private String privateKey;
	private String nombre;
	private ZinCoin sistema;
	private GeneradorDeClaves generador;
	
	@Override
	public String getNombre() {
		return this.nombre;
	}

	@Override
	public String getClave() {
		String clave = this.privateKey;
		return clave;
	}

	@Override
	public PublicKey getClavePublica() {
		KeyPair keys = this.generador.getClaves(this.privateKey);
		PublicKey k = keys.getPublic();
		return k;
	}

	@Override
	public void generarClaves() {
		try {
			this.generador = new GeneradorDeClaves();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		this.generador.generarClave(this.privateKey);		
	}
	
	public void crearContrato(){
		
	}

}
