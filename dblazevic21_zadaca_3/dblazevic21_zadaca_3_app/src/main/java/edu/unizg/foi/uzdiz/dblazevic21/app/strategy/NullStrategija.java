package edu.unizg.foi.uzdiz.dblazevic21.app.strategy;

import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class NullStrategija implements UpravljanjeRezervacijamaStrategija 
{
	@Override
	public void primijeni(Map<Integer, Aranzmani> aranzmani)
	{
		
	}
	
	@Override
	public String naziv()
	{
		return "NULL";
	}
}
