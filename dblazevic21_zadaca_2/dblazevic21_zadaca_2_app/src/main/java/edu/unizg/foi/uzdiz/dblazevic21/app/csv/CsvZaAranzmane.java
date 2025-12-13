package edu.unizg.foi.uzdiz.dblazevic21.app.csv;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.IspisiGreskuApp;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.CsvParserApp;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.GramatikaIJezikApp;
import edu.unizg.foi.uzdiz.dblazevic21.lib.facade.Facade;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class CsvZaAranzmane
{
	private static final int OCEKIVANI_STUPCI = 16;

	public void ucitaj(String putanja, Map<Integer, Aranzmani> aranzmani) 
	{
	    Facade facade = Facade.getInstance();

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
	                    continue;
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


    private Aranzmani parseAranzman(List<String> s)
    {
        Aranzmani a = new Aranzmani();

        int oznaka = CsvParserApp.uInt(s.get(0));
        a.setOznaka(oznaka);

        a.setNaziv(GramatikaIJezikApp.makniNavodnike(s.get(1)));
        a.setProgram(GramatikaIJezikApp.makniNavodnike(s.get(2)));

        LocalDate poc = CsvParserApp.uDatum(s.get(3));
        LocalDate zav = CsvParserApp.uDatum(s.get(4));
        a.setPocetniDatum(poc);
        a.setZavrsniDatum(zav);

        LocalTime vk = CsvParserApp.uVrijeme(s.get(5));
        LocalTime vp = CsvParserApp.uVrijeme(s.get(6));
        a.setVrijemeKretanja(vk);
        a.setVrijemePovratka(vp);

        a.setCijena(CsvParserApp.uFloat(s.get(7)));
        a.setMinBrojPutnika(CsvParserApp.uInt(s.get(8)));
        a.setMaksBrojPutnika(CsvParserApp.uInt(s.get(9)));
        a.setBrojNocenja(CsvParserApp.uInt(s.get(10)));
        a.setDoplataSobe(CsvParserApp.uFloat(s.get(11)));

        a.setPrijevoz(GramatikaIJezikApp.makniNavodnike(s.get(12)));
        a.setBrojDorucka(CsvParserApp.uInt(s.get(13)));
        a.setBrojRucka(CsvParserApp.uInt(s.get(14)));
        a.setBrojVecera(CsvParserApp.uInt(s.get(15)));

        return a;
    }
}
