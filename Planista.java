package planista;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class Planista {
	
	private static double[] dajTabliceZapotrzebowania(LinkedList<Proces> kolejka) { // Trzeba pamiętać tą tablicę i odnawiać kolejkę po każdym obsłużeniu poszczególnej strategii
		double[] wynik = new double[kolejka.size()];
		int i = 0;
		for (Proces proces : kolejka) {
			wynik[i] = proces.dajZapotrzebowanie();
			i++;
		}
		return wynik;
	}
	
	private static void odnówKolejkę(double[] zapotrzebowanie, LinkedList<Proces> kolejka) {
		int i = 0;
		for (Proces proces : kolejka) {
			proces.przywrócDoStanuPierwotnego(zapotrzebowanie[i]);
			i++;
		}
	}
	
	private static void wypisz(LinkedList<Proces> kolejka) {
		for (Proces proces : kolejka) {
			System.out.println("ID: " + proces.dajID());
			System.out.println("Z : " + proces.dajZapotrzebowanie());
		}
	}
	
	private static Collection<Strategia> stwórzKolekcję(LinkedList<Proces> kolejka, int[] parametry_q) {
		ArrayList<Strategia> kolekcja = new ArrayList<Strategia>(parametry_q.length +4);
		kolekcja.add(new FCFS((LinkedList<Proces>)kolejka.clone()));
		kolekcja.add(new SJF((LinkedList<Proces>)kolejka.clone()));
		kolekcja.add(new SRT((LinkedList<Proces>)kolejka.clone()));
		kolekcja.add(new PS((LinkedList<Proces>)kolejka.clone()));
		for (int i = 0; i < parametry_q.length; i++) {
			kolekcja.add(new RR((LinkedList<Proces>)kolejka.clone(), parametry_q[i]));
		}
		return kolekcja;
		}
	
	public static void main(String args[]) throws IOException {
		DaneWejściowe dane;
		if (args.length == 0) {
			dane = ObsługaWejścia.obsłużWejście(null);
		}
		else {
			dane = ObsługaWejścia.obsłużWejście(args[0]);
		}
		Collections.sort(dane.dajKolejkę(), new PorównywanieMomentuPojawienia());
		double[] zapotrzebowanie = dajTabliceZapotrzebowania(dane.dajKolejkę());
		Collection<Strategia> kolekcja = stwórzKolekcję(dane.dajKolejkę(), dane.dajParametry_q());
		for (Strategia strategia : kolekcja) {
			strategia.obsłużStrategię();
			strategia.wypisz_sie();
			odnówKolejkę(zapotrzebowanie, dane.dajKolejkę());
		}
	}
}
