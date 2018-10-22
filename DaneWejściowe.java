package planista;

import java.util.LinkedList;

public class DaneWejściowe {

	private LinkedList<Proces> kolejka;
	private int[] parametry_q;
	
	DaneWejściowe(LinkedList<Proces> kolejka, int[] parametry_q) {
		this.kolejka = kolejka;
		this.parametry_q = parametry_q;
	}
	
	public LinkedList<Proces> dajKolejkę() {
		return kolejka;
	}
	
	public int[] dajParametry_q() {
		return parametry_q;
	}
}
