package planista;

import java.util.*;

public class FCFS extends Strategia {

	public FCFS(LinkedList<Proces> kolejka_procesów) {
		super(1.0, kolejka_procesów);
		nazwa = "FCFS";
	}
	
	@Override
	public void obsłużProces(Proces proces) {
		int czas_działania = (int)proces.dajZapotrzebowanie()/(int)dajSzybkośćDziałaniaProcesora();
		dodajProcesDoZakończonych(proces);
		aktualny_czas += czas_działania;
		dodajCzasOczekiwania(czas_działania);
		proces.zwiększCzasWykonywania(czas_działania);
		zwiększLiczbęZakończonychProcesów();
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
