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
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacije;


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
        for (List<String> p : podaci) 
        {
            int oznaka = Integer.parseInt(p.get(0));
            String naziv = p.get(1);
            String program = p.get(2);
            LocalDate pocetniDatum = LocalDate.parse(p.get(3));
            LocalDate zavrsniDatum = LocalDate.parse(p.get(4));
            LocalTime vrijemeKretanja = LocalTime.parse(p.get(5));
            LocalTime vrijemePovratka = LocalTime.parse(p.get(6));
            float cijena = Float.parseFloat(p.get(7));
            int minBrojPutnika = Integer.parseInt(p.get(8));
            int maksBrojPutnika = Integer.parseInt(p.get(9));
            int brojNocenja = Integer.parseInt(p.get(10));
            float doplataSobe = Float.parseFloat(p.get(11));
            String prijevoz = p.get(12);
            int brojDorucka = Integer.parseInt(p.get(13));
            int brojRucka = Integer.parseInt(p.get(14));
            int brojVecera = Integer.parseInt(p.get(15));

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
    }

    public void ucitajRezervacije(List<List<String>> podaci) 
    {
        Rezervacije rez = Rezervacije.getInstance();

        for (List<String> p : podaci) 
        {
            String ime = p.get(0);
            String prezime = p.get(1);
            int oznakaAranzmana = Integer.parseInt(p.get(2));
            LocalDateTime datumVrijeme = LocalDateTime.parse(p.get(3));

            if (datumVrijeme != null) 
            {
                rez.dodajRezervaciju(ime, prezime, oznakaAranzmana, datumVrijeme);
            }
        }
    }


    public Map<Integer, Aranzmani> getAranzmani() 
    {
        return aranzmani;
    }
}
