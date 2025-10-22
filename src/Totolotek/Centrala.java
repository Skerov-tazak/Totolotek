package Totolotek;

import java.util.*;

public class Centrala {

    // ---- WEWNĘTRZNIE UŻYWANE KLASY ---- //    

    // ---- Wygrana (DATA): przechowuje informacje o pulach i numer losowań ---- //

    private static class Wygrana {

        Wygrana(int numerLosowania, int ilośćTrafień) {
            this.przysługującaPula = (LICZBA_POZIOMÓW_PULI - 1 - (ilośćTrafień - LICZBA_POZIOMÓW_PULI + 1));
            this.numerLosowania = numerLosowania;
        }

        int numerLosowania;

        int przysługującaPula;

        public int zwróćNumerWygranejLosowania() {

            return numerLosowania;
        }

        public int zwróćPrzysługującąPulę() {

            return przysługującaPula;
        }

    }

    // ---- ZbiórKuponów (DATA): przechowuje kupony o wspólnym ostatnim losowaniu ---- //

    private static class ZbiórKuponów {
        
        public ZbiórKuponów(int ostatniWażnyZakład) {
            
            this.ostatniWażnyZakład = ostatniWażnyZakład;
        }

        HashSet<Kupon> kupony = new HashSet<>();

        int ostatniWażnyZakład;

        public int zwróćOstatniWażnyZakład() {
            return ostatniWażnyZakład;
        }

        public void dodajKupon(Kupon kupon) {
            kupony.add(kupon);
        }

        public void usuńKupon(Kupon kupon) {
            kupony.remove(kupon);
        }

        public ArrayList<Kupon> zwróćListęKuponów() {
            return new ArrayList<Kupon>(kupony);
        }
    }

    // ---- KONSTRUKTOR ---- // 

    private Centrala() {} // Istnieje tylko jedna centrala...

    // ----- STAŁE ----- //

    private static final Centrala centrala = new Centrala();

    private static final int LICZBA_POZIOMÓW_PULI = 4;

    private static final long CENA_ZAKŁADU = 300L;

    private static final int PROCENT_NA_NAGRODY = 51;

    private static final int PROCENT_NA_I_STOPIEŃ = 44;

    private static final long MIN_GWARANTOWANA_I_STOPIEŃ = 200000000L;

    private static final long MIN_GWARANTOWANA_III_STOPIEŃ = 15 * CENA_ZAKŁADU;

    private static final int PROCENT_NA_II_STOPIEŃ = 8;

    private static final long WYSOKOŚĆ_IV_STOPIEŃ = 2400L;

    // ----- ZMIENNE ---- //

    private static int aktualnyNumerKolektury = 0;

    private static long obecnaKumulacja = 0L;

    private static long liczbaSprzedanychKuponów = 0L;

    private static final Bank bank = new Bank();

    private static long przychódZeSprzedaży = 0L;

    private static Losowanie aktualneLosowanie;

    private static int aktualnyNumerLosowania = 0;

    private static long[] sumaPuli = new long[LICZBA_POZIOMÓW_PULI];

    private static long[] kwotaPuli = new long[LICZBA_POZIOMÓW_PULI];

    private static long[] liczbaWygranychPuli = new long[LICZBA_POZIOMÓW_PULI];

    private static TreeMap<Integer, ZbiórKuponów> żyweKupony = new TreeMap<>();

    private static HashMap<Kupon, ArrayList<Wygrana>> wygraneKupony = new HashMap<>();

    private static ArrayList<Losowanie> przeprowadzoneLosowania = new ArrayList<>();

    private static long aktualnyNumerKuponu = 0L;

    // ----------- FUNKCJE ODPOWIEDZIALNE ZA FUNKCJONALNOŚĆ CENTRALI ----------- //

    // ---- Funkcja do tworzenia kolektur ---- //

    public static void otwórzKolekturę() {

        new Kolektura(bank, centrala);
    }

    // ---- Pomocnicza do liczenia procentów ---- //

    public static long procentWartości(int procent, long wartość) {
        return (wartość * procent) / 100;
    }

    // ---- Bitowo i szybko (O(N)) liczy liczbę wspólnych elementów ---- //

    private static int liczbaTrafionychNumerków(Zaklad zakładzik) {

        int[] arr1 = zakładzik.zwróćObstawioneLiczby();
        int[] arr2 = aktualneLosowanie.zwróćWygraneNumerki();

        long maska1 = 0L; // Zakłada że jest mniej niż 64 różne wartości
        long maska2 = 0L;

        for (int i = 0; i < Zaklad.długośćZakładu(); i++) {
            maska1 |= (1L << arr1[i]);
            maska2 |= (1L << arr2[i]);
        }

        long wspólneBity = maska1 & maska2;
        return Long.bitCount(wspólneBity);

    }

    // ---- Oblicza liczbę wygranych różnych stopni ---- //

    public static void obliczLiczbeWygranych() {

        for (int i = 0; i < liczbaWygranychPuli.length; i++)
            liczbaWygranychPuli[i] = 0L;

        for (ZbiórKuponów rozważany : żyweKupony.values()) {
            for (Kupon kupon : rozważany.zwróćListęKuponów()) {
                for (Zaklad zakładzik : kupon.zwróćWszystkieZakłady()) {

                    int trafioneNumerki = liczbaTrafionychNumerków(zakładzik);

                    if (trafioneNumerki >= 3) {

                        if (!wygraneKupony.containsKey(kupon)) {
                            wygraneKupony.put(kupon, new ArrayList<Wygrana>());
                        }

                        wygraneKupony.get(kupon).add(new Wygrana(aktualneLosowanie.zwróćNumer(), trafioneNumerki));
                        liczbaWygranychPuli[LICZBA_POZIOMÓW_PULI - 1 - (trafioneNumerki - LICZBA_POZIOMÓW_PULI + 1)]++;
                    }

                }
            }
        }
    }

    // ---- Oblicza wartości różnych puli zgodnie z zasadami ---- //

    public static void obliczPule() {

        long kwotaNaNagrody = procentWartości(PROCENT_NA_NAGRODY, przychódZeSprzedaży);

        usuńNieważneKupony();
        obliczLiczbeWygranych();

        sumaPuli[0] = Math.max(procentWartości(PROCENT_NA_I_STOPIEŃ, kwotaNaNagrody) + obecnaKumulacja,
                MIN_GWARANTOWANA_I_STOPIEŃ);
        sumaPuli[1] = procentWartości(PROCENT_NA_II_STOPIEŃ, kwotaNaNagrody);
        sumaPuli[3] = WYSOKOŚĆ_IV_STOPIEŃ * liczbaWygranychPuli[3];

        sumaPuli[2] = Math.max(MIN_GWARANTOWANA_III_STOPIEŃ * liczbaWygranychPuli[2],
                kwotaNaNagrody - procentWartości(PROCENT_NA_I_STOPIEŃ, kwotaNaNagrody) - sumaPuli[1] - sumaPuli[3]);

        for (int i = 0; i < LICZBA_POZIOMÓW_PULI; i++) {
            if (liczbaWygranychPuli[i] != 0)
                kwotaPuli[i] = sumaPuli[i] / liczbaWygranychPuli[i];
            else 
                kwotaPuli[i] = 0L;
        }

        if (liczbaWygranychPuli[0] == 0)
            obecnaKumulacja += sumaPuli[0];
        else
            obecnaKumulacja = 0L;

        przychódZeSprzedaży = 0L;
    }

    public static void usuńNieważneKupony() {

        Map.Entry<Integer, ZbiórKuponów> najstarszyEnt = żyweKupony.firstEntry();
        if(najstarszyEnt != null) {

            ZbiórKuponów najstarsze = najstarszyEnt.getValue();


            if (najstarsze.zwróćOstatniWażnyZakład() < aktualnyNumerLosowania) {
                żyweKupony.pollFirstEntry();
            }
        }
    }

    public static void losuj() {

        aktualneLosowanie = Losowanie.generujLosowanie(aktualnyNumerLosowania);
        aktualnyNumerLosowania++;

        przeprowadzoneLosowania.add(aktualneLosowanie);

        obliczPule();

        aktualneLosowanie.ustawDane(kwotaPuli, sumaPuli, liczbaWygranychPuli);

    }

    // ---- Oblicza Kwotę nagrody z jednego kuponu ---- //

    private static long[] obliczPrzysługiwanąNagrodę(Kupon kupon) {

        long największaWygrana = 0L;
        long całkowitaWygrana = 0L;

        if (wygraneKupony.containsKey(kupon)) { // WygraneKupony zawiera ten kupon tylko jeśli został on uczciwie
                                                // sprzedany
            long obecnaLiczonaWygrana = 0L;
            ArrayList<Wygrana> przyszługująceWygrane = wygraneKupony.get(kupon);

            for (Wygrana wygrana : przyszługująceWygrane) {
               
                Losowanie wygraneLosowanie = przeprowadzoneLosowania.get(wygrana.zwróćNumerWygranejLosowania() - 1);
                obecnaLiczonaWygrana += wygraneLosowanie.zwróćPuleWygranych()[wygrana.zwróćPrzysługującąPulę()];

                if (obecnaLiczonaWygrana > największaWygrana)
                    największaWygrana = obecnaLiczonaWygrana;

                całkowitaWygrana += obecnaLiczonaWygrana;
            }
        }

        return new long[] { całkowitaWygrana, największaWygrana };

    }

    // ---- Autoryzuje tranzakcje na podstawie kuponu ---- //

    public void wypłaćNagrodę(Kupon kupon, long numerKonta) {

        long[] otrzymaneNagrody = obliczPrzysługiwanąNagrodę(kupon);

        wygraneKupony.remove(kupon);

        ZbiórKuponów zbiór = żyweKupony.get(kupon.zwróćOstatnieWażneLosowanie());
        if (zbiór != null) {
            zbiór.usuńKupon(kupon);
        }

        bank.wpłaćNaKontoBezPodatku(numerKonta, otrzymaneNagrody[0] - otrzymaneNagrody[1]);
        bank.wpłaćNaKonto(numerKonta, otrzymaneNagrody[1]);

    }

    public void dodajKupon(Kupon kupon) {

        if(kupon == null)
            throw new IllegalArgumentException("Kupon nie może być null!");

        liczbaSprzedanychKuponów++;
        przychódZeSprzedaży += kupon.obliczCenę();

        if (żyweKupony.containsKey(kupon.zwróćOstatnieWażneLosowanie())) {
            ZbiórKuponów adekwatny = żyweKupony.get(kupon.zwróćOstatnieWażneLosowanie());
            adekwatny.dodajKupon(kupon);

        } else {
            ZbiórKuponów adekwatny = new ZbiórKuponów(kupon.zwróćOstatnieWażneLosowanie());
            adekwatny.dodajKupon(kupon);
            żyweKupony.put(kupon.zwróćOstatnieWażneLosowanie(), adekwatny);
        }
    }

    // ---- FUNKCJE OPERUJĄCE NA ZMIENNYCH ---- //

    public void zwiększNumerKuponu() {
        aktualnyNumerKuponu++;
    }

    public void zwiększNumerKolektury() {
        aktualnyNumerKolektury++;
    }

    // ---- FUNKCJE WYPISUJĄCE I ZWRACAJĄCE ---- //

    public static long zwróćAktualnyNumerKuponu() {
        return aktualnyNumerKuponu;
    }

    public static int zwróćAktualnyNumerKolektury() {
        return aktualnyNumerKolektury;
    }

    public static long zwróćCenęZakładu() {
        return CENA_ZAKŁADU;
    }

    public static int zwróćLiczbęPoziomówPuli() {
        return LICZBA_POZIOMÓW_PULI;
    }

    public static int zwróćNumerPoprzedniegoLosowania() {
        return aktualnyNumerLosowania;
    }

    private static void wypiszlongArray(long[] arr) {
       
        for(int i = 0; i < arr.length; i++) {
            System.out.println( i + ": " + arr[i]);
        }
    }

    private static void wypiszlongArrayKwota(long[] arr) {
       
        for(int i = 0; i < arr.length; i++) {
            System.out.println( (3 - (3 - i)) + ": " + Bank.formatujKwotę(arr[i]));
        }
    }

    public static void wypiszLiczbęSprzedanychKuponów(int numerLosowania) {
        
        System.out.println("Łącznie sprzedano kuponów: " + liczbaSprzedanychKuponów);
    }

    public static void wypiszKwotyWygranej(int numerLosowania) {
        
        System.out.println("Kwoty wygranych: ");
        wypiszlongArrayKwota(przeprowadzoneLosowania.get(numerLosowania).zwróćPuleWygranych());
    }

    public static void wypiszLiczbęZwycięskichZakładów(int numerLosowania) {

        System.out.println("Liczba Zwycięskich zakładów: ");
        wypiszlongArray(przeprowadzoneLosowania.get(numerLosowania).zwróćLiczbęZwycięskichZakładów());
    }

    public static void wypiszŁącznePuleWygranych(int numerLosowania) { 

        System.out.println("Łączne sumy Puli: ");
        wypiszlongArrayKwota(przeprowadzoneLosowania.get(numerLosowania).zwróćŁączneKwotyWygranych());
    }

    public static void wypiszPoprzednieLosowanie() {
        
        System.out.print(aktualneLosowanie);
        wypiszLiczbęSprzedanychKuponów(aktualnyNumerLosowania);
        wypiszŁącznePuleWygranych(aktualnyNumerLosowania - 1);
        wypiszLiczbęZwycięskichZakładów(aktualnyNumerLosowania - 1);
        wypiszKwotyWygranej(aktualnyNumerLosowania - 1);
        BudzetPanstwa.wypiszPrzekazaneSubwencje();
        BudzetPanstwa.wypiszZebranePodatki();
        bank.wypiszŚrodkiCentrali();
        liczbaSprzedanychKuponów = 0L;
        System.out.println("\n");

    }

    public static void wypiszPrzeprowadzoneLosowania() {

        for(int i = 0; i < przeprowadzoneLosowania.size(); i++) {
            
            System.out.println(przeprowadzoneLosowania.get(i));

            wypiszŁącznePuleWygranych(i);
            wypiszLiczbęZwycięskichZakładów(i);
            wypiszKwotyWygranej(i);
            BudzetPanstwa.wypiszPrzekazaneSubwencje();
            BudzetPanstwa.wypiszZebranePodatki();
            bank.wypiszŚrodkiCentrali();

            System.out.println("\n");
        }
    }

    public static void wypiszŚrodki() {

        bank.wypiszŚrodkiCentrali();
    }

}
