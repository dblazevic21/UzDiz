package edu.unizg.foi.uzdiz.dblazevic21.komande;

import edu.unizg.foi.uzdiz.dblazevic21.turistickaAgencija.TuristickaAgencija;

public interface Komanda 
{
	void izvrsi(String unos, TuristickaAgencija agencija);
	String getNaziv();
}
