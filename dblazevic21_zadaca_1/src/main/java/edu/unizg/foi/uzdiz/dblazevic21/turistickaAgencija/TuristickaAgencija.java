package edu.unizg.foi.uzdiz.dblazevic21.turistickaAgencija;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.komande.KomandaConcreteCreator;
import edu.unizg.foi.uzdiz.dblazevic21.komande.KomandaCreator;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;

public class TuristickaAgencija 
{

    private static volatile TuristickaAgencija INSTANCE;

    private Map<Integer, Aranzmani> aranzmani = new HashMap<>();
    private KomandaCreator komandaCreator;

    private TuristickaAgencija() 
    {
        this.komandaCreator = new KomandaConcreteCreator(aranzmani);
    }

    public static TuristickaAgencija getInstance()
    {
        if (INSTANCE == null) 
        {
            synchronized (TuristickaAgencija.class)
            {
                if (INSTANCE == null) 
                {
                    INSTANCE = new TuristickaAgencija();
                }
            }
        }
        return INSTANCE;
    }

    public void setAranzmani(Map<Integer, Aranzmani> aranzmani) 
    {
        this.aranzmani = (aranzmani != null) ? aranzmani : new HashMap<>();
        this.komandaCreator = new KomandaConcreteCreator(this.aranzmani);
    }

    public Map<Integer, Aranzmani> getAranzmani() 
    {
        return aranzmani;
    }

    public void izvrsiKomandu(String unos)
    {
        if (unos == null || unos.isBlank()) 
        {
            System.out.println("Neispravna komanda.");
            return;
        }

        String izrezan = unos.trim();

        komandaCreator.izvrsiKomandu(izrezan);
    }
}