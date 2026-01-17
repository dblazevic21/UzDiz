package edu.unizg.foi.uzdiz.dblazevic21.app.command;

import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.memento.AranzmanCaretaker;
import edu.unizg.foi.uzdiz.dblazevic21.app.memento.AranzmanMemento;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class VstarReceiver
{
    private final Map<Integer, Aranzmani> aranzmani;
    private final AranzmanCaretaker caretaker;

    public VstarReceiver(Map<Integer, Aranzmani> aranzmani, AranzmanCaretaker caretaker)
    {
        this.aranzmani = aranzmani;
        this.caretaker = caretaker;
    }

    public String vrati(int oznaka)
    {
        if (caretaker == null) return "Greška: spremište nije inicijalizirano.";

        AranzmanMemento m = caretaker.get(oznaka);
        if (m == null)
        {
            return "Greška: u spremištu ne postoji aranžman s oznakom " + oznaka + ".";
        }

        Aranzmani stari = aranzmani.get(oznaka);

        Aranzmani spremljeno = new Aranzmani();
        spremljeno.restoreMemento(m);
        spremljeno.setOznaka(oznaka);

        if (stari != null)
        {
            spremljeno.kopirajPretplateIz(stari);
        }

        aranzmani.put(oznaka, spremljeno);

        spremljeno.notifyObservers("VSTAR: vraćeno stanje aranžmana i rezervacija iz spremišta.");

        return "Vraćen aranžman " + oznaka + " i njegove rezervacije iz spremišta.";
    }
}
