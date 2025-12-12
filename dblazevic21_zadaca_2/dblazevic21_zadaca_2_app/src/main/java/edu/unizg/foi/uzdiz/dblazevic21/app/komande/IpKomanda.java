package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

public class IpKomanda implements Komanda 
{
	@Override
	public String getNaziv() 
	{
		return "IP";
	}

	@Override
	public void izvrsi(String unos) 
	{
		System.out.println("Izvršena je IP komanda.");
	}
}
