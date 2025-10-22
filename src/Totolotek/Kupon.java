package Totolotek;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Kupon {

    // ----- STAŁE ---- //

    private static final int MAX_LICZBA_ZAKŁADÓW = 8;

    private static final int MAX_LICZBA_LOSOWAŃ = 10;

    private static final long DŁUGOŚĆ_LOSOWEGO_ZNACZNIKA = 9L;

    // ----- ZMIENNE ----- //

    private String numerKuponu;

    private int numerKolektury;

    private Zaklad[] zakłady;

    private int liczbaZakładów;

    private int pierwszeWażneLosowanie;

    private int liczbaLosowań;

    // ---- KONSTRUKTORY ----- //

    private Kupon() {
    }

    public static Kupon generujBlankietowo(Blankiet blankiet, int numerKolektury, long numerKuponu) {

        if(blankiet == null || numerKolektury < 0 || numerKolektury >= Kolektura.liczbaWszystkichKolektur())
            throw new IllegalArgumentException();

        Kupon nowyKupon = new Kupon();

        nowyKupon.numerKolektury = numerKolektury;
        nowyKupon.liczbaZakładów = blankiet.zwróćLiczęPoprawnychZakładów();
        nowyKupon.zakłady = new Zaklad[nowyKupon.liczbaZakładów];
        nowyKupon.numerKuponu = stwórzNumerKuponu(numerKolektury, numerKuponu);
        nowyKupon.pierwszeWażneLosowanie = Centrala.zwróćNumerPoprzedniegoLosowania() + 1;
        nowyKupon.liczbaLosowań = blankiet.zwróćLiczbęLosowań();
        nowyKupon.zakłady = Arrays.copyOf(blankiet.zwróćWszystkieZakłady(), nowyKupon.liczbaZakładów);

        return nowyKupon;
    }

    public static Kupon generujLosowy(int liczbaZakładów, int liczbaLosowań, int numerKolektury, long numerKuponu) {

        if(liczbaZakładów < 0 || liczbaZakładów > MAX_LICZBA_ZAKŁADÓW || liczbaLosowań < 0 || liczbaLosowań > MAX_LICZBA_LOSOWAŃ
            || numerKolektury < 0 || numerKolektury >= Kolektura.liczbaWszystkichKolektur())    {
                throw new IllegalArgumentException();
        }

        Kupon nowyKupon = new Kupon();
        nowyKupon.numerKolektury = numerKolektury;
        nowyKupon.zakłady = new Zaklad[liczbaZakładów];
        nowyKupon.numerKuponu = stwórzNumerKuponu(numerKolektury, numerKuponu);
        nowyKupon.liczbaLosowań = liczbaLosowań;
        nowyKupon.pierwszeWażneLosowanie = Centrala.zwróćNumerPoprzedniegoLosowania() + 1;
        nowyKupon.liczbaZakładów = liczbaZakładów;

        for (int i = 0; i < liczbaZakładów; i++) {
            nowyKupon.zakłady[i] = Zaklad.stwórzLosowy();
        }

        return nowyKupon;
    }

    // ---- FUNCKJE LOKALNE ---- //

    private static long sumaCyfr(long liczba) {

        long wynik = 0L;
        while (liczba != 0) {
            wynik += liczba % 10;
            liczba = liczba / 10;
        }

        return wynik;
    }

    private static String stwórzNumerKuponu(int numerKolektury, long numerKuponu) {

        long losowyZnacznik = 0L;

        for (int i = 0; i < DŁUGOŚĆ_LOSOWEGO_ZNACZNIKA; i++) {
            long nowaCyfra = ThreadLocalRandom.current().nextLong(10L); // /dev/random (not enough noise)
            losowyZnacznik += nowaCyfra;
            losowyZnacznik *= 10;
        }

        long sumaCyfrModulo100 = (sumaCyfr(losowyZnacznik) + sumaCyfr((long) numerKolektury) + sumaCyfr(numerKuponu)) % 100;
        String sumaCyfrModulo100Lit = Long.toString(sumaCyfrModulo100);

        if (sumaCyfrModulo100 < 10)
            sumaCyfrModulo100Lit = "0" + sumaCyfrModulo100Lit;

        return numerKuponu + "-" + numerKolektury + "-" + losowyZnacznik + "-" + sumaCyfrModulo100Lit;
    }

    public long obliczCenę() {
        return Centrala.zwróćCenęZakładu() * liczbaLosowań * liczbaZakładów;
    }

    // ----- OPERACJE NA KUPONIE ----- //

    public int zwróćNumerSprzedającejKolektury() {
        return numerKolektury;
    }

    public static int zwróćMaksLiczbęLosowań() {
        return MAX_LICZBA_LOSOWAŃ;
    }

    public Zaklad[] zwróćWszystkieZakłady() {
        return zakłady;
    }

    public int zwróćLiczbęLosowań() {
        return liczbaLosowań;
    }

    public int zwróćPierwszeWażneLosowanie() {
        return pierwszeWażneLosowanie;
    }

    public int zwróćOstatnieWażneLosowanie() {
        return pierwszeWażneLosowanie + liczbaLosowań - 1;
    }

    public static int zwróćMaksLiczbęZakładów() {
        return MAX_LICZBA_ZAKŁADÓW;
    }

    public void oddajKolekturze(long numerKonta) {

        Kolektura.zwróćKolekturęNumer(numerKolektury).odbierzNagrodę(this, numerKonta);
    }

    public boolean nieaktualny() {
        return (zwróćOstatnieWażneLosowanie() <= Centrala.zwróćNumerPoprzedniegoLosowania());
    }

    // ----- FUNKCJE WYPISUJĄCE ----- //

    @Override
    public String toString() {

        String napis = "";
        napis += "KUPON NR " + numerKuponu + "\n";

        for (int i = 0; i < liczbaZakładów; i++) {
            napis += i + ": " + zakłady[i] + "\n";
        }

        napis += "LICZBA LOSOWAŃ: " + liczbaLosowań + "\n";
        napis += "NUMERY LOSOWAŃ: " + "\n";

        for (int j = 0; j < liczbaLosowań; j++) {
            napis += " " + pierwszeWażneLosowanie + j;
        }
        napis += "\n";
        napis += "CENA: " + obliczCenę() + "\n";
        return napis;
    }

    public void wypiszKupon() {
        System.out.print(this);
    }

    public void wypiszNumerKuponu() {

        System.out.println(numerKuponu);
    }

    public String zwróćNumerKuponu() {

        return numerKuponu;
    }

    public void wypiszCenę() {

        System.out.println(Bank.formatujKwotę(obliczCenę()));
    }

    public void wypiszPodatek() {

        System.out.println(Bank.formatujKwotę(Bank.zwróćPodatekOdCenyZakładu(obliczCenę())));
    }

    // Dwa kupony nie mogą być takie same, jeśli nie są tym samym kuponem...
    @Override
    public boolean equals(Object obj) {

        if (obj != this)
            return false;

        return true;
    }

    // Każdy numer kuponu jest unikatowy - zatem może być hashem
    @Override
    public int hashCode() {

        return Objects.hash(numerKuponu);
    }
}
