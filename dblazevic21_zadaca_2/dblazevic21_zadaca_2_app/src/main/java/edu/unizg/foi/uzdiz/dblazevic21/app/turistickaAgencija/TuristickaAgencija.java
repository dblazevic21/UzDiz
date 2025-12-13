package edu.unizg.foi.uzdiz.dblazevic21.app.turistickaAgencija;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.AranzmaniBuilder;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.AranzmaniBuilderConcrete;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.AranzmaniDirector;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OtkazanaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.CsvParserApp;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.DatumParserApp;
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
                int oznaka = CsvParserApp.uInt(stupci.get(0));
                String naziv = GramatikaIJezikApp.makniNavodnike(stupci.get(1));
                String program = GramatikaIJezikApp.makniNavodnike(stupci.get(2));
                LocalDate pocetniDatum = DatumParserApp.parseCsvDatum(stupci.get(3));
                LocalDate zavrsniDatum = DatumParserApp.parseCsvDatum(stupci.get(4));
                LocalTime vrijemeKretanja = DatumParserApp.parseCsvVrijeme(stupci.get(5));
                LocalTime vrijemePovratka = DatumParserApp.parseCsvVrijeme(stupci.get(6));
                float cijena = CsvParserApp.uFloat(stupci.get(7));
                int minBrojPutnika = CsvParserApp.uInt(stupci.get(8));
                int maksBrojPutnika = CsvParserApp.uInt(stupci.get(9));
                int brojNocenja = CsvParserApp.uInt(stupci.get(10));
                float doplataSobe = CsvParserApp.uFloat(stupci.get(11));
                String prijevoz = GramatikaIJezikApp.makniNavodnike(stupci.get(12));
                int brojDorucka = CsvParserApp.uInt(stupci.get(13));
                int brojRucka = CsvParserApp.uInt(stupci.get(14));
                int brojVecera = CsvParserApp.uInt(stupci.get(15));

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
        Rezervacije rez = Rezervacije.getInstance();

        for (List<String> stupci : podaci)
        {
            try
            {
                String ime = GramatikaIJezikApp.makniNavodnike(stupci.get(0));
                String prezime = GramatikaIJezikApp.makniNavodnike(stupci.get(1));
                int oznakaAranzmana = CsvParserApp.uInt(stupci.get(2));
                String dtRaw = GramatikaIJezikApp.makniNavodnike(stupci.get(3));

                Aranzmani aranzman = aranzmani.get(oznakaAranzmana);
                if (aranzman == null)
                {
                    continue;
                }

                LocalDateTime datumVrijeme = DatumParserApp.normalizirajDatumIVrijeme(
                        dtRaw.split(" ")[0],
                        dtRaw.contains(" ") ? dtRaw.split(" ", 2)[1] : ""
                );

                if (jeDuplikatRezervacije(aranzman, ime, prezime))
                {
                    continue;
                }

                Rezervacija novaRezervacija;
                if (datumVrijeme != null)
                {
                    novaRezervacija = rez.kreirajRezervaciju(ime, prezime, oznakaAranzmana, datumVrijeme);
                }
                else
                {
                    novaRezervacija = rez.kreirajRezervaciju(ime, prezime, oznakaAranzmana, dtRaw);
                }
                
                aranzman.dodajRezervaciju(novaRezervacija);
            }
            catch (Exception e)
            {
                System.out.println("Greška pri parsiranju rezervacije: " + e.getMessage());
            }
        }

        rez.azurirajStatuseRezervacija(aranzmani);
    }

    private boolean jeDuplikatRezervacije(Aranzmani aranzman, String ime, String prezime)
    {
        for (Rezervacija r : aranzman.getRezervacije())
        {
            if (!GramatikaIJezikApp.equalsIgnorirajCase(r.getIme(), ime) || !GramatikaIJezikApp.equalsIgnorirajCase(r.getPrezime(), prezime))
            {
                continue;
            }
            
            if (!(r.getStatus() instanceof OtkazanaConcreteState))
            {
                return true;
            }
        }
        return false;
    }

    public Map<Integer, Aranzmani> getAranzmani()
    {
        return aranzmani;
    }
}
