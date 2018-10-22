package planista;

import java.util.Comparator;

public class PorównywanieMomentuPojawienia implements Comparator<Proces> {
	@Override
	public int compare(Proces a, Proces b) {
		return a.dajMomentPojawienia() < b.dajMomentPojawienia() ? -1 : a.dajMomentPojawienia() == b.dajMomentPojawienia() ? 0 : 1;
	}

}