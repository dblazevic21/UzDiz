package edu.unizg.foi.uzdiz.dblazevic21.lib.facade;

import edu.unizg.foi.uzdiz.dblazevic21.lib.handleri.CsvUcitajSingleton;

import java.util.List;

public class TuristickaFacade
{
    private static volatile TuristickaFacade INSTANCE;
    private final CsvUcitajSingleton csvHandler;

    private TuristickaFacade()
    {
        this.csvHandler = CsvUcitajSingleton.getInstance();
    }

    public static TuristickaFacade getInstance()
    {
        if (INSTANCE == null)
        {
            synchronized (TuristickaFacade.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new TuristickaFacade();
                }
            }
        }
        return INSTANCE;
    }
    
    public void resetirajAranzmane()
    {
        csvHandler.resetirajAranzmane();
    }

    public void resetirajRezervacije()
    {
        csvHandler.resetirajRezervacije();
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
    
    public List<List<String>> ucitajSveRezervacije(List<String> putanje) 
    {
        csvHandler.ucitajSveRezervacije(putanje);
        return csvHandler.getRezervacije();
    }
    
    public int getBrojGresaka()
    {
        return CsvUcitajSingleton.brojGreske;
    }
}
