package planista;

import java.text.DecimalFormat;
import java.util.LinkedList;
	
public abstract class Strategia {
	protected double szybkość_działania_procesora;
	protected LinkedList<Proces> kolejka_procesów; // Zakładam, że procesy w kolejce będą posortowane względem czasu pojawienia się, w tej wersji programu sortuję, aby to osiągnąć
	protected LinkedList<Proces> zakończone_procesy;
	protected int liczba_zakończonych_procesów;
	protected double aktualny_czas;
	protected String nazwa;
	
	protected Strategia() {
		
	}
	
	protected Strategia(double szybkość_działania_procesora, LinkedList<Proces> lista_procesów) {
		this.szybkość_działania_procesora = szybkość_działania_procesora;
		this.kolejka_procesów = lista_procesów;
		zakończone_procesy = new LinkedList<Proces>();
		liczba_zakończonych_procesów = 0;
		aktualny_czas = 0;
	}
	
	public void wypisz_sie() {
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println("Strategia: " + nazwa);
		for (Proces proces : zakończone_procesy) {
			proces.wypisz_sie();
		}
		System.out.println();
		System.out.println("Średni czas obrotu: " + "" + df.format(policzŚredniCzasObrotu()));
		System.out.println("Średni czas oczekiwania: " + "" + df.format(policzŚredniCzasOczekiwania()));
		System.out.println();
	}
	
	public Proces dajElementKolejki(int i) {
		return kolejka_procesów.get(i);
	}
	
	public double dajSzybkośćDziałaniaProcesora() {
		return szybkość_działania_procesora;
	}
	
	public void ustalSzybkośćDziałania(double x) {
		szybkość_działania_procesora = x;
	}
	
	public int dajLiczbeZakończonychProcesów() {
		return liczba_zakończonych_procesów;
	}
	
	
	public double dajAktualnyCzas() {
		return aktualny_czas;
	}
	
	public int dajRozmiarKolejki() {
		return kolejka_procesów.size();
	}
	
	public boolean isEmpty(LinkedList<Proces> lista) {
		return lista.size() == 0;
	}
	
	
	public void dodajProcesDoKolejki(Proces proces) {
		kolejka_procesów.add(proces);
	}
	
	public void usuńProcesZKolejki(Proces proces) {
		kolejka_procesów.remove(proces);
	}
	
	public void dodajProcesDoZakończonych(Proces proces) {
		zakończone_procesy.add(proces);
	}
	
	public void dodajCzasOczekiwania(double czas) {
		for (Proces proces : kolejka_procesów) {
			if (proces.dajMomentPojawienia() < aktualny_czas) {
				double roznica_czasow = aktualny_czas - proces.dajMomentPojawienia();
				if (roznica_czasow > czas)
					proces.zwiększCzasOczekiwania(czas);
				else proces.zwiększCzasOczekiwania(roznica_czasow);
			}
		}
	}
	
	public double policzŚredniCzasWykonywania() {
		double suma = 0;
		for (Proces proces : zakończone_procesy) {
			suma += proces.dajCzasWykonywania();
		}
		return suma/zakończone_procesy.size();
	}
	
	public double policzŚredniCzasOczekiwania() {
		double suma = 0;
		for (Proces proces: zakończone_procesy) { 
			suma += proces.dajCzasOczekiwania();
		}
		return suma/zakończone_procesy.size();
	}
	
	public double policzŚredniCzasObrotu() {
		double suma = 0;
		for (Proces proces: zakończone_procesy) {
			suma += proces.dajCzasOczekiwania() + proces.dajCzasWykonywania();
		}
		return suma/zakończone_procesy.size();
	}
	
	public void zwiększLiczbęZakończonychProcesów() {
		liczba_zakończonych_procesów++;
	}
	
	public abstract void obsłużStrategię();
	public abstract void obsłużProces(Proces proces);
	public abstract Proces wybierzProces();
	
}
