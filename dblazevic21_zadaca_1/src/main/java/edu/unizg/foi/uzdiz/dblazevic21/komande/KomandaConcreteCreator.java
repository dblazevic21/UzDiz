package edu.unizg.foi.uzdiz.dblazevic21.komande;

import java.util.Map;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;

public class KomandaConcreteCreator extends KomandaCreator 
{

    public KomandaConcreteCreator(Map<Integer, Aranzmani> aranzmani) 
    {
        super(aranzmani);
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
	}
}
