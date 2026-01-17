package edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class AktivanConcreteState implements AranzmaniState 
{
	@Override
	public void azuriraj(Aranzmani aranzman, int ignoriran) 
	{
	    int broj = aranzman.getBrojJedinstvenihAktivnihRezervacija();
	    if (broj < aranzman.getMinBrojPutnika())
	    {
	        aranzman.setStatus(new UPripremiConcreteState());
	    } 
	    else if (broj > aranzman.getMaksBrojPutnika())
	    {
	        aranzman.setStatus(new PopunjenConcreteState());
	    }
	}

    @Override
    public void otkazi(Aranzmani aranzman) 
    {
        aranzman.setStatus(new OtkazanConcreteState());
    }

    @Override
    public String getNaziv() 
    {
        return "AKTIVAN";
    }
}