package edu.unizg.foi.uzdiz.dblazevic21.app.turistickaAgencija;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.AranzmaniBuilder;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.AranzmaniDirector;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.AranzmaniBuilderConcrete;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacije;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void ucitajAranzmane(List<Object[]> podaci) 
    {
        for (Object[] p : podaci) 
        {
            int oznaka = (Integer) p[0];
            String naziv = (String) p[1];
            String program = (String) p[2];
            LocalDate pocetniDatum = (LocalDate) p[3];
            LocalDate zavrsniDatum = (LocalDate) p[4];
            LocalTime vrijemeKretanja = (LocalTime) p[5];
            LocalTime vrijemePovratka = (LocalTime) p[6];
            float cijena = (Float) p[7];
            int minBrojPutnika = (Integer) p[8];
            int maksBrojPutnika = (Integer) p[9];
            int brojNocenja = (Integer) p[10];
            float doplataSobe = (Float) p[11];
            String prijevoz = (String) p[12];
            int brojDorucka = (Integer) p[13];
            int brojRucka = (Integer) p[14];
            int brojVecera = (Integer) p[15];

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

    public void ucitajRezervacije(List<Object[]> podaci) 
    {
        Rezervacije rez = Rezervacije.getInstance();

        for (Object[] p : podaci) 
        {
            String ime = (String) p[0];
            String prezime = (String) p[1];
            int oznakaAranzmana = (Integer) p[2];
            LocalDateTime datumVrijeme = (LocalDateTime) p[3];

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
