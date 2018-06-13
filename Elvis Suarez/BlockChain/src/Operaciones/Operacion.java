package Operaciones;

import java.security.PrivateKey;

public abstract class Operacion {
	
	public abstract void firmar(PrivateKey claveP,String operacion);
	public abstract String getOriginal();
	public abstract String  getTransaccion();
	public abstract String  obtenerUsuarioEmisor();
	public abstract double obtenerCantidad();

}
