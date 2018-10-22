package planista;
import java.util.*;
public class PS extends Strategia {
	private int liczba_aktualnych_procesów;
	private LinkedList<Proces> aktualne_procesy;
	
	public PS(LinkedList<Proces> kolejka_procesów) {
		this.kolejka_procesów = kolejka_procesów;
		this.aktualne_procesy = stwórzListęPoczątkowychProcesów(kolejka_procesów);
		this.szybkość_działania_procesora = 1.0/aktualne_procesy.size();
		this.zakończone_procesy = new LinkedList<Proces>();
		this.liczba_aktualnych_procesów = aktualne_procesy.size();
		nazwa = "PS";
	}
	
	public int dajLiczbeAktualnychProcesów() {
		return liczba_aktualnych_procesów;
	}
	
	public LinkedList<Proces> stwórzListęPoczątkowychProcesów(LinkedList<Proces> kolejka_procesów) { 
		LinkedList<Proces> początkowe = new LinkedList<Proces>(); // dodajemy wszystkie procesy które pojawił← się w chwili '0'
		for (Proces proces : kolejka_procesów) {
			if (proces.dajMomentPojawienia() == 0) {
				początkowe.add(proces);
			}
		}
		for (Proces proces : początkowe) {
			kolejka_procesów.remove(proces);
		}
		Collections.sort(początkowe, new PorównywanieZapotrzebowania());
		return początkowe;
	}
	
	
	private boolean dostatecznieBliskoZera(double a) {
		return a <= 0.00005 && a >= -0.00005; // Metoda używana, aby uniknąć błędów arytmetyki zmiennoprzecinkowej, epsilon subiektywny
	}
	
	private boolean usuńZakończonyProces() {
		for (Proces proces : aktualne_procesy) {
			if (dostatecznieBliskoZera(proces.dajZapotrzebowanie())) {
				dodajProcesDoZakończonych(proces);
				return aktualne_procesy.remove(proces);
			}
		}
		return false;
	}
	
	private void dodajDoAktualnychProcesów(Proces proces) {
		aktualne_procesy.add(proces);
	}
	
	private void zmniejszZapotrzebowanieDlaAktualnychProcesów(double x) {
		for (Proces proces : aktualne_procesy) {
			proces.zmniejszZapotrzebowanie(x);
		}
	}
	
	private void zwiększCzasWykonywaniaDlaAktualnychProcesów(double x) {
		for (Proces proces : aktualne_procesy) {
			proces.zwiększCzasWykonywania(x);
		}
	}
	
	private double ustalKwantCzasu(Proces proces) {
		if (proces == null || szybkość_działania_procesora <= proces.dajZapotrzebowanie()) {
			return 1.0;
		}
		else {
			return proces.dajZapotrzebowanie()/szybkość_działania_procesora;
		}
	}
	
	private Proces dajPierwszyAktualnyProces() {
		return aktualne_procesy.getFirst();
	}
	
	
	
	@Override
	public void obsłużStrategię() {
		PorównywanieZapotrzebowania porównywator = new PorównywanieZapotrzebowania();
		while (!isEmpty(aktualne_procesy) || !isEmpty(kolejka_procesów)) {
			double czas_działania = ustalKwantCzasu(dajPierwszyAktualnyProces());
			zmniejszZapotrzebowanieDlaAktualnychProcesów(czas_działania*szybkość_działania_procesora);
			zwiększCzasWykonywaniaDlaAktualnychProcesów(czas_działania);
			aktualny_czas += czas_działania;
			while (usuńZakończonyProces()) liczba_aktualnych_procesów--;
			while (!isEmpty(kolejka_procesów) && dajElementKolejki(0).dajMomentPojawienia() <= dajAktualnyCzas()) {
				dodajDoAktualnychProcesów(dajElementKolejki(0));
				usuńProcesZKolejki(dajElementKolejki(0));
				liczba_aktualnych_procesów++;
			}
			Collections.sort(aktualne_procesy, porównywator);
			szybkość_działania_procesora = 1.0/liczba_aktualnych_procesów;
		}
		
	}
	
	@Override
	public void obsłużProces(Proces proces) {} // Te dwie metody są zbędne w tej strategii

	@Override
	public Proces wybierzProces() {
		return null;
	}

}
