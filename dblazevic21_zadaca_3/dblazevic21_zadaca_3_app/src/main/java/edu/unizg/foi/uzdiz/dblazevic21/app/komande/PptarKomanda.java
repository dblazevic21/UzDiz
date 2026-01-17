package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.List;
import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.app.visitor.PptarAranzmaniVisitor;
import edu.unizg.foi.uzdiz.dblazevic21.app.visitor.PptarRezervacijeVisitor;

public class PptarKomanda implements Komanda 
{

    private final Map<Integer, Aranzmani> aranzmani;

    public PptarKomanda(Map<Integer, Aranzmani> aranzmani) 
    {
        this.aranzmani = aranzmani;
    }

    @Override
    public String getNaziv() 
    {
        return "PPTAR";
    }

    @Override
    public void izvrsi(String unos) 
    {
        String s = (unos == null) ? "" : unos.trim();

        String[] dijelovi = s.split("\\s+", 3);
        if (dijelovi.length < 3) 
        {
            System.out.println("Greška: Očekivano: PPTAR [A|R] riječ");
            return;
        }
        
        TablicaPrinter.ispisUnosa(unos);

        String tip = dijelovi[1].trim().toUpperCase();
        String rijec = dijelovi[2];

        if (rijec.isEmpty()) 
        {
            System.out.println("Greška: Nedostaje riječ.");
            return;
        }

        provjeraUnosa(tip, rijec);
    }

	public void provjeraUnosa(String tip, String rijec) 
	{
		if (tip.equals("A")) 
        {
            var visitor = new PptarAranzmaniVisitor(rijec);

            for (Aranzmani a : aranzmani.values()) 
            {
                a.accept(visitor);
            }

            List<Aranzmani> pogodeni = visitor.getPogodeni();
            if (pogodeni.isEmpty()) 
            {
                System.out.println("Nema aranžmana koji sadrže \"" + rijec + "\".");
                return;
            }

            int[] sirine = {8, 60};

            ispisAranzmanaPretrazenog(rijec, pogodeni, sirine);

        }
        else if (tip.equals("R")) 
        {
            var visitor = new PptarRezervacijeVisitor(rijec);

            for (Aranzmani a : aranzmani.values())
            {
                a.accept(visitor); 
            }

            List<Rezervacija> pogodene = visitor.getPogodene();
            if (pogodene.isEmpty()) 
            {
                System.out.println("Nema rezervacija koje sadrže \"" + rijec + "\".");
                return;
            }

            int[] sirine = {15, 15, 20, 8, 15};

            ispisRezervacijaPretrazenog(rijec, pogodene, sirine);
        }
        else 
        {
            System.out.println("Greška: Tip mora biti A ili R.");
        }
	}

	public void ispisRezervacijaPretrazenog(String rijec, List<Rezervacija> pogodene, int[] sirine) 
	{
		System.out.println();
		System.out.println("Rezervacije koje sadrže \"" + rijec + "\":");

		TablicaPrinter.printajSeperatorTabliceMulti(sirine);
		TablicaPrinter.printajRedTabliceMulti(
		    sirine,
		    "Ime",
		    "Prezime",
		    "Datum i vrijeme",
		    "Oznaka",
		    "Status"
		);
		TablicaPrinter.printajSeperatorTabliceMulti(sirine);

		for (Rezervacija r : pogodene)
		{
		    TablicaPrinter.printajRedTabliceMulti(
		        sirine,
		        r.getIme(),
		        r.getPrezime(),
		        r.getDatumVrijemeRaw(),
		        String.valueOf(r.getOznakaAranzmana()),
		        r.getStatus() != null ? r.getStatus().getNaziv() : "-"
		    );
		}

		TablicaPrinter.printajSeperatorTabliceMulti(sirine);
	}

	public void ispisAranzmanaPretrazenog(String rijec, List<Aranzmani> pogodeni, int[] sirine) 
	{
		System.out.println();
		System.out.println("Aranžmani koji sadrže \"" + rijec + "\":");

		TablicaPrinter.printajSeperatorTabliceMulti(sirine);
		TablicaPrinter.printajRedTabliceMulti(sirine, "Oznaka", "Naziv");
		TablicaPrinter.printajSeperatorTabliceMulti(sirine);

		for (Aranzmani a : pogodeni)
		{
		    TablicaPrinter.printajRedTabliceMulti(
		        sirine,
		        String.valueOf(a.getOznaka()),
		        a.getNaziv()
		    );
		}

		TablicaPrinter.printajSeperatorTabliceMulti(sirine);
	}
}
