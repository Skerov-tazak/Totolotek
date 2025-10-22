package Totolotek;

import java.util.*;

public abstract class Gracz {
    
    // ---- ZMIENNE ---- //

    private static final ArrayList<Gracz> gracze = new ArrayList<>();

    protected String imię;

    protected String nazwisko;

    protected long PESEL;

    protected long numerKonta;

    protected List<Kupon> posiadaneKupony = new LinkedList<>();

    // ---- KONSTRUKTOR ---- //

    public Gracz(String imię, String nazwisko, long PESEL, long środkiFinansowe) {

        this.imię = imię;
        this.nazwisko = nazwisko;
        this.PESEL = PESEL;
        numerKonta = Bank.dodajKonto(PESEL, środkiFinansowe);
        gracze.add(this);
    }

    // ---- AKCJA GRACZA ---- //

    protected void oddajRozegraneKupony() {
        
        Iterator<Kupon> iterator = posiadaneKupony.iterator();
        while(iterator.hasNext()) { 
            Kupon kupon = iterator.next();
            if(kupon != null && kupon.nieaktualny()) { 
                kupon.oddajKolekturze(numerKonta);
                iterator.remove();
            }
        }
    }

    public static void wykonajRuchyGraczy() {

        Iterator<Gracz> iterator = gracze.iterator();

        while (iterator.hasNext()) {

            Gracz rozważany = iterator.next();

            if (rozważany == null) {
                iterator.remove();
            } else {
                rozważany.wykonajOsobistyRuch();
            }
        }
    }

    public abstract void wykonajOsobistyRuch();

    @Override
    public String toString() {
        String napis = "" + imię + " " + nazwisko + " | nr: " + PESEL;
        return napis;
    }
}
