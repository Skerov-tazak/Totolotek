package Totolotek;

public class BudzetPanstwa {

    // ---- ZMIENNE ---- //

    private static long przekazaneSubwencje = 0L;

    private static long zebranePodatki = 0L;

    // ---- FUNKCJE WYPISUJĄCE ---- //

    public static void wypiszPrzekazaneSubwencje(){
        
        System.out.println("WARTOŚĆ PRZEKAZANYCH SUBWENCJI: " + Bank.formatujKwotę(przekazaneSubwencje));
    }

    public static void wypiszZebranePodatki(){

        System.out.println("WARTOŚĆ ZEBRANEGO PODATKU: " + Bank.formatujKwotę(zebranePodatki));
    }

    // ---- OPERACJE NA ZMIENNYCH ---- //

    public void zbierzPodatek(long wysokośćZebranegoPodatku){
        zebranePodatki += wysokośćZebranegoPodatku;
    }

    public void przekażSubwencję(long wysokośćPrzekazanejSubwencji){
        przekazaneSubwencje += wysokośćPrzekazanejSubwencji;
    }
}
