package Operaciones;
import java.util.ArrayList;
import java.util.List;

public class Bloque {
	
	//VER COMO PONER EL LIMTE A INGRESOS DE TRANSACCIONES
	private List<Transaccion> transacciones;
	private static int LIMITE = 10;
	
	public Bloque() {
		this.transacciones = new ArrayList<>();
	}
	
	public boolean addTransaction(Transaccion t) {
		if(this.transacciones.size() <= LIMITE) {
			if(this.transacciones.add(t)){
				return true;
			}
		}
		return false;
	}
	
	public List<Transaccion> getTransacciones(){
		List<Transaccion> retorno = this.transacciones;
		return retorno;
	}
	
	public Transaccion getTransaction(int pos) {
		Transaccion t = null;
		if(pos < this.transacciones.size()) {
			t= this.transacciones.get(pos);
		}
		return t;
	}
	
	public double getBalance(String user) {
		double saldo = 0;
		for(Transaccion t : this.transacciones) {
			if(user.equals(t.obtenerUsuarioEmisor())){
				saldo -=t.obtenerCantidad();
			}else if(user.equals(t.obtenerUsuarioReceptor())){
				saldo+=t.obtenerCantidad();
			}
		}
		return saldo;
	}
	
}
