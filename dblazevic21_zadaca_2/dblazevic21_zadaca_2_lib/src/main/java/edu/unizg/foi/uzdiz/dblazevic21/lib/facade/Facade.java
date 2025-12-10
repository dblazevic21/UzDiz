package edu.unizg.foi.uzdiz.dblazevic21.lib.facade;

import edu.unizg.foi.uzdiz.dblazevic21.lib.handleri.CsvUcitajSingleton;

import java.util.List;

public class Facade
{
    private final CsvUcitajSingleton csvHandler;

    
    public Facade() {
        this.csvHandler = CsvUcitajSingleton.getInstance();
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
