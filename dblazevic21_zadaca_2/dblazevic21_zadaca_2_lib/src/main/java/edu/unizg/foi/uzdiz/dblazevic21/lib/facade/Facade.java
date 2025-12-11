package edu.unizg.foi.uzdiz.dblazevic21.lib.facade;

import edu.unizg.foi.uzdiz.dblazevic21.lib.handleri.CsvUcitajSingleton;

import java.util.List;

public class Facade
{
    private static volatile Facade INSTANCE;
    private final CsvUcitajSingleton csvHandler;

    private Facade()
    {
        this.csvHandler = CsvUcitajSingleton.getInstance();
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

    public List<List<String>> ucitajAranzmane(String putanja)
    {
        csvHandler.ucitajAranzmane(putanja);
        return csvHandler.getAranzmani();
    }

    public List<List<String>> ucitajRezervacije(String putanja)
    {
        csvHandler.ucitajRezervacije(putanja);
        return csvHandler.getRezervacije();
    }

    public int getBrojGresaka()
    {
        return CsvUcitajSingleton.brojGreske;
    }
}
