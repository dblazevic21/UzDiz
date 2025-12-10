package edu.unizg.foi.uzdiz.dblazevic21.lib.facade;

import java.util.ArrayList;
import java.util.List;

import edu.unizg.foi.uzdiz.dblazevic21.lib.csv.AranzmanCsvRecord;
import edu.unizg.foi.uzdiz.dblazevic21.lib.csv.RezervacijaCsvRecord;
import edu.unizg.foi.uzdiz.dblazevic21.lib.handleri.CsvUcitajSingleton;
import edu.unizg.foi.uzdiz.dblazevic21.lib.util.CsvParser;

public class Facade 
{
    private static volatile Facade INSTANCE;

    private Facade() {}

    public static Facade getInstance() 
    {
        if (INSTANCE == null) 
        {
            synchronized (Facade.class) 
            {
                if (INSTANCE == null) 
                {
                    INSTANCE = new Facade();
                }
            }
        }
        return INSTANCE;
    }

    public List<Object[]> ucitajAranzmane(String putanja) 
    {
        List<AranzmanCsvRecord> records = CsvUcitajSingleton.getInstance().ucitajAranzmaneKaoRecorde(putanja);
        List<Object[]> rezultat = new ArrayList<>();

        for (AranzmanCsvRecord r : records) 
        {
            Object[] podaci = new Object[16];
            podaci[0] = CsvParser.uInt(r.oznaka());
            podaci[1] = r.naziv();
            podaci[2] = r.program();
            podaci[3] = CsvParser.uDatum(r.pocetniDatum());
            podaci[4] = CsvParser.uDatum(r.zavrsniDatum());
            podaci[5] = CsvParser.uVrijeme(r.vrijemeKretanja());
            podaci[6] = CsvParser.uVrijeme(r.vrijemePovratka());
            podaci[7] = CsvParser.uFloat(r.cijena());
            podaci[8] = CsvParser.uInt(r.minBrojPutnika());
            podaci[9] = CsvParser.uInt(r.maksBrojPutnika());
            podaci[10] = CsvParser.uInt(r.brojNocenja());
            podaci[11] = CsvParser.uFloat(r.doplataJednokrevetnaSoba());
            podaci[12] = r.prijevoz();
            podaci[13] = CsvParser.uInt(r.brojDorucaka());
            podaci[14] = CsvParser.uInt(r.brojRuckova());
            podaci[15] = CsvParser.uInt(r.brojVecera());
            rezultat.add(podaci);
        }

        return rezultat;
    }

    public List<Object[]> ucitajRezervacije(String putanja) 
    {
        List<RezervacijaCsvRecord> records = CsvUcitajSingleton.getInstance().ucitajRezervacijeKaoRecorde(putanja);
        List<Object[]> rezultat = new ArrayList<>();

        for (RezervacijaCsvRecord r : records) 
        {
            Object[] podaci = new Object[4];
            podaci[0] = r.ime();
            podaci[1] = r.prezime();
            podaci[2] = CsvParser.uInt(r.oznakaAranzmana());
            podaci[3] = CsvParser.uDatumVrijeme(r.datumVrijeme());
            rezultat.add(podaci);
        }

        return rezultat;
    }

    public int getBrojGresaka() 
    {
        return CsvUcitajSingleton.brojGreske;
    }
}
