package edu.unizg.foi.uzdiz.dblazevic21.handleri;

import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacije;

public class IspisRezultataHandler 
{
	public void ispisiRezultate()
	{
		Rezervacije rezervacije = Rezervacije.getInstance();
		System.out.println("=== Rezervacije ===");
		rezervacije.ispisiRezervacije();
	}
}
