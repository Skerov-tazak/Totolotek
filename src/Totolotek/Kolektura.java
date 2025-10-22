package Totolotek;
import java.util.*;

public class Kolektura {

        // ---- ZMIENNE ---- //

        private static final HashMap<Integer, Kolektura> listaKolektur = new HashMap<>();

        public static Kolektura zwróćKolekturęNumer(int numerKolekturyDoZwrócenia) {
                return listaKolektur.get(numerKolekturyDoZwrócenia);
        }

        public static int liczbaWszystkichKolektur() {
                return Centrala.zwróćAktualnyNumerKolektury();
        }

        private int numerKolektury;

        private Bank bank;

        private Centrala centrala;

        // ---- KONSTRUKTOR ---- //

        public Kolektura(Bank bank, Centrala centrala) {
                this.bank = bank;
                this.centrala = centrala;
                numerKolektury = Centrala.zwróćAktualnyNumerKolektury();
                centrala.zwiększNumerKolektury();
                listaKolektur.put(numerKolektury, this);
        }

        // ---- FUKNCJE SPRZEDAJĄCE KUPONY ---- //

        public Kupon sprzedajKupon(int ileZakładów, int ileLosowań, long numerKonta){

                if(ileLosowań == 0 || ileZakładów == 0)
                        throw new IllegalArgumentException("Niepoprawny zakup kuponu");

                if(bank.obciążKonto(numerKonta, ileLosowań * ileZakładów * Centrala.zwróćCenęZakładu()) == false){
                        return null;
                }
                else 
                {
                        centrala.zwiększNumerKuponu();

                        Kupon kupon = Kupon.generujLosowy(ileZakładów, ileLosowań, numerKolektury, Centrala.zwróćAktualnyNumerKuponu());
                        centrala.dodajKupon(kupon);
                        return kupon;
                }
        }

        public Kupon sprzedajKupon(Blankiet blankiet, long numerKonta) {


                if(blankiet == null)
                        throw new IllegalArgumentException("Niepoprawny zakup kuponu");


                if(bank.obciążKonto(numerKonta, blankiet.obliczKoszt()) == false){
                        return null;
                }
                else 
                {
                        centrala.zwiększNumerKuponu();
                        Kupon kupon = Kupon.generujBlankietowo(blankiet, numerKolektury, Centrala.zwróćAktualnyNumerKuponu());
                        centrala.dodajKupon(kupon);
                        return kupon;
                }
        }

        // ---- FUNKCJA DO ODBIERANIA NAGRÓÐ ---- // 

        public void odbierzNagrodę(Kupon dokumentUprawniający, long numerKonta) {
                
                if(dokumentUprawniający.zwróćNumerSprzedającejKolektury() != numerKolektury)
                        return;

                centrala.wypłaćNagrodę(dokumentUprawniający, numerKonta);
        }

        @Override
        public int hashCode() {
        
                return Objects.hash(numerKolektury);
        }

        // Dwie kolektury nie mogą być identyczne...
        @Override
        public boolean equals(Object obj) {
        
                if(obj == this)
                        return true;
                else 
                        return false;
        }

}
