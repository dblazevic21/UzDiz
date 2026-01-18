package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.unizg.foi.uzdiz.dblazevic21.app.cor.CleanupHandlerCoR;
import edu.unizg.foi.uzdiz.dblazevic21.app.cor.ExecuteHandlerCoR;
import edu.unizg.foi.uzdiz.dblazevic21.app.cor.ExtractOznakaHandlerCoR;
import edu.unizg.foi.uzdiz.dblazevic21.app.cor.HandlerZaCoR;
import edu.unizg.foi.uzdiz.dblazevic21.app.cor.KomandaContext;
import edu.unizg.foi.uzdiz.dblazevic21.app.cor.SnapshotHandlerCoR;
import edu.unizg.foi.uzdiz.dblazevic21.app.memento.AranzmanCaretaker;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public abstract class KomandaCreator 
{
	private static final Set<String> PER_ARANZMAN = Set.of("DRTA", "ORTA", "OTA", "PTAR", "UPTAR", "VSTAR");
	
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
        
        if (PER_ARANZMAN.contains(naziv) && caretaker != null)
    	{
    		KomandaContext context = new KomandaContext(
    				trimmed,
    				naziv,
    				komanda,
    				aranzmani,
    				caretaker
			);
    		
    		HandlerZaCoR extractOznakaHandler = new ExtractOznakaHandlerCoR();
    		HandlerZaCoR snapshotHandler = new SnapshotHandlerCoR();
    		HandlerZaCoR executeHandler = new ExecuteHandlerCoR();
    		HandlerZaCoR cleanupHandler = new CleanupHandlerCoR();
    		
    		extractOznakaHandler.setSljedeci(snapshotHandler);
    		snapshotHandler.setSljedeci(executeHandler);
    		executeHandler.setSljedeci(cleanupHandler);
    		
    		extractOznakaHandler.handle(context);
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