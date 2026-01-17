package edu.unizg.foi.uzdiz.dblazevic21.app.command;

import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.memento.AranzmanCaretaker;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class PstarReceiver 
{
    private final Map<Integer, Aranzmani> aranzmani;
    private final AranzmanCaretaker caretaker;

    public PstarReceiver(Map<Integer, Aranzmani> aranzmani, AranzmanCaretaker caretaker) 
    {
        this.aranzmani = aranzmani;
        this.caretaker = caretaker;
    }

    public String spremi(int oznaka) 
    {
        Aranzmani a = aranzmani.get(oznaka);
        
        if (a == null)
        {
        	return "Greška: ne postoji aranžman s oznakom " + oznaka + ".";
        }

        if (!caretaker.store(oznaka, a.createMemento())) 
        {
            return "Greška: aranžman " + oznaka + " je već spremljen u spremištu.";
        }

        return "Spremljen aranžman " + oznaka + " i njegove rezervacije u spremište.";
    }
}
