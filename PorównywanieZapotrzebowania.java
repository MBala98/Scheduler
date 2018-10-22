package planista;

import java.util.Comparator;

public class PorównywanieZapotrzebowania implements Comparator<Proces> {
	@Override
	public int compare(Proces a, Proces b) {
		return a.dajZapotrzebowanie() < b.dajZapotrzebowanie() ? -1 : a.dajZapotrzebowanie() == b.dajZapotrzebowanie() ? 0 : 1;
	}

}
