package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

public class OtaKomanda implements Komanda 
{
		@Override
	public String getNaziv() 
	{
		return "OTA";
	}

	@Override
	public void izvrsi(String unos) 
	{
		System.out.println("Izvršena OTA komanda s unosom: " + unos);
		// Implementacija OTA komande ide ovdje
	}
}
