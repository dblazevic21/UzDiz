package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.command.CommandInvoker;
import edu.unizg.foi.uzdiz.dblazevic21.app.command.PstarCommand;
import edu.unizg.foi.uzdiz.dblazevic21.app.command.PstarReceiver;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.memento.AranzmanCaretaker;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class PstarKomanda implements Komanda 
{
	private final Map<Integer, Aranzmani> aranzmani;

    private final AranzmanCaretaker caretaker;
    private final CommandInvoker invoker = new CommandInvoker();
    
    public PstarKomanda(Map<Integer, Aranzmani> aranzmani, AranzmanCaretaker caretaker)
    {
        this.aranzmani = aranzmani;
        this.caretaker = caretaker;
    }

    @Override
    public String getNaziv()
    {
        return "PSTAR";
    }

    @Override
    public void izvrsi(String unos) 
    {
        String s = (unos == null) ? "" : unos.trim();
        String[] dijelovi = s.split("\\s+");
        int oznaka = 0;
        
        if (dijelovi.length != 2) 
        {
            System.out.println("Greška: Očekivano: PSTAR oznaka");
            return;
        }
        
        TablicaPrinter.ispisUnosa(unos);
        
        try 
        {
            oznaka = Integer.parseInt(dijelovi[1]);
        } 
        catch (Exception e)
        {
            System.out.println("Greška: oznaka mora biti broj.");
            return;
        }

        PstarReceiver receiver = new PstarReceiver(aranzmani, caretaker);

        var komanda = new PstarCommand(receiver, oznaka);

        invoker.run(komanda);
    }
}
