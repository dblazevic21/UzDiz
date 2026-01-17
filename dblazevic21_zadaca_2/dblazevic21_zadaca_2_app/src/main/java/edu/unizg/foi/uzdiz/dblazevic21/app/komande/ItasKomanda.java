package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.FormaterZaIspise;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.IspisKonfiguracija;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.IspisiGreskuApp;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.DatumParserApp;
import edu.unizg.foi.uzdiz.dblazevic21.lib.facade.TuristickaFacade;

public class ItasKomanda implements Komanda
{
	
	 private final Map<Integer, Aranzmani> aranzmani;

    public ItasKomanda(Map<Integer, Aranzmani> aranzmani) 
    {
        this.aranzmani = aranzmani;
    }
	
	@Override
	public String getNaziv()
	{
		return "ITAS";
	}

	@Override
	public void izvrsi(String unos)
	{
	    TuristickaFacade facade = TuristickaFacade.getInstance();
	    String odrezan = (unos == null) ? "" : unos.trim();

	    LocalDate odDatum = null;
	    LocalDate doDatum = null;

	    String[] dijelovi = odrezan.split("\\s+");
	    if (dijelovi.length == 1 && dijelovi[0].equalsIgnoreCase("ITAS")) 
	    {
	        
	    } 
	    else if (dijelovi.length == 3 && dijelovi[0].equalsIgnoreCase("ITAS")) 
	    {
	        try 
	        {
	            odDatum = DatumParserApp.parseDatumZaKomandu(dijelovi[1]);
	            doDatum = DatumParserApp.parseDatumZaKomandu(dijelovi[2]);
	        }
	        catch (Exception e) 
	        {
	            facade.getBrojGresaka();
	            System.out.println("Greška pri parsiranju datuma. Provjerite format datuma.");
	        }
	        IspisiGreskuApp.provjeraDatuma(unos, odDatum, doDatum);
	    }
	    else 
	    {
	        facade.getBrojGresaka();
	        System.out.println("Neispravan format. Upotrijebite: ITAS ili ITAS [odDatum doDatum]");
	        return;
	    }
	    ispisiStatistiku(odDatum, doDatum, unos);
	}

	
	private void ispisiStatistiku(LocalDate odDatum, LocalDate doDatum, String unos) 
	{
	    int[] sirine = {8, 35, 10, 10, 10, 10, 10, 15};
	    boolean[] desno = {
	    		true, 
	    		false, 
	    		true, 
	    		true, 
	    		true, 
	    		true, 
	    		true, 
	    		true
    		};

	    TablicaPrinter.ispisUnosa(unos);
	    System.out.println("Statistika rezervacija po aranžmanima:");
	    System.out.println();
	    
	    TablicaPrinter.printajSeperatorTabliceMulti(sirine);
	    TablicaPrinter.printajRedTabliceMultiAlign(sirine, desno, 
	            "Oznaka", "Naziv", "Ukupno", "Aktivni", "Na čekanju", "Odgođeni", "Otkazani", "Prihod");
	    TablicaPrinter.printajSeperatorTabliceMulti(sirine);

	    List<Aranzmani> lista = new ArrayList<>(aranzmani.values());
	    lista.sort(Comparator.comparing(Aranzmani::getPocetniDatum, Comparator.nullsLast(Comparator.naturalOrder()))
	            .thenComparing(Aranzmani::getOznaka));
	    if (IspisKonfiguracija.jeObrnutoKronoloski()) 
	    {
	        Collections.reverse(lista);
	    }

	    for (Aranzmani a : lista) 
	    {
	        List<Rezervacija> rezervacije = a.getRezervacije();

	        if (odDatum != null && doDatum != null) 
	        {
	            rezervacije = rezervacije.stream()
	                .filter(r -> !r.getDatumVrijeme().toLocalDate().isBefore(odDatum) &&
	                             !r.getDatumVrijeme().toLocalDate().isAfter(doDatum))
	                .toList();
	        }

	        long ukupno = rezervacije.size();
	        long aktivne = rezervacije.stream().filter(r -> r.getStatus().getNaziv().equals("AKTIVNA")).count();
	        long naCekanju = rezervacije.stream().filter(r -> r.getStatus().getNaziv().equals("NA ČEKANJU")).count();
	        long odgodene = rezervacije.stream().filter(r -> r.getStatus().getNaziv().equals("ODGOĐENA")).count();
	        long otkazane = rezervacije.stream().filter(r -> r.getStatus().getNaziv().equals("OTKAZANA")).count();
	        float prihod = aktivne * a.getCijena();

	        TablicaPrinter.printajRedTabliceMultiAlign(sirine, desno,
	            FormaterZaIspise.fmtBroj(a.getOznaka()),
	            FormaterZaIspise.izrezi(a.getNaziv(), 35),
	            FormaterZaIspise.fmtBroj(ukupno),
	            FormaterZaIspise.fmtBroj(aktivne),
	            FormaterZaIspise.fmtBroj(naCekanju),
	            FormaterZaIspise.fmtBroj(odgodene),
	            FormaterZaIspise.fmtBroj(otkazane),
	            FormaterZaIspise.fmtCijena(prihod));
	    }

	    TablicaPrinter.printajSeperatorTabliceMulti(sirine);
	}

}
