package planista;

import java.util.*;

public class SJF extends Strategia {
	
	public SJF(LinkedList<Proces> kolejka_procesów) {
		super(1.0, kolejka_procesów);
		nazwa = "SJF";
	}

	@Override
	public void obsłużProces(Proces proces) {
		int czas_działania = (int)proces.dajZapotrzebowanie()/(int)dajSzybkośćDziałaniaProcesora();
		dodajProcesDoZakończonych(proces);
		aktualny_czas += czas_działania;
		dodajCzasOczekiwania(czas_działania);
		proces.zwiększCzasWykonywania(czas_działania);
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


