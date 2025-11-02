package edu.unizg.foi.uzdiz.dblazevic21.ispis;

import edu.unizg.foi.uzdiz.dblazevic21.enumeracije.StatusRezervacije;

public class StatusFormater 
{
    public static String statusOznaka(StatusRezervacije s) 
    {
        if (s == null) return "-";
        return switch (s) 
		{
            case PRIMLJENA -> "PRIMLJENA";
            case AKTIVNA -> "AKTIVNA";
            case NA_CEKANJU -> "NA ČEKANJU";
            case OTKAZANA -> "OTKAZANA";
        };
    }
}
