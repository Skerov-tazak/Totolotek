
package Totolotek;

import java.util.*;

public class Staloblankietowy extends Gracz {

    // ---- ZMIENNE ---- //

    private Blankiet ulubionyBlankiet;

    private Kolektura[] ulubioneKolektury;

    private int obecnaKolektura;

    private int częstośćZakupu;

    private int iteracjaZakupu = 0;

    public Staloblankietowy(String imię, String Nazwisko, long PESEL, long środkiPoczątkowe, int częstośćZakupu, Blankiet ulubionyBlankiet, Kolektura[] ulubioneKolektury) {
        
        super(imię, Nazwisko, PESEL, środkiPoczątkowe);

        if(ulubioneKolektury == null || ulubionyBlankiet == null)
            throw new IllegalArgumentException();

        this.ulubioneKolektury = Arrays.copyOf(ulubioneKolektury, ulubioneKolektury.length);
        this.ulubionyBlankiet = ulubionyBlankiet;       
        this.częstośćZakupu = częstośćZakupu;
    }

    // ---- AKCJA GRACZA ---- //

    @Override
    public void wykonajOsobistyRuch() {
        
        if(iteracjaZakupu == częstośćZakupu) {

            Kupon zakupiony = ulubioneKolektury[obecnaKolektura].sprzedajKupon(ulubionyBlankiet, numerKonta);
            posiadaneKupony.add(zakupiony);
            obecnaKolektura = (obecnaKolektura++) % ulubioneKolektury.length;
            iteracjaZakupu = 0;
        
        } else {

            iteracjaZakupu++;
            oddajRozegraneKupony();

        }
    }
}
