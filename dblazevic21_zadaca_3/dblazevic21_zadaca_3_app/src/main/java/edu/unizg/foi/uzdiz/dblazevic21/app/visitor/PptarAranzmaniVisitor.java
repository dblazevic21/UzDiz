package edu.unizg.foi.uzdiz.dblazevic21.app.visitor;

import java.util.ArrayList;
import java.util.List;

import edu.unizg.foi.uzdiz.dblazevic21.app.component.TuristickiVisitor;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;

public class PptarAranzmaniVisitor implements TuristickiVisitor 
{
    private final String trazeno;
    private final List<Aranzmani> pogodeni = new ArrayList<>();

    public PptarAranzmaniVisitor(String trazeno)
    {
        this.trazeno = trazeno;
    }

    @Override
    public void visit(Aranzmani a) 
    {
        String naziv = a.getNaziv() == null ? "" : a.getNaziv();
        String program = a.getProgram() == null ? "" : a.getProgram();

        if (naziv.contains(trazeno) || program.contains(trazeno)) 
        {
            pogodeni.add(a);
        }
    }

    @Override
    public void visit(Rezervacija r) 
    {
    	
    }

    public List<Aranzmani> getPogodeni() 
    {
        return pogodeni;
    }
}

