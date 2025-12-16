package edu.unizg.foi.uzdiz.dblazevic21.app.csv;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.IspisiGreskuApp;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.CsvParserApp;
import edu.unizg.foi.uzdiz.dblazevic21.lib.facade.TuristickaFacade;

public class CsvZaRezervacije 
{

    private static final int OCEKIVANI_STUPCI = 4;

    public List<List<String>> provjeri(List<List<String>> podaci) 
    {
        List<List<String>> validni = new ArrayList<>();
        int brojLinije = 1;
        for (List<String> stupci : podaci) 
        {
            brojLinije++;
            List<String> razlozi = new ArrayList<>();
            IspisiGreskuApp.provjeriStupceRezervacije(stupci, razlozi);
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
        Rezervacije rezervacije = Rezervacije.getInstance();
        for (List<String> s : validni)
        {
            try
            {
                String ime = s.get(0).trim();
                String prezime = s.get(1).trim();
                int oznaka = CsvParserApp.uInt(s.get(2));
                String datumVrijemeRaw = s.get(3);
                LocalDateTime dt = CsvParserApp.uDatumVrijeme(datumVrijemeRaw);

                rezervacije.dodajRezervacijuBezStatusa(ime, prezime, oznaka, dt, aranzmani);
            }
            catch (Exception ignored) {}
        }
    }
    
    public void ucitaj(List<String> putanje, Map<Integer, Aranzmani> aranzmani) 
    {
        TuristickaFacade facade = TuristickaFacade.getInstance();
        Rezervacije rezervacije = Rezervacije.getInstance();

        for (String putanja : putanje) 
        {
            List<List<String>> rezervacijePodaci = facade.ucitajRezervacije(putanja);
            if (rezervacijePodaci == null || rezervacijePodaci.isEmpty()) 
            {
                System.out.println("Datoteka je prazna ili nije učitana: " + putanja);
                continue;
            }

            int brojLinije = 1;
            for (List<String> stupci : rezervacijePodaci) 
            {
                brojLinije++;
                if (stupci.size() != OCEKIVANI_STUPCI) 
                {
                    facade.getBrojGresaka();
                    IspisiGreskuApp.ispisiGresku(
                        facade.getBrojGresaka(),
                        brojLinije,
                        stupci.toString(),
                        List.of("Očekivano " + OCEKIVANI_STUPCI + " stupaca, dobiveno " + stupci.size()),
                        putanja
                    );
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
                        IspisiGreskuApp.ispisiGresku(
                            facade.getBrojGresaka(),
                            brojLinije,
                            stupci.toString(),
                            List.of("Neispravan datum i vrijeme."),
                            putanja
                        );
                        continue;
                    }

                    rezervacije.dodajRezervacijuBezStatusa(ime, prezime, oznaka, dt, aranzmani);
                } 
                catch (Exception e) 
                {
                    facade.getBrojGresaka();
                    IspisiGreskuApp.ispisiGresku(
                        facade.getBrojGresaka(),
                        brojLinije,
                        stupci.toString(),
                        List.of("Greška pri obradi rezervacije: " + e.getMessage()),
                        putanja
                    );
                }
            }
        }
    }
}
