package edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;

public class OtkazanaConcreteState implements RezervacijeState 
{
	@Override
    public void obradi(Rezervacija rezervacija) 
    {
        
    }

    @Override
    public void otkazi(Rezervacija rezervacija) 
    {
       
    }

    @Override
    public void aktiviraj(Rezervacija rezervacija) 
    {
        
    }

    @Override
    public void staviNaCekanje(Rezervacija rezervacija) 
    {
        
    }

    @Override
    public void odgodi(Rezervacija rezervacija) 
    {
        
    }

    @Override
    public String getNaziv() 
    {
        return "OTKAZANA";
    }
}
