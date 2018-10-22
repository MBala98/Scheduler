package planista;
import java.text.DecimalFormat;
public class Proces {
	private int ID;
	private double zapotrzebowanie;
	private int moment_pojawienia;
	private double czas_oczekiwania;
	private double czas_wykonywania;
	
	public Proces(int ID, double zapotrzebowanie, int moment_pojawienia) {
		this.ID = ID;
		this.zapotrzebowanie = zapotrzebowanie;
		this.moment_pojawienia = moment_pojawienia;
		this.czas_oczekiwania = 0;
		this.czas_wykonywania = 0;
	}
	
	public int dajID() {
		return ID;
	}
	
	public double dajZapotrzebowanie() {
		return zapotrzebowanie;
	}
	
	public int dajMomentPojawienia() {
		return moment_pojawienia;
	}
	
	public double dajCzasOczekiwania() {
		return czas_oczekiwania;
	}
	
	public double dajCzasWykonywania() {
		return czas_wykonywania;
	}
	
	public void zmniejszZapotrzebowanie(double x) {
		zapotrzebowanie -= x;
	}
	
	public void zwiększCzasOczekiwania(double x) {
		czas_oczekiwania += x;
	}
	
	public void zwiększCzasWykonywania(double x) {
		czas_wykonywania += x;
	}
	
	public void przywrócDoStanuPierwotnego(double zapotrzebowanie) {
		czas_oczekiwania = 0;
		czas_wykonywania = 0;
		this.zapotrzebowanie = zapotrzebowanie;
	}
	
	public static double zaokraglijLiczbe(double x) {
		x = Math.round(x * 100);
		x = x/100;
		return x;
	}
	
	public void wypisz_sie() {
		DecimalFormat df = new DecimalFormat("#.00");
		double moment_zakonczenia = czas_oczekiwania + czas_wykonywania + moment_pojawienia;
		System.out.print("["  + ID + " " + moment_pojawienia + " " + df.format(moment_zakonczenia) + "]");
	}
	
}

