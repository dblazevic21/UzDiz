package edu.unizg.foi.uzdiz.dblazevic21.app.ispis;

import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani.AranzmaniState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.RezervacijeState;

public class StatusFormater 
{
    public static String statusOznaka(RezervacijeState s) 
    {
        if (s == null) return "-";
        return s.getNaziv();
    }
    
    public static String statusOznakaAranzmana(AranzmaniState s)
    {
    	if (s == null) return "-";
    	return s.getNaziv();
    }
}
