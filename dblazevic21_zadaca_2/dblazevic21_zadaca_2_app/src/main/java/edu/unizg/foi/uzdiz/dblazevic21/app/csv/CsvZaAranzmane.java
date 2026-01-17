package edu.unizg.foi.uzdiz.dblazevic21.app.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.IspisiGreskuApp;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.AranzmaniDirector;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.CsvParserApp;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.GramatikaIJezikApp;
import edu.unizg.foi.uzdiz.dblazevic21.lib.facade.TuristickaFacade;

public class CsvZaAranzmane
{
	private static final int OCEKIVANI_STUPCI = 16;

	public List<List<String>> provjeri(List<List<String>> podaci) 
	{
	    List<List<String>> validni = new ArrayList<>();
	    int brojLinije = 1;
	    for (List<String> stupci : podaci) 
	    {
	        brojLinije++;
	        List<String> razlozi = new ArrayList<>();
	        IspisiGreskuApp.provjeriStupceAranzmana(stupci, razlozi);
	        if (razlozi.isEmpty()) 
	        {
	            validni.add(stupci);
	        }
	        else 
	        {
	            IspisiGreskuApp.ispisiGresku(
	                brojLinije, brojLinije, stupci.toString(), razlozi, "FACADE"
	            );
	        }
	    }
	    return validni;
	}

	public void dodaj(List<List<String>> validni, Map<Integer, Aranzmani> aranzmani) 
	{
	    for (List<String> s : validni)
	    {
	        try 
	        {
	            Aranzmani a = parseAranzman(s);
	            int oznaka = a.getOznaka();
	            if (!aranzmani.containsKey(oznaka)) 
	            {
	                aranzmani.put(oznaka, a);
	            }
	        } 
	        catch (Exception ignored) {}
	    }
	}
	
	public void ucitaj(String putanja, Map<Integer, Aranzmani> aranzmani) 
	{
	    TuristickaFacade facade = TuristickaFacade.getInstance();

	    try (BufferedReader br = Files.newBufferedReader(Paths.get(putanja), StandardCharsets.UTF_8)) 
	    {
	        String linija = br.readLine();
	        if (linija == null) 
	        {
	            return;
	        }

	        int brojLinije = 1;
	        while ((linija = br.readLine()) != null) 
	        {
	            brojLinije++;

	            String raw = linija;
	            linija = linija.trim();
	            if (linija.isEmpty()) 
	            {
	                continue;
	            }

	            List<String> stupci = CsvParserApp.parseCsvLiniju(linija);
	            if (stupci.size() != OCEKIVANI_STUPCI)
	            {
	                facade.getBrojGresaka();
	                IspisiGreskuApp.ispisiGresku(
	                    facade.getBrojGresaka(),
	                    brojLinije,
	                    raw,
	                    List.of("Očekivano " + OCEKIVANI_STUPCI + " stupaca, dobiveno " + stupci.size()),
	                    putanja
	                );
	                continue;
	            }

	            upravljanjeAranzmanima(putanja, aranzmani, facade, brojLinije, raw, stupci);
	        }
	    } 
	    catch (IOException e)
	    {
	        facade.getBrojGresaka();
	        IspisiGreskuApp.ispisiGresku(
	            facade.getBrojGresaka(),
	            0,
	            "",
	            List.of("Greška pri čitanju datoteke: " + e.getMessage()),
	            putanja
	        );
	    }
	}


	public void upravljanjeAranzmanima(String putanja, Map<Integer, Aranzmani> aranzmani, TuristickaFacade facade, int brojLinije,
			String raw, List<String> stupci) 
	{
		try
		{
		    Aranzmani a = parseAranzman(stupci);
		    int oznaka = a.getOznaka();

		    if (aranzmani.containsKey(oznaka))
		    {
		        facade.getBrojGresaka();
		        IspisiGreskuApp.ispisiDuplikatAranzmana(
		            facade.getBrojGresaka(),
		            brojLinije,
		            raw,
		            oznaka,
		            putanja
		        );
		        return;
		    }

		    aranzmani.put(oznaka, a);
		}
		catch (Exception e)
		{
		    facade.getBrojGresaka();
		    IspisiGreskuApp.ispisiGresku(
		        facade.getBrojGresaka(),
		        brojLinije,
		        raw,
		        List.of("Greška pri parsiranju aranžmana: " + e.getMessage()),
		        putanja
		    );
		}
	}

	private Aranzmani parseAranzman(List<String> s)
	{
	    int oznaka = CsvParserApp.uInt(s.get(0));
	    String naziv = GramatikaIJezikApp.makniNavodnike(s.get(1));
	    String program = GramatikaIJezikApp.makniNavodnike(s.get(2));
	    LocalDate poc = CsvParserApp.uDatum(s.get(3));
	    LocalDate zav = CsvParserApp.uDatum(s.get(4));
	    LocalTime vk = CsvParserApp.uVrijeme(s.get(5));
	    LocalTime vp = CsvParserApp.uVrijeme(s.get(6));
	    float cijena = CsvParserApp.uFloat(s.get(7));
	    int min = CsvParserApp.uInt(s.get(8));
	    int max = CsvParserApp.uInt(s.get(9));
	    int nocenja = CsvParserApp.uInt(s.get(10));
	    float doplata = CsvParserApp.uFloat(s.get(11));
	    String prijevoz = GramatikaIJezikApp.makniNavodnike(s.get(12));
	    int dor = CsvParserApp.uInt(s.get(13));
	    int ruc = CsvParserApp.uInt(s.get(14));
	    int vec = CsvParserApp.uInt(s.get(15));

	    AranzmaniDirector director = new AranzmaniDirector();
	    return director.kreirajOsnovniAranzman(
	        oznaka,
	        naziv,
	        program,
	        poc,
	        zav,
	        vk,
	        vp,
	        cijena,
	        min,
	        max,
	        nocenja,
	        doplata,
	        prijevoz,
	        dor,
	        ruc,
	        vec
	    );
	}
}
