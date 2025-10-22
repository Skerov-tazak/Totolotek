package Test;

import Totolotek.*;
import static org.junit.Assert.assertEquals;

import org.junit.*;

public class UnitTests {

    @Test
    public void niepoprawneDaneNullTest() {

        int liczbaUdanychTestów = 0;

        try {

            new Minimalista("X", "Y", 1, 100000, null);

        } catch (IllegalArgumentException e) {
            liczbaUdanychTestów++;
        }
        try {

            new Staloblankietowy("X", "Y", 1, 100000, 1, null, null);

        } catch (IllegalArgumentException e) {
            liczbaUdanychTestów++;
        }
        try {
            Centrala.otwórzKolekturę();
            Blankiet blankiet = Totolotek.generujBlankiet();
            Kolektura[] ulubione = new Kolektura[1];
            ulubione[0] = Kolektura.zwróćKolekturęNumer(0);

            new Staloblankietowy("X", "Y", 1, 100000L, 0, blankiet, ulubione);

        } catch (IllegalArgumentException e) {
            liczbaUdanychTestów++;
        }
        try {

            Kupon.generujLosowy(10, 10, 2, 2);

        } catch (IllegalArgumentException e) {
            liczbaUdanychTestów++;
        }
        try {

            Kupon.generujBlankietowo(null, 0, 1);

        } catch (IllegalArgumentException e) {
            liczbaUdanychTestów++;
        }
        try {

            Kupon.generujLosowy(10, 10, 0, 2);

        } catch (IllegalArgumentException e) {
            liczbaUdanychTestów++;
        }   
        try {
            Zaklad test = Zaklad.wypełnijZakład(null);
        
        } catch (IllegalArgumentException e){
            liczbaUdanychTestów++;
        }

        assertEquals(6, liczbaUdanychTestów);
    }

    @Test
    public void niepoprawneDaneZakładTest() {

        int liczbaZdanychTestów = 0;

        Zaklad test = Zaklad.wypełnijZakład(1,2,3,4,5);
        
        if(test.czyAnulowany())
            liczbaZdanychTestów++;
        
        test = Zaklad.wypełnijZakład(2,3,4,5,50,1);

        if(test.czyAnulowany())
            liczbaZdanychTestów++;

        assertEquals(2, liczbaZdanychTestów);
        assertEquals("Zakład Anulowany!", test.toString());
        
    }

}
