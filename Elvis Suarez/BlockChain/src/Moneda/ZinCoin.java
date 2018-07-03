package Moneda;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import Firma.Verificador;
import Monederas.*;
import Operaciones.*;


// AGREGAR LAS MEDIDAS DE CONCURRENCIAS RESPECTIVAS
public class ZinCoin extends Token{

	//Deberia preguntar si hago el HASH de esto
	private List<Bloque> bloques;
	//Debo tener los usuarios para lograr comunicarse entre usuarios
	private List<Cuenta> nodos;
	private Pila pila;
	private CasaCambio banco; // Todos los token deberian tenerlo
	private String nombre;
	
	
	public ZinCoin(String nombre) {
		this.bloques = new ArrayList<>();
		this.nodos = new ArrayList<>();
		this.pila = new Pila();
		this.banco = new CasaCambio();
		this.nombre = nombre;
	}
	
	public String getNombre() {
		String name = this.nombre;
		return name;
	}
	
	public void addUser(Corriente c){
		this.nodos.add(c);
	}
	
	public PublicKey getClavePublica(String usuario) {
		PublicKey keyN = null;
		for(Cuenta c : this.nodos){
			if(c.getNombre().equals(usuario)) {
				PublicKey key = c.getClavePublica();
				return key;
			}
		}
		return keyN;
	}
	
	private double balance(String user) {
		double saldo = 0;
		for(Bloque b : this.bloques) {
			saldo+=b.getBalance(user);
		}
		return saldo;
	}
	
	private boolean balancePositivo(Operacion t){
		String user = t.obtenerUsuarioEmisor();
		double saldo = 0;
		saldo = balance(user);
		if(saldo >= t.obtenerCantidad()){
			return true;
		}
		return false;
	}
	
	private boolean validarFirma(Operacion t){
		Verificador verificar = new Verificador();
		String usuario = t.obtenerUsuarioEmisor();
		boolean resultado = false;
		try {
			resultado = verificar.validarFirma(t.getOriginal(),t.getTransaccion(),getClavePublica(usuario));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultado;
	}
	
	private boolean validarTransaccion(Operacion t) {
		if(balancePositivo(t) && validarFirma(t)) {
			return true;
		}
		return false;
	}
	
	private void informarUsuario(boolean correcto,Transaccion t) {
		String usuario = t.obtenerUsuarioEmisor();
		for(Cuenta c : this.nodos) {
			if(c.getNombre().equals(usuario)){
				Corriente corriente = (Corriente) c;
				corriente.setTransaccion(correcto, t);
			}
		}
	}
	
	private boolean addTransaccion(){
		Transaccion t = this.pila.getTransaccion();
		
		if(validarTransaccion((t))) {
			int pos = this.bloques.size();
			Bloque b = this.bloques.get(pos-1);
			if(!b.addTransaction(t)) {
				Bloque nuevo = new Bloque();
				nuevo.addTransaction(t);
				this.bloques.add(nuevo);
			}
			informarUsuario(true,t);
			return true;
		}
		informarUsuario(false,t);
		return false;
	}
	
	public void entregarTransaccion(Transaccion t){
		this.pila.agregar(t);
		this.addTransaccion();
	}
	
	public void canjear(Cambio c) {
		if(this.validarTransaccion(c)) {
			Transaccion t = new Transaccion(c.getTransaccion(),c.getOriginal());			
			int pos = this.bloques.size();
			Bloque b = this.bloques.get(pos-1);
			if(!b.addTransaction(t)) {
				Bloque nuevo = new Bloque();
				nuevo.addTransaction(t);
				this.bloques.add(nuevo);
			}
			informarUsuario(true,t);
			
			this.banco.cambiar("ZinCoin","Otra moneda",c);
		}
	}
	
}
