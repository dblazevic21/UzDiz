package edu.unizg.foi.uzdiz.dblazevic21.app.component;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;

public interface TuristickiVisitor 
{
	void visit(Aranzmani a);
	void visit(Rezervacija r);
}
