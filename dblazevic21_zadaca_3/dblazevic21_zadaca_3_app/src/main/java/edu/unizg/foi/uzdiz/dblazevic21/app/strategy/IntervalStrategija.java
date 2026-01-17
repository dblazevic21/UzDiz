package edu.unizg.foi.uzdiz.dblazevic21.app.strategy;

import java.time.LocalDateTime;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;

public class IntervalStrategija 
{
    final LocalDateTime start;
    final LocalDateTime kraj;

    IntervalStrategija(LocalDateTime start, LocalDateTime kraj) 
    {
        if (start != null && kraj != null && kraj.isBefore(start)) 
        {
            this.start = kraj;
            this.kraj = start;
        }
        else 
        {
            this.start = start;
            this.kraj = kraj;
        }
    }

    boolean preklapaSe(IntervalStrategija ostalo) 
    {
        if (ostalo == null || start == null || kraj == null || ostalo.start == null || ostalo.kraj == null) return false;
        return !start.isAfter(ostalo.kraj) && !ostalo.start.isAfter(kraj);
    }
    
    public static String keyOsobe(Rezervacija r) 
	{
        String ime = (r.getIme() == null) ? "" : r.getIme().trim().toLowerCase();
        String prezime = (r.getPrezime() == null) ? "" : r.getPrezime().trim().toLowerCase();
        return ime + "_" + prezime;
    }
}
