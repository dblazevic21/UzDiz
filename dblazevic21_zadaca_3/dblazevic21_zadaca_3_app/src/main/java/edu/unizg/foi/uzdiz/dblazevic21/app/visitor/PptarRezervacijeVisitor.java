package edu.unizg.foi.uzdiz.dblazevic21.app.visitor;

import java.util.ArrayList;
import java.util.List;

import edu.unizg.foi.uzdiz.dblazevic21.app.component.TuristickiVisitor;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;

public class PptarRezervacijeVisitor implements TuristickiVisitor 
{
    private final String trazeno;
    private final List<Rezervacija> pogodene = new ArrayList<>();

    public PptarRezervacijeVisitor(String trazeno)
    {
        this.trazeno = trazeno;
    }

    @Override
    public void visit(Aranzmani a) 
    {
    	
    }

    @Override
    public void visit(Rezervacija r) 
    {
        String ime = r.getIme() == null ? "" : r.getIme();
        String prezime = r.getPrezime() == null ? "" : r.getPrezime();

        if (ime.contains(trazeno) || prezime.contains(trazeno))
        {
            pogodene.add(r);
        }
    }

    public List<Rezervacija> getPogodene()
    {
        return pogodene;
    }
}

