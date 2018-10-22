package planista;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;
public class ObsługaWejścia {
	private static final int SPACJA = 32;
	private static final int ZERO = 48;
	private static final int DZIEWIEC = 57;
	
	private static boolean czyLiczba(int x) {
		return x >= ZERO && x <= DZIEWIEC;
	}
	
	private static void obsłużBłąd(String komunikat, BufferedReader reader) throws IOException {
		System.out.println(komunikat);
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}
	
	private static Proces obsłużLinię(String linia, int numer_wiersza, BufferedReader reader) throws IOException {
		String moment_pojawienia, zapotrzebowanie;
		int koniec_liczby = 1;
		int długość = linia.length();
		char znak = linia.charAt(0);
		if (!czyLiczba(znak))
			obsłużBłąd("Błąd w wierszu " + numer_wiersza + " : Moment pojawienia jest błędny", reader);
		while (koniec_liczby < długość && czyLiczba(znak)) {
			znak = linia.charAt(koniec_liczby);
			koniec_liczby++;
		}
		moment_pojawienia = linia.substring(0, koniec_liczby-1);
		if (linia.charAt(koniec_liczby-1) != SPACJA)
			obsłużBłąd("Błąd w wierszu " + numer_wiersza + " : Oczekiwana spacja po momencie pojawienia", reader);
		int początek_zapotrzebowania = koniec_liczby;
		znak = linia.charAt(koniec_liczby);
		if (!czyLiczba(znak))
			obsłużBłąd("Błąd w wierszu " + numer_wiersza + " : Zapotrzebowanie jest błędne", reader);
		while (koniec_liczby < długość && czyLiczba(znak)) {
			znak = linia.charAt(koniec_liczby);
			koniec_liczby++;
		}
		if (koniec_liczby != długość) 
			obsłużBłąd("Błąd w wierszu " + numer_wiersza + " : Oczekiwana nowa linia po zapotrzebowaniu", reader);
		zapotrzebowanie = linia.substring(początek_zapotrzebowania);
		return new Proces(numer_wiersza, Integer.parseInt(zapotrzebowanie), Integer.parseInt(moment_pojawienia));
	}
	
	private static int[] obsłużOstatniąLinię(String linia, int numer_wiersza, BufferedReader reader, int ilość_q) throws IOException {
		int[] parametry_q = new int[ilość_q];
		char znak;
		int koniec_poprzedniej_cyfry = 0;
		int długość = linia.length();
		//System.out.println(długość);
		int k = 0;
		for (int i = 0; i < ilość_q; i++) {
			//System.out.println(i);
			znak = linia.charAt(k);
			k++;
			if (!czyLiczba(znak))
				obsłużBłąd("Błąd w wierszu " + numer_wiersza + " : Błędny parametr q", reader);
			while (k < długość && czyLiczba(znak)) {
				//System.out.println("W petli " + k);
				znak = linia.charAt(k);
				k++;
			}
			if (linia.charAt(k-1) != SPACJA && k != długość)
				obsłużBłąd("Błąd w wierszu " + numer_wiersza + " : Oczekiwana spacja po parametrze q", reader);
			if (k == długość && i != ilość_q-1)
				obsłużBłąd("Błąd w wierszu " + numer_wiersza + " : Za mało strategii RR", reader);
			if (k == długość) k++;
			parametry_q[i] = Integer.parseInt(linia.substring(koniec_poprzedniej_cyfry, k-1));
			koniec_poprzedniej_cyfry = k;
		}
		return parametry_q;
			
	}
	
	private static int obsłużParametr(String linia, int numer_wiersza, BufferedReader reader) throws IOException {
		int długość = linia.length();
		char znak;
		for (int i = 0; i < długość; i++) {
			znak = linia.charAt(i);
			if (!czyLiczba(znak))
				obsłużBłąd("Błąd w wierszu " + numer_wiersza + " : Błędna liczba danych wejściowych", reader);
		}
		return Integer.parseInt(linia);
	}
	
	public static DaneWejściowe obsłużWejście(String ścieżka) throws IOException {
		if (ścieżka != null) { // Przypadek gdy dostajemy argument do uruchomienia programu
			Path path = Paths.get(ścieżka);
			BufferedReader reader = null;
			try {
				reader = Files.newBufferedReader(path);
			} catch (IOException e){
				System.out.println("Plik z danymi nie jest dostępny");
				e.printStackTrace();
			} 
				String linia = reader.readLine();
				if (linia == null) 
					obsłużBłąd("Plik jest pusty", reader);	
				int liczba_wierszy = obsłużParametr(linia, 1, reader);
				LinkedList<Proces> kolejka = new LinkedList<Proces>();
				int i;
				for (i = 0; i < liczba_wierszy; i++) {
					linia = reader.readLine();
					if (linia == null)
						obsłużBłąd("Błąd w wierszu " + i+1 + " : Za mało linii", reader);
					kolejka.add(obsłużLinię(linia, i+1, reader));
				}
				linia = reader.readLine();
				if (linia == null) 
					obsłużBłąd("Błąd w wierszu " + i+2 + " : Brak strategii RR", reader);	
				int ilość_q = obsłużParametr(linia, i+2, reader);
				//System.out.println("Ilość q: " + ilość_q);
				linia = reader.readLine();
				if (linia == null) 
					obsłużBłąd("Błąd w wierszu " + i+3 + " : Brak parametrów RR", reader);	
				int[] parametry_q = obsłużOstatniąLinię(linia, i+3, reader, ilość_q);
				return new DaneWejściowe(kolejka, parametry_q);
		}
		else { // Przypadek gdy nie dostajemy argumentu przy uruchomieniu programu i czytamy ze stdin.
			Scanner scanner = new Scanner(System.in);
			int ilość_wierszy = scanner.nextInt();
			LinkedList<Proces> kolejka = new LinkedList<Proces>();
			for (int i = 0; i < ilość_wierszy; i++) {
				int moment_pojawienia = scanner.nextInt();
				int zapotrzebowanie = scanner.nextInt();
				kolejka.add(new Proces(i, zapotrzebowanie, moment_pojawienia));
			}
			int ilość_q = scanner.nextInt();
			int[] parametry_q = new int[ilość_q];
			for (int k = 0; k < ilość_q; k++) {
				int q = scanner.nextInt();
				parametry_q[k] = q;
			}
			scanner.close();
			return new DaneWejściowe(kolejka, parametry_q);
		}
	}
	
}
