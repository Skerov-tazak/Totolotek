package Totolotek;

import java.util.Arrays;
import java.util.HashSet;
import java.security.SecureRandom;

public class Losowanie {

    // ---- STAŁE ---- //

    private static final SecureRandom secureRandom = new SecureRandom();

    private static final int MAKSYMALNY_NUMEREK = 49; // NIE USTAWIAĆ WYŻEJ NIŻ 63 BO SIĘ ZEPSUJE!!!

    private static final int MINIMALNY_NUMEREK = 1;

    // ---- ZMIENNE ---- //

    private static long liczbaSprzedanychKuponów = 0L;

    private int numerLosowania;

    private int[] wygraneNumerki;

    private long[] łączneKwotyWygranych = new long[Centrala.zwróćLiczbęPoziomówPuli()];

    private long[] puleWygranych = new long[Centrala.zwróćLiczbęPoziomówPuli()];

    private long[] liczbaZwycięskichZakładów = new long[Centrala.zwróćLiczbęPoziomówPuli()]; 

    // ---- FUNKCJA USTAWIAJĄCA ---- //

    public void ustawDane(long[] kwotyPuli, long[] sumyPuli, long[] liczbaWygranych){
        łączneKwotyWygranych = Arrays.copyOf(sumyPuli, sumyPuli.length); 
        puleWygranych = Arrays.copyOf(kwotyPuli, kwotyPuli.length);
        liczbaZwycięskichZakładów = Arrays.copyOf(liczbaWygranych, liczbaWygranych.length);
    }

    // ---- KONSTRUKTOR ---- //

    public static Losowanie generujLosowanie(int numerPoprzedniego){
        
        Losowanie noweLosowanie = new Losowanie();
        noweLosowanie.numerLosowania = numerPoprzedniego + 1;
        noweLosowanie.wygraneNumerki = Arrays.copyOf(generujLosoweNumerki(), Zaklad.długośćZakładu());

        return noweLosowanie;
    }

    public static int[] generujLosoweNumerki() { 
        
        HashSet<Integer> zaakceptowaneNumerki = new HashSet<>();
        int[] odpowiedź = new int[Zaklad.długośćZakładu()];
        int ograniczenie = MAKSYMALNY_NUMEREK - MINIMALNY_NUMEREK + 1;

        int i = 0;
        while(i < Zaklad.długośćZakładu()){
            
            int potencjalne = secureRandom.nextInt(ograniczenie) + MINIMALNY_NUMEREK;
            
            if(!zaakceptowaneNumerki.contains(potencjalne)) {

                odpowiedź[i] = potencjalne;
                i++;
                zaakceptowaneNumerki.add(potencjalne);
            }
        }

        return odpowiedź;
    }

    // ---- FUNKCJE ZWRACAJĄCE ---- //

    

    public int[] zwróćWygraneNumerki(){
        return wygraneNumerki;
    }

    public int zwróćNumer(){
        return numerLosowania;
    }

    public long[] zwróćPuleWygranych(){
        return puleWygranych;
    }

    public long[] zwróćŁączneKwotyWygranych() { 
        return łączneKwotyWygranych;
    }

    public long[] zwróćLiczbęZwycięskichZakładów() { 
        return liczbaZwycięskichZakładów;
    }

    public static int zwróćMinimalnyNumerek(){
        return MINIMALNY_NUMEREK;
    }

    public static int zwróćMaksymalnyNumerek(){
        return MINIMALNY_NUMEREK;
    }

    public void zarejestrujSprzedanyKupon() {
        liczbaSprzedanychKuponów++;
    }

    public long zwróćLiczbęSprzedanychKuponów() {
        return liczbaSprzedanychKuponów;
    }

    private String wypiszWygraneLiczby() { 
   
        String res = "";
        for(int x : wygraneNumerki) { 
            if(x < 10)
                res += " ";
            res += " " + x;
        }
        return res;
    }

    @Override
    public String toString() {

        String napis = "Losowanie nr " + numerLosowania + "\n" + "Wyniki: " + wypiszWygraneLiczby() + "\n";
        return napis;  
    }

}
