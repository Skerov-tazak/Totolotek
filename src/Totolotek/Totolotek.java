package Totolotek;

import java.security.SecureRandom;
import java.util.*;

public class Totolotek {

    private static final int LICZBA_LOSOWAŃ = 42;

    private static final int LICZBA_KOLEKTUR = 10;

    private static final int LICZBA_GRACZY_KAŻDEGO_RODZAJU = 200;

    private static final int LICZBA_CYFR_PESELU = 11;

    private static final long MAKSYMALNE_ŚRODKI_POCZĄTKOWE = 100000000000L;

    private static final int MAKSYMALNA_LICZBA_ULUBIONYCH_KOLEKTUR = 4;

    private static final int MAKSYMALNA_CZĘSTOŚĆ_ZAKUPU = 10;

    private static HashSet<Long> PESELE = new HashSet<>();

    private static long[] środkiFinansowe = new long[LICZBA_GRACZY_KAŻDEGO_RODZAJU * 3];

    private static final SecureRandom secureRandom = new SecureRandom();

    public static void main(String[] args) throws Exception {

        prezentacjaDziałaniaKlas();
    }

    private static long potęgaDziesiątki(int potęga) {

        long resultat = 1L;
        for (int i = 0; i < potęga; i++) {
            resultat *= 10L;
        }

        return resultat;
    }

    private static void generujPESELE() {

        long maksPesel = potęgaDziesiątki(LICZBA_CYFR_PESELU + 1) - 1;
        long minPesel = potęgaDziesiątki(LICZBA_CYFR_PESELU);

        while (PESELE.size() < LICZBA_GRACZY_KAŻDEGO_RODZAJU * 4) {

            long możliwyPesel = secureRandom.nextLong(maksPesel - minPesel + 1) + minPesel;

            if (!PESELE.contains(możliwyPesel)) {
                PESELE.add(możliwyPesel);
            }

        }
    }

    private static void generujPoczątkoweŚrodki() {

        for(int i = 0; i < środkiFinansowe.length; i++) { 

            środkiFinansowe[i] = secureRandom.nextLong(MAKSYMALNE_ŚRODKI_POCZĄTKOWE);
        }
    }

    public static Kolektura[] generujUlubioneKolektury() {  // MEGA TURBO NIEBEZPIECZNE JAK DUŻO KOLEKTUR... (DONT USE THIS PLS. ONLY TESTING PURPOSES)

        int liczbaUlubionychKolektur = secureRandom.nextInt(MAKSYMALNA_LICZBA_ULUBIONYCH_KOLEKTUR - 
        1 ) + 1; // jak MLUK duże do liczby kolektur to lipa...

        Kolektura[] ulubioneKolektury = new Kolektura[liczbaUlubionychKolektur];

        HashSet<Integer> zaakceptowaneKolektury = new HashSet<>();

        int i = 0;
        while(i < liczbaUlubionychKolektur) {

            int propozycja = secureRandom.nextInt(Kolektura.liczbaWszystkichKolektur());
            
            if(!(zaakceptowaneKolektury.contains(propozycja))) {
                
                zaakceptowaneKolektury.add(propozycja);
                ulubioneKolektury[i] = Kolektura.zwróćKolekturęNumer(propozycja);
                i++;
            }
        }
        
        return ulubioneKolektury;
    }

    public static Blankiet generujBlankiet() { 

        Blankiet blankiet = new Blankiet();

        int liczbaZakładówNaBlankiecie = secureRandom.nextInt(Kupon.zwróćMaksLiczbęZakładów() - 1) + 1;

        for(int i = 0; i < liczbaZakładówNaBlankiecie; i++) { 

            blankiet.wypełnijZakład(Losowanie.generujLosoweNumerki());
        }

        int liczbaLosowań = secureRandom.nextInt(Kupon.zwróćMaksLiczbęLosowań() - 1) + 1;

        blankiet.wypełnijLiczbęLosowań(liczbaLosowań);

        return blankiet;
    }

    private static void stwórzGraczy() { 

        

        String nazwisko = "Xsiński";
        String imię = "Igrek";

        generujPESELE();
        generujPoczątkoweŚrodki();


        Iterator<Long> PESEL = PESELE.iterator();
        for(int j = 0; j < LICZBA_GRACZY_KAŻDEGO_RODZAJU; j++) { 
    

            new Losowy(imię, nazwisko, PESEL.next()); 

        
            int ulubionaKolektura = secureRandom.nextInt(Kolektura.liczbaWszystkichKolektur());          
            new Minimalista(imię, nazwisko, PESEL.next(), środkiFinansowe[j*3], Kolektura.zwróćKolekturęNumer(ulubionaKolektura));   

        
            
            new Staloliczbowy(imię, nazwisko, PESEL.next(), środkiFinansowe[j*3 + 1], Losowanie.generujLosoweNumerki(), generujUlubioneKolektury());
  

            int częstośćZakupu = secureRandom.nextInt(MAKSYMALNA_CZĘSTOŚĆ_ZAKUPU - 1) + 1;
            new Staloblankietowy(imię, nazwisko, PESEL.next(), środkiFinansowe[j*3 + 2], częstośćZakupu, generujBlankiet(),  generujUlubioneKolektury());        
        
        }
    }


    private static void prezentacjaDziałaniaKlas() {

        for (int i = 0; i < LICZBA_KOLEKTUR; i++) {
            Centrala.otwórzKolekturę();
        }

        stwórzGraczy();
  
        for (int i = 0; i < LICZBA_LOSOWAŃ; i++) { 
            Gracz.wykonajRuchyGraczy();
            Centrala.losuj();
            Centrala.wypiszPoprzednieLosowanie();
        }

    
        

    }
}
