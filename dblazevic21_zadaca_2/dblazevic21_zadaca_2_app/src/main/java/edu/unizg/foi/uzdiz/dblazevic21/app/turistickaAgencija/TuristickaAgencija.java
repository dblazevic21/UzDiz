package edu.unizg.foi.uzdiz.dblazevic21.app.turistickaAgencija;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.AranzmaniBuilder;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.AranzmaniBuilderConcrete;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.AranzmaniDirector;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OtkazanaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.DatumParser;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.GramatikaIJezikApp;

public class TuristickaAgencija
{
    private static volatile TuristickaAgencija INSTANCE;

    private final Map<Integer, Aranzmani> aranzmani = new HashMap<>();
    private final AranzmaniDirector director;

    private TuristickaAgencija()
    {
        AranzmaniBuilder builder = new AranzmaniBuilderConcrete();
        this.director = new AranzmaniDirector(builder);
    }

    public static TuristickaAgencija getInstance()
    {
        if (INSTANCE == null)
        {
            synchronized (TuristickaAgencija.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new TuristickaAgencija();
                }
            }
        }
        return INSTANCE;
    }

    public void ucitajAranzmane(List<List<String>> podaci)
    {
        for (List<String> stupci : podaci)
        {
            try
            {
                int oznaka = uInt(stupci.get(0));
                String naziv = GramatikaIJezikApp.makniNavodnike(stupci.get(1));
                String program = GramatikaIJezikApp.makniNavodnike(stupci.get(2));
                LocalDate pocetniDatum = DatumParser.parseCsvDatum(stupci.get(3));
                LocalDate zavrsniDatum = DatumParser.parseCsvDatum(stupci.get(4));
                LocalTime vrijemeKretanja = DatumParser.parseCsvVrijeme(stupci.get(5));
                LocalTime vrijemePovratka = DatumParser.parseCsvVrijeme(stupci.get(6));
                float cijena = uFloat(stupci.get(7));
                int minBrojPutnika = uInt(stupci.get(8));
                int maksBrojPutnika = uInt(stupci.get(9));
                int brojNocenja = uInt(stupci.get(10));
                float doplataSobe = uFloat(stupci.get(11));
                String prijevoz = GramatikaIJezikApp.makniNavodnike(stupci.get(12));
                int brojDorucka = uInt(stupci.get(13));
                int brojRucka = uInt(stupci.get(14));
                int brojVecera = uInt(stupci.get(15));

                Aranzmani a = director.kreirajOsnovniAranzman(
                        oznaka, naziv, program, pocetniDatum, zavrsniDatum,
                        vrijemeKretanja, vrijemePovratka, cijena,
                        minBrojPutnika, maksBrojPutnika, brojNocenja,
                        doplataSobe, prijevoz, brojDorucka, brojRucka, brojVecera
                );

                if (a != null)
                {
                    aranzmani.put(oznaka, a);
                }
            }
            catch (Exception e)
            {
                System.out.println("Greška pri parsiranju aranžmana: " + e.getMessage());
            }
        }
    }

    public void ucitajRezervacije(List<List<String>> podaci)
    {
        for (List<String> stupci : podaci)
        {
            try
            {
                String ime = GramatikaIJezikApp.makniNavodnike(stupci.get(0));
                String prezime = GramatikaIJezikApp.makniNavodnike(stupci.get(1));
                int oznakaAranzmana = uInt(stupci.get(2));
                String dtRaw = GramatikaIJezikApp.makniNavodnike(stupci.get(3));

                Aranzmani aranzman = aranzmani.get(oznakaAranzmana);
                if (aranzman == null)
                {
                    continue;
                }

                if (aranzman.imaRezervaciju(ime, prezime))
                {
                    continue;
                }

                LocalDateTime datumVrijeme = DatumParser.normalizirajDatumIVrijeme(
                        dtRaw.split(" ")[0],
                        dtRaw.contains(" ") ? dtRaw.split(" ", 2)[1] : ""
                );

                if (datumVrijeme != null)
                {
                    aranzman.dodajRezervaciju(ime, prezime, datumVrijeme);
                }
                else
                {
                    aranzman.dodajRezervaciju(ime, prezime, dtRaw);
                }
            }
            catch (Exception e)
            {
                System.out.println("Greška pri parsiranju rezervacije: " + e.getMessage());
            }
        }

        azurirajOdgodeneRezervacije();
    }

    private void azurirajOdgodeneRezervacije()
    {
        Map<String, List<Rezervacija>> rezervacijePoOsobi = new HashMap<>();

        for (Aranzmani a : aranzmani.values())
        {
            for (Rezervacija r : a.getRezervacije())
            {
                if (r.getStatus() instanceof OtkazanaConcreteState)
                {
                    continue;
                }

                String kljuc = (r.getIme() + "_" + r.getPrezime()).toLowerCase();
                rezervacijePoOsobi
                        .computeIfAbsent(kljuc, k -> new ArrayList<>())
                        .add(r);
            }
        }

        for (List<Rezervacija> rezervacijeOsobe : rezervacijePoOsobi.values())
        {
            if (rezervacijeOsobe.size() < 2)
            {
                continue;
            }

            for (int i = 0; i < rezervacijeOsobe.size(); i++)
            {
                Rezervacija trenutna = rezervacijeOsobe.get(i);
                Aranzmani aranzmanTrenutni = aranzmani.get(trenutna.getOznakaAranzmana());
                
                if (aranzmanTrenutni == null) continue;

                for (int j = 0; j < i; j++)
                {
                    Rezervacija ranija = rezervacijeOsobe.get(j);
                    Aranzmani aranzmanRaniji = aranzmani.get(ranija.getOznakaAranzmana());
                    
                    if (aranzmanRaniji == null) continue;

                    if (aranzmaniSePreklapaju(aranzmanRaniji, aranzmanTrenutni))
                    {
                        trenutna.odgodi();
                        break;
                    }
                }
            }
        }
    }

    private boolean aranzmaniSePreklapaju(Aranzmani a1, Aranzmani a2)
    {
        if (a1.getPocetniDatum() == null || a1.getZavrsniDatum() == null ||
            a2.getPocetniDatum() == null || a2.getZavrsniDatum() == null)
        {
            return false;
        }

        return !a1.getPocetniDatum().isAfter(a2.getZavrsniDatum()) &&
               !a2.getPocetniDatum().isAfter(a1.getZavrsniDatum());
    }

    public List<Rezervacija> getSveRezervacije()
    {
        List<Rezervacija> sve = new ArrayList<>();
        for (Aranzmani a : aranzmani.values())
        {
            sve.addAll(a.getRezervacije());
        }
        return sve;
    }

    public List<Rezervacija> getRezervacijeZaOsobu(String ime, String prezime)
    {
        List<Rezervacija> rezultat = new ArrayList<>();
        for (Aranzmani a : aranzmani.values())
        {
            for (Rezervacija r : a.getRezervacije())
            {
                if (equalsIgnorirajCase(r.getIme(), ime) && 
                    equalsIgnorirajCase(r.getPrezime(), prezime))
                {
                    rezultat.add(r);
                }
            }
        }
        return rezultat;
    }

    private boolean equalsIgnorirajCase(String rijec1, String rijec2)
    {
        String prvaRijec = (rijec1 == null) ? "" : rijec1.trim();
        String drugaRijec = (rijec2 == null) ? "" : rijec2.trim();
        return prvaRijec.equalsIgnoreCase(drugaRijec);
    }

    private int uInt(String s)
    {
        try
        {
            String t = (s == null) ? "" : s.trim();
            if (t.isEmpty()) return 0;
            return Integer.parseInt(t);
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    private float uFloat(String s)
    {
        try
        {
            String t = (s == null) ? "" : s.trim().replace(',', '.');
            if (t.isEmpty()) return 0f;
            return Float.parseFloat(t);
        }
        catch (Exception e)
        {
            return 0f;
        }
    }

    public Map<Integer, Aranzmani> getAranzmani()
    {
        return aranzmani;
    }
}
