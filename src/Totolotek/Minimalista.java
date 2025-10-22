package Totolotek;

public class Minimalista extends Gracz {

    // ---- STAŁE ---- //

    private static int ILE_ZAKŁADÓW = 1;

    private static int ILE_LOSOWAŃ = 1;

    // ---- ZMIENNE ---- //

    private Kolektura ulubionaKolektura;

    // ---- KONSTRUKTOR ---- //

    public Minimalista(String imię, String nazwisko, long PESEL, long środkiFinansowe, Kolektura ulubionaKolektura) {

        super(imię, nazwisko, PESEL, środkiFinansowe);
        
        if(ulubionaKolektura == null)
            throw new IllegalArgumentException();

        this.ulubionaKolektura = ulubionaKolektura;
    }

    // ---- AKCJA GRACZA ---- //

    @Override
    public void wykonajOsobistyRuch() {

        Kupon kupiony = ulubionaKolektura.sprzedajKupon(ILE_ZAKŁADÓW, ILE_LOSOWAŃ, numerKonta);
        posiadaneKupony.add(kupiony);

        oddajRozegraneKupony();

    }

}
