package edu.unizg.foi.uzdiz.dblazevic21.app.cor;

import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.komande.Komanda;
import edu.unizg.foi.uzdiz.dblazevic21.app.memento.AranzmanCaretaker;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class KomandaContext 
{
	public String unos;
	public String nazivKomande;
	public Komanda komanda;
	
	public Integer oznakaAranzmana;
	
	public Map<Integer, Aranzmani> aranzmani;
	public AranzmanCaretaker caretaker;
	
	public KomandaContext (String unos, String nazivKomande, Komanda komanda, Map<Integer, Aranzmani> aranzmani, AranzmanCaretaker caretaker)
	{
		this.unos = unos;
		this.nazivKomande = nazivKomande;
		this.komanda = komanda;
		this.aranzmani = aranzmani;
		this.caretaker = caretaker;
	}
}
