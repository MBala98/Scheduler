package planista;
import java.text.DecimalFormat;
import java.util.*;
public class RR extends Strategia {
	private int parametr_q;
	
	public RR(LinkedList<Proces> kolejka_procesów, int q) {
		super(1.0, kolejka_procesów);
		parametr_q = q;
		nazwa = "RR";
	}

	public int dajParametrQ() {
		return parametr_q;
	}
	
	private int min(int a, int b) {
		if (a < b) return a;
		else return b;
	}
	
	@Override
	public void wypisz_sie() { // Nadpisuje metodę, gdyż potrzebuję, aby wypisywała RR-q
		DecimalFormat df = new DecimalFormat("#.00");
		System.out.println("Strategia: " + nazwa + "-" + parametr_q);
		for (Proces proces : zakończone_procesy) {
			proces.wypisz_sie();
		}
		System.out.println();
		System.out.println("Średni czas obrotu: " + "" + df.format(policzŚredniCzasObrotu()));
		System.out.println("Średni czas oczekiwania: " + "" + df.format(policzŚredniCzasOczekiwania()));
		System.out.println();
	}
	
	private void dodajDoKolejkiWzgledemCzasu(Proces proces) {
		int rozmiar = kolejka_procesów.size();
		int i = 0;
		while (i < rozmiar && dajElementKolejki(i).dajMomentPojawienia() < aktualny_czas) 
			i++;
		kolejka_procesów.add(i, proces);
	}
	
	@Override
	public void obsłużProces(Proces proces) {
		int czas_działania = min((int)proces.dajZapotrzebowanie(), parametr_q);
		aktualny_czas += czas_działania;
		dodajCzasOczekiwania(czas_działania);
		proces.zmniejszZapotrzebowanie(czas_działania);
		proces.zwiększCzasWykonywania(czas_działania);
		if (proces.dajZapotrzebowanie() == 0) dodajProcesDoZakończonych(proces);
		else dodajDoKolejkiWzgledemCzasu(proces);
	}

	@Override
	public Proces wybierzProces() {
		return kolejka_procesów.removeFirst();
	}
	
	@Override
	public void obsłużStrategię() {
		while (!isEmpty(kolejka_procesów)) {
			Proces proces = wybierzProces();
			if (proces.dajMomentPojawienia() > aktualny_czas)
				aktualny_czas = (double)proces.dajMomentPojawienia();
			obsłużProces(proces);
		}
	}

}
