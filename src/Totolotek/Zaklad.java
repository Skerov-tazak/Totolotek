package Totolotek;
import java.util.Arrays;

public class Zaklad {

    // ---- STAŁE ---- //

    private static final int DŁUGOŚĆ_ZAKŁADU = 6;

    // ---- ZMIENNE ---- //

    private int[] obstawioneLiczby = new int[DŁUGOŚĆ_ZAKŁADU];

    private boolean czyAnulowany;

    // ---- KONSTRUKTORY ---- //

    private Zaklad(){}

    public static Zaklad wypełnijZakład(int... pola){
        
        if(pola == null)
            throw new IllegalArgumentException();

        Zaklad nowyZakład = new Zaklad();
        
        if(pola.length != DŁUGOŚĆ_ZAKŁADU){
            nowyZakład.czyAnulowany = true;
        }else {
            for(int i = 0; i < DŁUGOŚĆ_ZAKŁADU; i++){
                if(pola[i] > Losowanie.zwróćMaksymalnyNumerek()){
                    nowyZakład.czyAnulowany = true;
                    return nowyZakład;
                }else{
                    nowyZakład.obstawioneLiczby[i] = pola[i];
                }
            }
            Arrays.sort(nowyZakład.obstawioneLiczby);
        }

        return nowyZakład;
    }

    public static Zaklad stwórzLosowy(){

        Zaklad nowyZakład = new Zaklad();
        nowyZakład.czyAnulowany = false;

        nowyZakład.obstawioneLiczby = Losowanie.generujLosoweNumerki();

        Arrays.sort(nowyZakład.obstawioneLiczby);
        return nowyZakład;
    }

    // ---- FUNKCJE ZWRACAJĄCE ---- //

    public static int długośćZakładu(){
        return DŁUGOŚĆ_ZAKŁADU;
    }

    public boolean czyAnulowany(){
        return czyAnulowany;
    }

    public int[] zwróćObstawioneLiczby(){
        return obstawioneLiczby;
    }

    // ---- FUNKCJE WYPISUJĄCE ---- //

    public void wypiszZakład() { 
        
        if(czyAnulowany())
            System.out.print("Zakład anulowany!!");

        for(int x : obstawioneLiczby) { 
            if(x < 10)
                System.out.print(" ");
            System.out.print(" " + x);

        }
    }

    @Override
    public String toString() {
        
        if(czyAnulowany())
            return "Zakład Anulowany!";

        String napis = "";

        for(int x : obstawioneLiczby) { 
            if(x < 10)
                napis += " ";
            napis += " " + x;
        }

        return napis;

    }
}
