package edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;

public interface RezervacijeState 
{
	void obradi(Rezervacija rezervacija);
    void otkazi(Rezervacija rezervacija);
    void aktiviraj(Rezervacija rezervacija);
    void staviNaCekanje(Rezervacija rezervacija);
    void odgodi(Rezervacija rezervacija);
    String getNaziv();
}
