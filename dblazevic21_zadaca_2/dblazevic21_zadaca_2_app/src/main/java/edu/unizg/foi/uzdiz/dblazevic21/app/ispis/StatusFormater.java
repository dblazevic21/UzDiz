package edu.unizg.foi.uzdiz.dblazevic21.app.ispis;

import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.RezervacijeState;

public class StatusFormater 
{
    public static String statusOznaka(RezervacijeState s) 
    {
        if (s == null) return "-";
        return s.getNaziv();
    }
}
