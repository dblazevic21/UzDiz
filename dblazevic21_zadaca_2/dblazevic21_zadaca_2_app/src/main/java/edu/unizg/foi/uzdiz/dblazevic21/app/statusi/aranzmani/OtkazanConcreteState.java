package edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class OtkazanConcreteState implements AranzmaniState 
{
    @Override
    public void azuriraj(Aranzmani aranzman, int brojRezervacija) 
    {
    	
    }

    @Override
    public void otkazi(Aranzmani aranzman) { }

    @Override
    public String getNaziv() 
    {
        return "OTKAZAN";
    }
}