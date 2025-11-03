package edu.unizg.foi.uzdiz.dblazevic21.turistickaAgencija;

import edu.unizg.foi.uzdiz.dblazevic21.komande.*;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TuristickaAgencija 
{

    private static volatile TuristickaAgencija INSTANCE;

    private final Map<String, KomandaBridge> komande = new HashMap<>();
    private Map<Integer, Aranzmani> aranzmani = new HashMap<>();

    private TuristickaAgencija() 
    {
        registrirajKomande();
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
        registrirajKomande();
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
        String veliko = izrezan.toUpperCase(Locale.ROOT);
        String naziv = veliko.split("\\s+", 2)[0];

        KomandaBridge k = komande.get(naziv);
        
        if (k == null) 
        {
            System.out.println("Nepoznata komanda. Lista komandi: ITAK, ITAP, IRO, IRTA, ORTA, DRTA.");
            return;
        }
        k.izvrsiKomandu(unos);
    }

    private void registrirajKomande() 
    {
        komande.clear();
        komande.put("DRTA", new IzvrsiteljKomande(new DrtaKomanda(aranzmani)));
        komande.put("IRTA", new IzvrsiteljKomande(new IrtaKomanda(aranzmani)));
        komande.put("IRO", new IzvrsiteljKomande(new IroKomanda(aranzmani)));
        komande.put("ORTA", new IzvrsiteljKomande(new OrtaKomanda(aranzmani)));
        komande.put("ITAK", new IzvrsiteljKomande(new ItakKomanda(aranzmani)));
        komande.put("ITAP", new IzvrsiteljKomande(new ItapKomanda(aranzmani)));
    }
}