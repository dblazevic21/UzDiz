package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.memento.AranzmanCaretaker;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public abstract class KomandaCreator 
{

    protected final Map<Integer, Aranzmani> aranzmani;
    protected final AranzmanCaretaker caretaker;
    protected final List<Komanda> registriraneKomande = new ArrayList<>();

    protected KomandaCreator(Map<Integer, Aranzmani> aranzmani)
    {
        this(aranzmani, null);
    }
    
    protected KomandaCreator(Map<Integer, Aranzmani> aranzmani, AranzmanCaretaker caretaker) 
    {
        this.aranzmani = aranzmani;
        this.caretaker = caretaker;
        registrirajKomande();
    }

    protected abstract Komanda kreirajKomanda(String naziv);
    
    protected abstract void registrirajKomande();

    public void izvrsiKomandu(String unos) 
    {
        if (unos == null || unos.isBlank()) 
        {
            System.out.println("Prazan unos komande.");
            return;
        }

        String trimmed = unos.trim();
        String[] dijelovi = trimmed.split("\\s+", 2);
        String naziv = dijelovi[0].toUpperCase();

        Komanda komanda = kreirajKomanda(naziv);
        if (komanda == null) 
        {
            System.out.println("Nepoznata komanda: " + naziv);
            return;
        }
        
        komanda.izvrsi(trimmed);
    }
    
    protected void ispisiDostupneKomande()
    {
    	if (registriraneKomande.isEmpty()) 
    	{
            System.out.println("Nema registriranih komandi.");
            return;
        }
        StringBuilder sb = new StringBuilder("Nepoznata komanda. Lista komandi: ");
        for (int i = 0; i < registriraneKomande.size(); i++) 
        {
            if (i > 0)
            {
            	sb.append(", ");
            }
            sb.append(registriraneKomande.get(i).getNaziv());
        }
        sb.append('.');
        System.out.println(sb.toString());
    }
}