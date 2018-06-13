package Moneda;

import Monederas.Corriente;
import Operaciones.Transaccion;

public abstract class Token {
	
	public abstract void addUser(Corriente c);
	public abstract String getNombre();
	public abstract void entregarTransaccion(Transaccion t);
}
