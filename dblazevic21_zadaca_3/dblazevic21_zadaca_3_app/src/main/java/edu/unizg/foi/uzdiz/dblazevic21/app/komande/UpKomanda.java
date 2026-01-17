package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.List;
import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.csv.CsvZaAranzmane;
import edu.unizg.foi.uzdiz.dblazevic21.app.csv.CsvZaRezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.IspisiGreskuApp;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.lib.facade.TuristickaFacade;

public class UpKomanda implements Komanda
{
	private final Map<Integer, Aranzmani> aranzmani;

    public UpKomanda(Map<Integer, Aranzmani> aranzmani)
    {
        this.aranzmani = aranzmani;
    }
	
    @Override
    public String getNaziv()
    {
        return "UP";
    }

    @Override
    public void izvrsi(String unos)
    {
        TuristickaFacade facade = TuristickaFacade.getInstance();
        facade.resetirajRezervacije();
        provjeriUnos(unos, facade);
    }

	public void provjeriUnos(String unos, TuristickaFacade facade) 
	{
		String odrezan = (unos == null) ? "" : unos.trim();

        if (odrezan.isEmpty())
        {
        	TablicaPrinter.ispisUnosa(unos);
            int rb = facade.getBrojGresaka();
            IspisiGreskuApp.ispisiGresku(
                rb,
                0,
                odrezan,
                List.of("Nedostaje argument. Očekivano: UP [A|R] nazivDatoteke"),
                "konzola"
            );
            return;
        }
        

        if (odrezan.toUpperCase().startsWith("UP"))
        {
            odrezan = odrezan.substring(2).trim();
        }

        if (odrezan.isEmpty())
        {
        	TablicaPrinter.ispisUnosa(unos);
            int rb = facade.getBrojGresaka();
            IspisiGreskuApp.ispisiGresku(
                rb,
                0,
                odrezan,
                List.of("Nedostaje oznaka tipa (A ili R) i naziv datoteke."),
                "konzola"
            );
            return;
        }

        String[] dijelovi = odrezan.split("\\s+", 2);
        if (dijelovi.length < 2)
        {
        	TablicaPrinter.ispisUnosa(unos);
            int rb = facade.getBrojGresaka();
            IspisiGreskuApp.ispisiGresku(
                rb,
                0,
                odrezan,
                List.of("Nedostaje naziv datoteke. Očekivano: UP [A|R] nazivDatoteke"),
                "konzola"
            );
            return;
        }

        String tip = dijelovi[0].trim().toUpperCase();
        String nazivDatoteke = dijelovi[1].trim();

        if (nazivDatoteke.isEmpty())
        {
            int rb = facade.getBrojGresaka();
            IspisiGreskuApp.ispisiGresku(
                rb,
                0,
                odrezan,
                List.of("Nema datoteke."),
                "konzola"
            );
            return;
        }
    	
        TablicaPrinter.ispisUnosa(unos);
        izvrsiKomandu(tip, nazivDatoteke, unos);
	}

    public void izvrsiKomandu(String tip, String nazivDatoteke, String unos)
    {
        TuristickaFacade facade = TuristickaFacade.getInstance();

        try
        {
            switch (tip)
            {
                case "A":
                    CsvZaAranzmane csvA = new CsvZaAranzmane();
                    csvA.ucitaj(nazivDatoteke, aranzmani);
                    Rezervacije.getInstance().azurirajStatuseRezervacija(aranzmani);
                    System.out.println("Učitavanje aranžmana iz datoteke: " + nazivDatoteke + " završeno.");
                    break;

                case "R":
                    CsvZaRezervacije csvR = new CsvZaRezervacije();
                    csvR.ucitaj(List.of(nazivDatoteke), aranzmani);
                    Rezervacije.getInstance().azurirajStatuseRezervacija(aranzmani);
                    System.out.println("Učitavanje rezervacija iz datoteke: " + nazivDatoteke + " završeno.");
                    break;

                default:
                    int rb = facade.getBrojGresaka();
                    IspisiGreskuApp.ispisiGresku(
                        rb,
                        0,
                        unos,
                        List.of("Nepoznat tip \"" + tip + "\". Dozvoljeno: A (aranžmani), R (rezervacije)."),
                        "konzola"
                    );
                    break;
            }
        }
        catch (Exception e)
        {
            int rb = facade.getBrojGresaka();
            IspisiGreskuApp.ispisiGresku(
                rb,
                0,
                unos,
                List.of("Greška pri učitavanju iz datoteke \"" + nazivDatoteke + "\": " + e.getMessage()),
                nazivDatoteke
            );
        }
    }
}
