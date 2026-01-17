package edu.unizg.foi.uzdiz.dblazevic21.app.strategy;

import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public interface UpravljanjeRezervacijamaStrategija 
{
	void primijeni(Map<Integer, Aranzmani> aranzmani);
	default void primijeniNakonKapaciteta(Map<Integer, Aranzmani> aranzmani) { }
    String naziv();
}
