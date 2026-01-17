package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.memento.AranzmanCaretaker;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class KomandaConcreteCreator extends KomandaCreator 
{

    public KomandaConcreteCreator(Map<Integer, Aranzmani> aranzmani) 
    {
        super(aranzmani);
    }
    
    public KomandaConcreteCreator(Map<Integer, Aranzmani> aranzmani, AranzmanCaretaker caretaker)
    {
        super(aranzmani, caretaker);
    }

    @Override
    protected Komanda kreirajKomanda(String naziv)
    {
        return switch (naziv) {
            case "DRTA" -> new DrtaKomanda(aranzmani);
            case "ORTA" -> new OrtaKomanda(aranzmani);
            case "ITAK" -> new ItakKomanda(aranzmani);
            case "IRTA" -> new IrtaKomanda(aranzmani);
            case "ITAP" -> new ItapKomanda(aranzmani);
            case "IRO"  -> new IroKomanda(aranzmani);
            case "OTA"  -> new OtaKomanda(aranzmani);
            case "IP"   -> new IpKomanda();
            case "BP"   -> new BpKomanda(aranzmani);
            case "UP"   -> new UpKomanda(aranzmani);
            case "ITAS" -> new ItasKomanda(aranzmani);
            case "STATS"-> new StatsKomanda(aranzmani);
            case "PPTAR"-> new PptarKomanda(aranzmani);
            case "PSTAR"-> new PstarKomanda(aranzmani, caretaker);
            case "VSTAR"-> new VstarKomanda(aranzmani, caretaker); 
            case "PTAR" -> new PtarKomanda(aranzmani);
            case "UPTAR"-> new UptarKomanda(aranzmani);
            default     -> null;
        };
    }
    
    @Override
    protected void registrirajKomande() 
	{
    	registriraneKomande.clear();
        registriraneKomande.add(new DrtaKomanda(aranzmani));
        registriraneKomande.add(new IrtaKomanda(aranzmani));
        registriraneKomande.add(new IroKomanda(aranzmani));
        registriraneKomande.add(new OrtaKomanda(aranzmani));
        registriraneKomande.add(new ItakKomanda(aranzmani));
        registriraneKomande.add(new ItapKomanda(aranzmani));
        registriraneKomande.add(new OtaKomanda(aranzmani));
        registriraneKomande.add(new IpKomanda());
        registriraneKomande.add(new BpKomanda(aranzmani));
        registriraneKomande.add(new UpKomanda(aranzmani));
        registriraneKomande.add(new ItasKomanda(aranzmani));
        registriraneKomande.add(new StatsKomanda(aranzmani));
        registriraneKomande.add(new PptarKomanda(aranzmani));
        registriraneKomande.add(new PstarKomanda(aranzmani, caretaker));
        registriraneKomande.add(new VstarKomanda(aranzmani, caretaker));
        registriraneKomande.add(new PtarKomanda(aranzmani));
        registriraneKomande.add(new UptarKomanda(aranzmani));
	}
}
