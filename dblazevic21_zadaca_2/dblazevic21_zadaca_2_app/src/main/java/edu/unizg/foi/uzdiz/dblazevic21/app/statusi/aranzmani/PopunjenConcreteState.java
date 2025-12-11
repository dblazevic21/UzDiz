package edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class PopunjenConcreteState implements AranzmaniState 
{
    @Override
    public void azuriraj(Aranzmani aranzman, int brojRezervacija) 
    {
        if (brojRezervacija < aranzman.getMinBrojPutnika()) 
        {
            aranzman.setStatus(new UPripremiConcreteState());
        } 
        else if (brojRezervacija <= aranzman.getMaksBrojPutnika()) 
        {
            aranzman.setStatus(new AktivanConcreteState());
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
        return "POPUNJEN";
    }
}