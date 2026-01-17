package edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public interface AranzmaniState 
{
    void azuriraj(Aranzmani aranzman, int brojRezervacija);
    void otkazi(Aranzmani aranzman);
    String getNaziv();
}