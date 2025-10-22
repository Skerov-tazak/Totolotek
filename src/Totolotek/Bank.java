package Totolotek;
import java.util.HashMap;
import java.util.Objects;

// Obsługuje Wszystkie Płatności i tranzakcje
public class Bank {

    // ---- STAŁE ---- //

    private final BudzetPanstwa budżetPaństwa = new BudzetPanstwa();

    // ---- ZMIENNE ---- //

    private static HashMap<Long,Long> kontaBankowe = new HashMap<>();

    private static final long PROCENT_PODATKU_OD_ZAKŁADU = 20L;

    private static final long PROCENT_PODATKU_OD_WYGRANEJ = 10L;

    private long środkiFinansoweCentrali = 0L;

    // ---- FUNCKJA DOSTĘPNE DLA GRACZA ---- //

    // Dowolna Funkcja Bijekcyjna, cel tutaj jest taki by chronić dane osobowe...
    private static long wyliczNumerKonta(long PESEL){
        return (long)Objects.hash(PESEL);       
    }

    public static long dodajKonto(long PESEL, long początkowyStanKonta){
        
        long numerKonta = wyliczNumerKonta(PESEL);
        kontaBankowe.put(numerKonta, początkowyStanKonta);
        return numerKonta;
    }

    public static long zwróćStanKonta(long numerKonta) {
        return kontaBankowe.get(numerKonta);
    }

    // ---- FUNCKJE IMPLEMENTUJĄCE TRANZAKCJE ---- //

    public boolean obciążKonto(long numerKonta, long wartość){
        
        long obecnyStanKonta = kontaBankowe.get(numerKonta);
        long nowyStanKonta = obecnyStanKonta - wartość;
        
        if(nowyStanKonta < 0){
            return false; 
        }
        else {
            kontaBankowe.put(numerKonta, nowyStanKonta);
            long należnyPodatek = zwróćPodatekOdCenyZakładu(wartość);
            budżetPaństwa.zbierzPodatek(należnyPodatek);
            środkiFinansoweCentrali += ( wartość - należnyPodatek );
            return true;
        }
    }
    
    private void obciążCentrale(long wartość){
        
        if(środkiFinansoweCentrali < wartość){
            long wymaganaSubwencja = wartość - środkiFinansoweCentrali;
            środkiFinansoweCentrali = 0L;
            budżetPaństwa.przekażSubwencję(wymaganaSubwencja);
        } else {
            środkiFinansoweCentrali -= wartość;
        }
    }

    public void wpłaćNaKonto(long numerKonta, long wartość){

        obciążCentrale(wartość);
        long należnyPodatek = zwróćPodatekOdWygranej(wartość);
        budżetPaństwa.zbierzPodatek(należnyPodatek);
        kontaBankowe.put(numerKonta, kontaBankowe.get(numerKonta) + wartość - należnyPodatek);
    }

    public void wpłaćNaKontoBezPodatku(long numerKonta, long wartość){

        obciążCentrale(wartość);
        kontaBankowe.put(numerKonta, kontaBankowe.get(numerKonta) + wartość);
    }

    // ---- FUNCKJE WYPISUJĄCE ---- //

    public static long zwróćPodatekOdCenyZakładu(long wartość){

        return ( wartość * PROCENT_PODATKU_OD_ZAKŁADU ) / 100;
    }

    public static long zwróćPodatekOdWygranej(long wartość){

        return ( wartość * PROCENT_PODATKU_OD_WYGRANEJ ) / 100;
    }   

    public void wypiszŚrodkiCentrali(){
         
        System.out.println("ŚRODKI CENTRALI: " + formatujKwotę(środkiFinansoweCentrali));
    }

    public static String formatujKwotę(long kwota) { 
        
        long złotówek = kwota;
        long groszy = złotówek % 100;
        złotówek = złotówek / 100;
        return "" + złotówek + "zł" + " " + groszy + "gr";
    }
}   
