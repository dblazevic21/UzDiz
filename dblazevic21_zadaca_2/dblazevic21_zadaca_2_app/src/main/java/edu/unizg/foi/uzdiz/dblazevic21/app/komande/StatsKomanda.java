package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.unizg.foi.uzdiz.dblazevic21.app.component.StatsKomandaConcreteDecorator;
import edu.unizg.foi.uzdiz.dblazevic21.app.component.TuristickiElementComponent;
import edu.unizg.foi.uzdiz.dblazevic21.app.component.TuristickiElementConcreteComponent;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.IspisiGreskuApp;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.DatumParserApp;

public class StatsKomanda implements Komanda 
{
	private final Map<Integer, Aranzmani> aranzmani;
	
	@Override
	public String getNaziv() 
	{
		return "STATS";
	}
	
	public StatsKomanda(Map<Integer, Aranzmani> aranzmani)
	{
	    this.aranzmani = aranzmani;
	}


	@Override
	public void izvrsi(String unos)
	{
	    String s = (unos == null) ? "" : unos.trim();

	    Pattern p0 = Pattern.compile("^STATS$", Pattern.CASE_INSENSITIVE);
	    Pattern p1 = Pattern.compile("^STATS\\s+(\\d+)$", Pattern.CASE_INSENSITIVE);
	    Pattern p2 = Pattern.compile("^STATS\\s+(\\d+)\\s+(.+)\\s+(.+)$", Pattern.CASE_INSENSITIVE);

	    Matcher m1 = p1.matcher(s);
	    Matcher m2 = p2.matcher(s);

	    LocalDate odDatum = null;
	    LocalDate doDatum = null;
	    List<Rezervacija> ciljaneRezervacije;
	    String opis;

	    if (p0.matcher(s).matches())
	    {
	        ciljaneRezervacije = aranzmani.values().stream()
	            .flatMap(a -> a.getRezervacije().stream())
	            .toList();

	        opis = "Svi aranžmani";
	    }
	    else if (m1.matches())
	    {
	        int oznaka = Integer.parseInt(m1.group(1));
	        Aranzmani a = aranzmani.get(oznaka);

	        if (a == null)
	        {
	        	TablicaPrinter.ispisUnosa(unos);
	            System.out.println("Ne postoji aranžman s oznakom: " + oznaka);
	            return;
	        }

	        ciljaneRezervacije = a.getRezervacije();
	        opis = "Aranžman " + oznaka;
	    }
	    else if (m2.matches())
	    {
	        int oznaka = Integer.parseInt(m2.group(1));
	        Aranzmani a = aranzmani.get(oznaka);

	        if (a == null)
	        {
	        	TablicaPrinter.ispisUnosa(unos);
	            System.out.println("Ne postoji aranžman s oznakom: " + oznaka);
	            return;
	        }

	        odDatum = DatumParserApp.parseDatumZaKomandu(
	                DatumParserApp.normalizirajDatum(m2.group(2))
	        );
	        doDatum = DatumParserApp.parseDatumZaKomandu(
	                DatumParserApp.normalizirajDatum(m2.group(3))
	        );

	        IspisiGreskuApp.provjeraDatuma(unos, odDatum, doDatum);

	        ciljaneRezervacije = a.getRezervacije();
	        opis = "Aranžman " + oznaka;
	    }
	    else
	    {
	        System.out.println("Upotreba: STATS [oznaka] [od do]");
	        return;
	    }

	    kreirajStatistiku(unos, odDatum, doDatum, ciljaneRezervacije, opis);
	}

	public void kreirajStatistiku(String unos, LocalDate odDatum, LocalDate doDatum,
			List<Rezervacija> ciljaneRezervacije, String opis)
	{
		TuristickiElementComponent element =
	        new TuristickiElementConcreteComponent(opis);

	    StatsKomandaConcreteDecorator decorator =
	        new StatsKomandaConcreteDecorator(element, ciljaneRezervacije);

	    TablicaPrinter.ispisUnosa(unos);
	    System.out.println("Statistika za: " + opis);

	    if (odDatum != null && doDatum != null)
	    {
	    	decorator.prikaziStatistiku(odDatum, doDatum);
	    }
	    else
	    {
	    	decorator.prikaziStatistiku();
	    }
	}
}
