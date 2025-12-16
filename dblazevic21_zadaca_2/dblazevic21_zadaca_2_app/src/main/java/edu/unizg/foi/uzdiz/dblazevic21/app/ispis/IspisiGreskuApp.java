package edu.unizg.foi.uzdiz.dblazevic21.app.ispis;

import java.time.LocalDate;
import java.util.List;

import edu.unizg.foi.uzdiz.dblazevic21.app.utils.CsvParserApp;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.GramatikaIJezikApp;

public class IspisiGreskuApp 
{
	public static void ispisiGresku(int rbGreske, int brojLinije, String sadrzaj, List<String> razlozi, String putanja)
    {
        System.out.println("Greška #" + rbGreske + " u datoteci " + putanja + " na liniji " + brojLinije + ":");
        System.out.println("  Redak: " + sadrzaj);
        for (String r : razlozi) 
        {
            System.out.println("  Razlog: " + r);
        }
    }
	
	public static void provjeriStupceAranzmana(List<String> stupci, List<String> razloziGreske) 
	{
	    if (stupci.size() < 16) 
	    {
	        razloziGreske.add("nedovoljan broj stupaca (" + stupci.size() + "/16)");
	    } 
	    else 
	    {
	        if (CsvParserApp.uInt(stupci.get(0)) == 0) 
	        {
	            razloziGreske.add("Oznaka mora biti cijeli broj veći od 0.");
	        }
	        if (GramatikaIJezikApp.makniNavodnike(stupci.get(1)).isEmpty()) 
	        {
	            razloziGreske.add("Naziv ne smije biti prazan.");
	        }
	        if (GramatikaIJezikApp.makniNavodnike(stupci.get(2)).isEmpty()) 
	        {
	            razloziGreske.add("Program ne smije biti prazan.");
	        }
	        if (CsvParserApp.uDatum(stupci.get(3)) == null) 
	        {
	            razloziGreske.add("Pogrešan format početnog datuma.");
	        }
	        if (CsvParserApp.uDatum(stupci.get(4)) == null) 
	        {
	            razloziGreske.add("Pogrešan format završnog datuma.");
	        }
	        if (CsvParserApp.uFloat(stupci.get(7)) <= 0) 
	        {
	            razloziGreske.add("Cijena mora biti veća od 0.");
	        }
	        if (CsvParserApp.uInt(stupci.get(8)) <= 0) 
	        {
	            razloziGreske.add("Minimalan broj putnika mora biti veći od 0.");
	        }
	        if (CsvParserApp.uInt(stupci.get(9)) <= 0) 
	        {
	            razloziGreske.add("Maksimalan broj putnika mora biti veći od 0.");
	        }
	        if (CsvParserApp.uInt(stupci.get(8)) > CsvParserApp.uInt(stupci.get(9)))
	        {
	        	razloziGreske.add("Minimalan broj putnika ne može biti veći od maksimalnog broja putnika.");
	        }
	        if (CsvParserApp.uInt(stupci.get(10)) < 0) 
	        {
	            razloziGreske.add("Broj noćenja ne može biti negativan.");
	        }
	        if (CsvParserApp.uFloat(stupci.get(11)) < 0) 
	        {
	            razloziGreske.add("Doplata za sobu ne može biti negativna.");
	        }
	    }
	}

	
	public static void provjeriStupceRezervacije(List<String> stupci, List<String> razloziGreske) 
	{
		if (stupci.size() < 4) 
		{
			razloziGreske.add("nedovoljan broj stupaca (" + stupci.size() + "/4)");
		}
		else
		{
			if (GramatikaIJezikApp.makniNavodnike(stupci.get(0)).isEmpty()) 
			{
				razloziGreske.add("Ime ne smije biti prazno.");
			}
			if (GramatikaIJezikApp.makniNavodnike(stupci.get(1)).isEmpty()) 
			{
				razloziGreske.add("Prezime ne smije biti prazno.");
			}
			if (CsvParserApp.uInt(stupci.get(2)) == 0) 
			{
				razloziGreske.add("Oznaka aranžmana mora biti cijeli broj veći od 0.");
			}
			if (CsvParserApp.uDatumVrijeme(GramatikaIJezikApp.makniNavodnike(stupci.get(3))) == null)
			{
				razloziGreske.add("Pogrešan format datuma i vremena rezervacije.");
			}
		}
	}
	
	public static void ispisiDuplikatAranzmana(int rbGreske, int brojLinije, String sadrzaj, int oznaka, String putanja) 
	{
	    ispisiGresku(
	        rbGreske,
	        brojLinije,
	        sadrzaj,
	        List.of("Aranžman s oznakom " + oznaka + " već postoji i bit će ignoriran."),
	        putanja
	    );
	}
	
	public static void provjeraDatuma(String unos, LocalDate odDatum, LocalDate doDatum) 
	{
	    if (odDatum != null && doDatum == null)
	    {
	    	TablicaPrinter.ispisUnosa(unos);
	        System.out.println("Neispravan format datuma. Upotrijebite: ITAS dd.MM.yyyy dd.MM.yyyy ili samo ITAS");
	        return;
	    }
	    
	    if (odDatum == null && doDatum != null)
	    {
	    	TablicaPrinter.ispisUnosa(unos);
	        System.out.println("Neispravan format datuma. Upotrijebite: ITAS dd.MM.yyyy dd.MM.yyyy ili samo ITAS");
	        return;
	    }
	    
	    if (odDatum != null && doDatum != null && odDatum.isAfter(doDatum))
	    {
	    	TablicaPrinter.ispisUnosa(unos);
	        System.out.println("Neispravan raspon datuma. Početni datum mora biti prije završnog.");
	        return;
	    }
	    
	    if (odDatum != null && doDatum != null && doDatum.isBefore(odDatum))
	    {
	    	TablicaPrinter.ispisUnosa(unos);
	        System.out.println("Neispravan raspon datuma. Završni datum mora biti poslije početnog.");
	        return;
	    }
	}
}
