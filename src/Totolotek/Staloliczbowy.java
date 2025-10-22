package Totolotek;
import java.util.*;

public class Staloliczbowy extends Gracz{

    // ---- ZMIENNE ---- //

    private int[] ulubioneLiczby;

    private Kolektura[] ulubioneKolektury;

    private int obecnaKolektura;

    // ---- KONSTRUKTOR ---- //

    public Staloliczbowy(String imię, String Nazwisko, long PESEL, long środkiPoczątkowe, int[] ulubioneLiczby, Kolektura[] ulubioneKolektury) {
        
        super(imię, Nazwisko, PESEL, środkiPoczątkowe);

        if(ulubioneKolektury == null || ulubioneKolektury.length == 0 || ulubioneLiczby.length != Zaklad.długośćZakładu())
            throw new IllegalArgumentException("Niepoprawna inicjalizacja gracza Stałoliczbowego");
        
        this.ulubioneLiczby = Arrays.copyOf(ulubioneLiczby, ulubioneLiczby.length);
        this.ulubioneKolektury = Arrays.copyOf(ulubioneKolektury, ulubioneKolektury.length);
        
    }

    // ---- AKCJA GRACZA ---- //

    @Override
    public void wykonajOsobistyRuch() {
        
        if(posiadaneKupony.size() == 0) { 

            Blankiet wypełnioneLiczby = Blankiet.wypełnijBlankiet(10, ulubioneLiczby);

            Kupon zakupiony = ulubioneKolektury[obecnaKolektura].sprzedajKupon(wypełnioneLiczby, numerKonta);
            posiadaneKupony.add(zakupiony);
            obecnaKolektura = (obecnaKolektura++) % ulubioneKolektury.length;

        } else {

            oddajRozegraneKupony();
        }
    }

}
