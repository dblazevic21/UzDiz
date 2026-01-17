package edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class UPripremiConcreteState implements AranzmaniState 
{
    @Override
    public void azuriraj(Aranzmani aranzman, int ignoriran) 
    {
    	int broj = aranzman.getBrojJedinstvenihAktivnihRezervacija();
        if (broj >= aranzman.getMinBrojPutnika() && broj <= aranzman.getMaksBrojPutnika()) 
        {
            aranzman.setStatus(new AktivanConcreteState());
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
        return "U PRIPREMI";
    }
}