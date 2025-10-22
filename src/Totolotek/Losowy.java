package Totolotek;
import java.security.SecureRandom;

public class Losowy extends Gracz {

    // --- STAŁE ---- //

    private static final SecureRandom secureRandom = new SecureRandom();

    private static long MAKSYMALNE_POCZĄTKOWE_ŚRODKI = 100000000L;

    private static int MIN_ILOŚĆ_KUPIONA = 1;

    private static int MAX_ILOŚĆ_KUPIONA = 100;

    // ---- KONSTRUKTOR ---- //

    public Losowy(String imię, String nazwisko, long PESEL) { 
        
        super(imię, nazwisko, PESEL, secureRandom.nextLong(MAKSYMALNE_POCZĄTKOWE_ŚRODKI));  
    }

    // ---- AKCJA GRACZA ---- //

    @Override
    public void wykonajOsobistyRuch() {
        
        int ograniczenie = MAX_ILOŚĆ_KUPIONA - MIN_ILOŚĆ_KUPIONA + 1;
        int ilośćKuponówDoKupienia = secureRandom.nextInt(ograniczenie) + MIN_ILOŚĆ_KUPIONA;

        for(int i = 0; i < ilośćKuponówDoKupienia; i++) {

            int liczbaLosowań = secureRandom.nextInt(Kupon.zwróćMaksLiczbęLosowań() - 1) + 1;
            int liczbaZakładów = secureRandom.nextInt(Kupon.zwróćMaksLiczbęZakładów() - 1) + 1;
            int numerKolektury = secureRandom.nextInt(Kolektura.liczbaWszystkichKolektur());
            Kupon zakupiony = Kolektura.zwróćKolekturęNumer(numerKolektury).sprzedajKupon(liczbaZakładów, liczbaLosowań, numerKonta);
            posiadaneKupony.add(zakupiony);
        }

        oddajRozegraneKupony();
    }
}
