package Moneda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Operaciones.*;
import javafx.util.Pair;

public class CasaCambio {

	private List<Pair<String, Pair<String,Cambio>>> intercambios;
	private Map<String,Token> criptomonedas;
	
	public CasaCambio() {
		this.intercambios = new ArrayList<>();
	}
	
	public void cambiar(String tokenOrigen,String token,Cambio c){
		Pair<String,Cambio> par = new Pair(token,c);
		Pair<String,Pair<String,Cambio>> par2 = new Pair(tokenOrigen,par);
		this.intercambios.add(par2);
	}
	
	public Cambio buscarIntercambio(String token,double minimo) {
		Cambio c = null;
		
		for(Pair<String,Pair<String,Cambio>> par : this.intercambios){
			if(par.getKey().equals(token)) {
				Pair<String,Cambio> var = par.getValue(); 
				if(var.getValue().obtenerCantidad() >= minimo) {
					return var.getValue();
				}
			}
		}
		return c;
	}	
	
	public void intercambiar(String tokenDestino1,String tokenDestino2,Cambio c1,Cambio c2) {
		Transaccion nueva = new Transaccion(c1.getTransaccion(),c1.getOriginal()+" "+c2.obtenerUsuarioEmisor());		
		Transaccion nueva2 = new Transaccion(c2.getTransaccion(),c2.getOriginal()+" "+c1.obtenerUsuarioEmisor());
		this.criptomonedas.get(tokenDestino1).entregarTransaccion(nueva);
		this.criptomonedas.get(tokenDestino2).entregarTransaccion(nueva2);
	}
	
}
