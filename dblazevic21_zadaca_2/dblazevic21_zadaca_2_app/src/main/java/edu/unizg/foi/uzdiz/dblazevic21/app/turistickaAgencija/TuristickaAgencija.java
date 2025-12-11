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
        Rezervacije rez = Rezervacije.getInstance();

        for (List<String> stupci : podaci)
        {
            try
            {
                String ime = GramatikaIJezikApp.makniNavodnike(stupci.get(0));
                String prezime = GramatikaIJezikApp.makniNavodnike(stupci.get(1));
                int oznakaAranzmana = uInt(stupci.get(2));
                String dtRaw = GramatikaIJezikApp.makniNavodnike(stupci.get(3));

                if (!aranzmani.containsKey(oznakaAranzmana))
                {
                    continue;
                }

                LocalDateTime datumVrijeme = DatumParser.normalizirajDatumIVrijeme(
                        dtRaw.split(" ")[0],
                        dtRaw.contains(" ") ? dtRaw.split(" ", 2)[1] : ""
                );

<<<<<<< HEAD
                if (jeDuplikatRezervacije(rez, ime, prezime, oznakaAranzmana, datumVrijeme, dtRaw))
                {
                    continue;
                }

=======
>>>>>>> refs/remotes/origin/main
                if (datumVrijeme != null)
                {
                    rez.dodajRezervaciju(ime, prezime, oznakaAranzmana, datumVrijeme);
                }
                else
                {
                    rez.dodajRezervaciju(ime, prezime, oznakaAranzmana, dtRaw);
                }
            }
            catch (Exception e)
            {
                System.out.println("Greška pri parsiranju rezervacije: " + e.getMessage());
            }
        }

        rez.azurirajStatuseRezervacija(aranzmani);
    }

<<<<<<< HEAD
    private boolean jeDuplikatRezervacije(Rezervacije rez, String ime, String prezime, 
            int oznakaAranzmana, LocalDateTime datumVrijeme, String dtRaw)
    {
        for (Rezervacija r : rez.getSveRezervacije())
        {
            if (r.getOznakaAranzmana() != oznakaAranzmana)
            {
                continue;
            }
            
            if (!equalsIgnorirajCase(r.getIme(), ime) || !equalsIgnorirajCase(r.getPrezime(), prezime))
            {
                continue;
            }
            
            if (datumVrijeme != null && r.getDatumVrijeme() != null)
            {
                if (datumVrijeme.equals(r.getDatumVrijeme()))
                {
                    return true;
                }
            }
            else if (dtRaw != null && r.getDatumVrijemeRaw() != null)
            {
                if (dtRaw.equals(r.getDatumVrijemeRaw()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean equalsIgnorirajCase(String rijec1, String rijec2)
    {
        String prvaRijec = (rijec1 == null) ? "" : rijec1.trim();
        String drugaRijec = (rijec2 == null) ? "" : rijec2.trim();
        
        return prvaRijec.equalsIgnoreCase(drugaRijec);
    }

=======
>>>>>>> refs/remotes/origin/main
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
