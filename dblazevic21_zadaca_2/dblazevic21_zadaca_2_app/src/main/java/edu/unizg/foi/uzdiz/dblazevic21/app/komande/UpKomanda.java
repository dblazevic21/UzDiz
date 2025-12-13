package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

public class UpKomanda implements Komanda 
{
		@Override
	public String getNaziv() 
	{
		return "UP";
	}

	@Override
	public void izvrsi(String unos) 
	{
		String odrezan = (unos == null) ? "" : unos.trim();
		
		
	}
}
