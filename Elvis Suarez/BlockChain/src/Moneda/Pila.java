package Moneda;

import Operaciones.*;
import java.util.ArrayList;
import java.util.List;

public class Pila {

	private List<Transaccion> lista;
	
	public Pila() {
		this.lista = new ArrayList<>();
	}
	
	public boolean agregar(Transaccion nueva) {
		int pos = 0;
		for(Transaccion t : this.lista) {
			if(t.getPropina() < nueva.getPropina()){
				this.lista.add(pos,nueva);
				return true;
			}
			pos++;
		}
		return false;
	}
	
	public Transaccion getTransaccion() {
		Transaccion t = this.lista.get(0);
		this.lista.remove(0);
		return t;
	}
	
}
