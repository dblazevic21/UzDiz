package edu.unizg.foi.uzdiz.dblazevic21.app.csv;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.CsvParserApp;
import edu.unizg.foi.uzdiz.dblazevic21.lib.facade.Facade;

public class CsvZaRezervacije 
{

    private static final int OCEKIVANI_STUPCI = 4;

    public void ucitaj(String putanja, Map<Integer, Aranzmani> aranzmani) 
    {
        Facade facade = Facade.getInstance();
        List<List<String>> rezervacijePodaci = facade.ucitajRezervacije(putanja);

        Rezervacije rezervacije = Rezervacije.getInstance();

        int brojLinije = 1;
        for (List<String> stupci : rezervacijePodaci) 
        {
            brojLinije++;

            if (stupci.size() != OCEKIVANI_STUPCI) 
            {
                facade.getBrojGresaka();
                continue;
            }

            try 
            {
                String ime = stupci.get(0).trim();
                String prezime = stupci.get(1).trim();
                int oznaka = CsvParserApp.uInt(stupci.get(2));
                LocalDateTime dt = CsvParserApp.uDatumVrijeme(stupci.get(3));

                if (dt == null) 
                {
                    facade.getBrojGresaka();
                    continue;
                }

                boolean ok = rezervacije.dodajRezervacijuBezStatusa(ime, prezime, oznaka, dt, aranzmani);
                if (!ok) 
                {
                    facade.getBrojGresaka();
                }
            }
            catch (Exception e) 
            {
                facade.getBrojGresaka();
            }
        }

        rezervacije.azurirajStatuseRezervacija(aranzmani);
    }
}
