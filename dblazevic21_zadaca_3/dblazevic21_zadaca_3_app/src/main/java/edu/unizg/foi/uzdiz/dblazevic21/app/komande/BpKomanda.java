package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.lib.facade.TuristickaFacade;

public class BpKomanda implements Komanda
{
    private final Map<Integer, Aranzmani> aranzmani;

    public BpKomanda(Map<Integer, Aranzmani> aranzmani)
    {
        this.aranzmani = aranzmani;
    }

    @Override
    public String getNaziv()
    {
        return "BP";
    }

    @Override
    public void izvrsi(String unos)
    {
        String odrezan = (unos == null) ? "" : unos.trim();

        Pattern p = Pattern.compile("^BP\\s+([AR])$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher m = p.matcher(odrezan);

        if (!m.matches())
        {
            System.out.println("Nepoznata komanda. Upotrijebite: BP A | BP R");
            return;
        }

        char odabir = Character.toUpperCase(m.group(1).charAt(0));

        TablicaPrinter.ispisUnosa(unos);
        switch (odabir)
        {
	        case 'A' -> {
	            TuristickaFacade.getInstance().resetirajAranzmane(); 
	            aranzmani.clear();
	            Rezervacije.getInstance().ocistiSve(aranzmani);
	            System.out.println("Obrisani svi aranžmani i sve rezervacije (BP A).");
	        }
	
	        case 'R' -> {
	            TuristickaFacade.getInstance().resetirajRezervacije();
	            Rezervacije.getInstance().ocistiRezervacije(aranzmani);
	            aranzmani.values().forEach(Aranzmani::azurirajStatuseRezervacija);
	            System.out.println("Obrisane sve rezervacije (BP R).");
	        }
            default -> {
                System.out.println("Greška: dozvoljene vrijednosti su samo A ili R.");
            }
        }
    }
}
