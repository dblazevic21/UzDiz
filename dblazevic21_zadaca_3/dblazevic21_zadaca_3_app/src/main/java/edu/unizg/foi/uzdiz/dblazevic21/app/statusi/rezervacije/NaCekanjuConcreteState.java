package edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;

public class NaCekanjuConcreteState implements RezervacijeState 
{
	@Override
    public void obradi(Rezervacija rezervacija) { }

    @Override
    public void otkazi(Rezervacija rezervacija) 
    {
        rezervacija.setStatus(new OtkazanaConcreteState());
    }

    @Override
    public void aktiviraj(Rezervacija rezervacija) 
    {
        rezervacija.setStatus(new AktivnaConcreteState());
    }

    @Override
    public void staviNaCekanje(Rezervacija rezervacija) { }

    @Override
    public void odgodi(Rezervacija rezervacija) 
    {
        rezervacija.setStatus(new OdgodenaConcreteState());
    }

    @Override
    public String getNaziv() 
    {
        return "NA ČEKANJU";
    }
}
