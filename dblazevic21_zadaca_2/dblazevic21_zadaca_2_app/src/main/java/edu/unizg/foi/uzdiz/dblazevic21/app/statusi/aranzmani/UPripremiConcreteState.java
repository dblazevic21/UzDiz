package edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class UPripremiConcreteState implements AranzmaniState 
{
    @Override
    public void azuriraj(Aranzmani aranzman, int brojRezervacija) 
    {
        if (brojRezervacija >= aranzman.getMinBrojPutnika() && brojRezervacija <= aranzman.getMaksBrojPutnika()) 
        {
            aranzman.setStatus(new AktivanConcreteState());
        } 
        else if (brojRezervacija > aranzman.getMaksBrojPutnika()) 
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