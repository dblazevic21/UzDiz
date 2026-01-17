package edu.unizg.foi.uzdiz.dblazevic21.ispis;

import java.util.List;

import edu.unizg.foi.uzdiz.dblazevic21.util.CsvParser;
import edu.unizg.foi.uzdiz.dblazevic21.util.GramatikaIJezik;

public class IspisiGresku 
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
	        if (CsvParser.uInt(stupci.get(0)) == 0) 
	        {
	            razloziGreske.add("Oznaka mora biti cijeli broj veći od 0.");
	        }
	        if (GramatikaIJezik.makniNavodnike(stupci.get(1)).isEmpty()) 
	        {
	            razloziGreske.add("Naziv ne smije biti prazan.");
	        }
	        if (GramatikaIJezik.makniNavodnike(stupci.get(2)).isEmpty()) 
	        {
	            razloziGreske.add("Program ne smije biti prazan.");
	        }
	        if (CsvParser.uDatum(stupci.get(3)) == null) 
	        {
	            razloziGreske.add("Pogrešan format početnog datuma.");
	        }
	        if (CsvParser.uDatum(stupci.get(4)) == null) 
	        {
	            razloziGreske.add("Pogrešan format završnog datuma.");
	        }
	        if (stupci.get(12).isEmpty()) 
	        {
	            if (CsvParser.uVrijeme(stupci.get(5)) == null) 
	            {
	                razloziGreske.add("Trebate dodati prijevoz ili dodati vrijeme kretanja");
	            }
	            if (CsvParser.uVrijeme(stupci.get(6)) == null) 
	            {
	                razloziGreske.add("Trebate dodati prijevoz ili dodati vrijeme povratka.");
	            }
	        }
	        if (CsvParser.uFloat(stupci.get(7)) <= 0) 
	        {
	            razloziGreske.add("Cijena mora biti veća od 0.");
	        }
	        if (CsvParser.uInt(stupci.get(8)) <= 0) 
	        {
	            razloziGreske.add("Minimalan broj putnika mora biti veći od 0.");
	        }
	        if (CsvParser.uInt(stupci.get(9)) <= 0) 
	        {
	            razloziGreske.add("Maksimalan broj putnika mora biti veći od 0.");
	        }
	        if (CsvParser.uInt(stupci.get(8)) > CsvParser.uInt(stupci.get(9)))
	        {
	        	razloziGreske.add("Minimalan broj putnika ne može biti veći od maksimalnog broja putnika.");
	        }
	        if (CsvParser.uInt(stupci.get(10)) < 0) 
	        {
	            razloziGreske.add("Broj noćenja ne može biti negativan.");
	        }
	        if (CsvParser.uFloat(stupci.get(11)) < 0) 
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
			if (GramatikaIJezik.makniNavodnike(stupci.get(0)).isEmpty()) 
			{
				razloziGreske.add("Ime ne smije biti prazno.");
			}
			if (GramatikaIJezik.makniNavodnike(stupci.get(1)).isEmpty()) 
			{
				razloziGreske.add("Prezime ne smije biti prazno.");
			}
			if (CsvParser.uInt(stupci.get(2)) == 0) 
			{
				razloziGreske.add("Oznaka aranžmana mora biti cijeli broj veći od 0.");
			}
			if (CsvParser.uDatumVrijeme(GramatikaIJezik.makniNavodnike(stupci.get(3))) == null)
			{
				razloziGreske.add("Pogrešan format datuma i vremena rezervacije.");
			}
		}
	}
}
