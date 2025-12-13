package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import edu.unizg.foi.uzdiz.dblazevic21.app.csv.CsvZaAranzmane;
import edu.unizg.foi.uzdiz.dblazevic21.app.csv.CsvZaRezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.app.turistickaAgencija.TuristickaAgencija;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

import java.util.Map;

public class UpKomanda implements Komanda
{
    @Override
    public String getNaziv()
    {
        return "UP";
    }

    @Override
    public void izvrsi(String unos)
    {
        String odrezan = (unos == null) ? "" : unos.trim();
        if (odrezan.isEmpty())
        {
            System.out.println("Greška: nedostaje argument. Očekivano: UP [A|R] nazivDatoteke");
            return;
        }
        
        if (odrezan.toUpperCase().startsWith("UP"))
        {
            odrezan = odrezan.substring(2).trim();
        }

        if (odrezan.isEmpty())
        {
            System.out.println("Greška: nedostaje oznaka tipa (A ili R) i naziv datoteke.");
            return;
        }

        String[] dijelovi = odrezan.split("\\s+", 2);
        if (dijelovi.length < 2)
        {
            System.out.println("Greška: nedostaje naziv datoteke. Očekivano: UP [A|R] nazivDatoteke");
            return;
        }

        String tip = dijelovi[0].trim().toUpperCase();
        String nazivDatoteke = dijelovi[1].trim();

        if (nazivDatoteke.isEmpty())
        {
            System.out.println("Greška: naziv datoteke je prazan.");
            return;
        }

        izvrsiKomandu(tip, nazivDatoteke);
    }

	public void izvrsiKomandu(String tip, String nazivDatoteke)
	{
		Map<Integer, Aranzmani> aranzmani = TuristickaAgencija
                .getInstance()
                .getAranzmani();

        try
        {
            switch (tip)
            {
                case "A":
                    CsvZaAranzmane csvA = new CsvZaAranzmane();
                    csvA.ucitaj(nazivDatoteke, aranzmani);
                    System.out.println("Učitavanje aranžmana iz datoteke: " + nazivDatoteke + " završeno.");
                    break;

                case "R":
                    CsvZaRezervacije csvR = new CsvZaRezervacije();
                    csvR.ucitaj(nazivDatoteke, aranzmani);
                    System.out.println("Učitavanje rezervacija iz datoteke: " + nazivDatoteke + " završeno.");
                    break;

                default:
                    System.out.println("Greška: nepoznat tip \"" + tip + "\". Dozvoljeno: A (aranžmani), R (rezervacije).");
                    break;
            }
        }
        catch (Exception e)
        {
            System.out.println("Greška pri učitavanju iz datoteke \"" + nazivDatoteke + "\": " + e.getMessage());
        }
	}
}
