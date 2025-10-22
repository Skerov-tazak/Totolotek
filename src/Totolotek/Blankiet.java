package Totolotek;

public class Blankiet {

    // ---- ZMIENNE ---- //

    private Zaklad[] zakłady = new Zaklad[Kupon.zwróćMaksLiczbęZakładów()];

    private int wypełnioneZakłady = 0;

    private int poprawnieWypełnione = 0;

    private int liczbaLosowań = 1;

    // ---- KOSTRUKTORY ---- //

    public Blankiet() {}

    public static Blankiet wypełnijBlankiet(int ile, int... pola) {

        Blankiet nowyBlankiet = new Blankiet();
        nowyBlankiet.wypełnijZakład(pola);
        nowyBlankiet.wypełnijLiczbęLosowań(ile);
        return nowyBlankiet;
    }

    // ---- OPERACJE NA BLANKIETACH ---- //

    public boolean wypełnijZakład(int... pola) {

        if (wypełnioneZakłady == zakłady.length)
            return false;
      
        Zaklad obecny = Zaklad.wypełnijZakład(pola);
        
        if(!obecny.czyAnulowany()) {
            zakłady[poprawnieWypełnione] = obecny;
            poprawnieWypełnione++;
        }

        wypełnioneZakłady++;
        return true;
    }

    public void wypełnijLiczbęLosowań(int ile) {

        if (liczbaLosowań > Kupon.zwróćMaksLiczbęLosowań())
            throw new IllegalArgumentException();

        if (liczbaLosowań < ile) {
            liczbaLosowań = ile;
        }
    }

    // ---- FUNCKJE ZWRACAJĄCE ---- //

    public Zaklad[] zwróćWszystkieZakłady() {
        return zakłady;
    }

    public int zwróćLiczbęLosowań(){
        return liczbaLosowań;
    }

    public int zwróćLiczęPoprawnychZakładów(){
        return poprawnieWypełnione;
    }

    public Long obliczKoszt() {
        return Centrala.zwróćCenęZakładu() * liczbaLosowań * poprawnieWypełnione;
    }

    
}
