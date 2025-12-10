package edu.unizg.foi.uzdiz.dblazevic21.lib.facade;

import java.util.ArrayList;
import java.util.List;

import edu.unizg.foi.uzdiz.dblazevic21.lib.csv.AranzmanCsvRecord;
import edu.unizg.foi.uzdiz.dblazevic21.lib.csv.RezervacijaCsvRecord;
import edu.unizg.foi.uzdiz.dblazevic21.lib.handleri.CsvUcitajSingleton;

public class Facade 
{

    private static volatile Facade INSTANCE;

    private final CsvUcitajSingleton csvUcitaj;

    private Facade() 
    {
        this.csvUcitaj = CsvUcitajSingleton.getInstance();
    }

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
    
    public List<AranzmanCsvRecord> ucitajAranzmaneSve(List<String> putanje) 
    {
        List<AranzmanCsvRecord> rezultat = new ArrayList<>();
        if (putanje == null) 
        {
            return rezultat;
        }
        for (String p : putanje) {
            rezultat.addAll(csvUcitaj.ucitajAranzmaneKaoRecorde(p));
        }
        return rezultat;
    }
    
    public List<RezervacijaCsvRecord> ucitajRezervacijeSve(List<String> putanje) 
    {
        List<RezervacijaCsvRecord> rezultat = new ArrayList<>();
        if (putanje == null) 
        {
            return rezultat;
        }
        for (String p : putanje) 
        {
            rezultat.addAll(csvUcitaj.ucitajRezervacijeKaoRecorde(p));
        }
        return rezultat;
    }
}
