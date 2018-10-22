package planista;
import java.util.*;
public class SRT extends Strategia {
	public SRT(LinkedList<Proces> kolejka_procesów) {
		super(1.0, kolejka_procesów);
		nazwa = "SRT";
	}
	
	
	
	
	
	@Override
	public void obsłużProces(Proces proces) {
		int max = dajRozmiarKolejki();
		int i = 0;
		while (i < max && dajElementKolejki(i).dajMomentPojawienia() <= dajAktualnyCzas())
			i++; // Mozemy pominac wszystkie procesy ktore byly w kolejce w momencie wybrania aktualnie przerabianego procesu
				// dzieki temu jak wybierany jest proces
		while (proces.dajZapotrzebowanie() > 0) {
			aktualny_czas++;
			proces.zmniejszZapotrzebowanie(1);
			dodajCzasOczekiwania(1);
			proces.zwiększCzasWykonywania(1);
			if (i < max && dajElementKolejki(i).dajMomentPojawienia() <= dajAktualnyCzas()) {
				if (dajElementKolejki(i).dajZapotrzebowanie() >= proces.dajZapotrzebowanie()) i++;
				else break;
			}
		}
		if (proces.dajZapotrzebowanie() == 0) {
			dodajProcesDoZakończonych(proces);
		}
		else dodajProcesDoKolejki(proces);
		Collections.sort(kolejka_procesów, new PorównywanieMomentuPojawienia());
	}

	@Override
	public Proces wybierzProces() {
		Proces aktualny = dajElementKolejki(0);
		int i = 1;
		int max = dajRozmiarKolejki();
		while (i < max && dajElementKolejki(i).dajMomentPojawienia() <= dajAktualnyCzas()) {
			Proces kandydat = dajElementKolejki(i);
			if (aktualny.dajZapotrzebowanie() > kandydat.dajZapotrzebowanie()) 
				aktualny = kandydat;
			else if (aktualny.dajZapotrzebowanie() == kandydat.dajZapotrzebowanie() && kandydat.dajID() < aktualny.dajID())
				aktualny = kandydat;
			i++;
		}
		usuńProcesZKolejki(aktualny);
		return aktualny;
	}

	@Override
	public void obsłużStrategię() {
		while (!isEmpty(kolejka_procesów)) {
			Proces proces = wybierzProces();
			obsłużProces(proces);
		}
	}
}
