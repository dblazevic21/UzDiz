package edu.unizg.foi.uzdiz.dblazevic21.lib.handleri;

import edu.unizg.foi.uzdiz.dblazevic21.lib.ispis.IspisiGresku;
import edu.unizg.foi.uzdiz.dblazevic21.lib.util.CsvParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvUcitajSingleton
{

    private static volatile CsvUcitajSingleton INSTANCE;
    public static int brojGreske = 0;

    private final List<List<String>> aranzmani = new ArrayList<>();
    private final List<List<String>> rezervacije = new ArrayList<>();

    private CsvUcitajSingleton() {}

    public static CsvUcitajSingleton getInstance() 
    {
        if (INSTANCE == null) 
        {
            synchronized (CsvUcitajSingleton.class)
            {
                if (INSTANCE == null) 
                {
                    INSTANCE = new CsvUcitajSingleton();
                }
            }
        }
        return INSTANCE;
    }
    
    public void resetirajAranzmane() 
    {
        aranzmani.clear();
    }

    public void resetirajRezervacije() 
    {
        rezervacije.clear();
    }

    public void ucitajAranzmane(String putanja) 
    {
        List<List<String>> rezultat = ucitajCsv(putanja, 16);
        aranzmani.addAll(rezultat);
    }

    public void ucitajRezervacije(String putanja) 
    {
        List<List<String>> rezultat = ucitajCsv(putanja, 4);
        rezervacije.addAll(rezultat);
    }
    
    public void ucitajSveRezervacije(List<String> putanje) 
    {
        for (String putanja : putanje) 
        {
            ucitajRezervacije(putanja);
        }
    }

    public List<List<String>> getAranzmani() 
    {
        return new ArrayList<>(aranzmani);
    }

    public List<List<String>> getRezervacije() 
    {
        return new ArrayList<>(rezervacije);
    }

    private List<List<String>> ucitajCsv(String putanja, int ocekivaniBrojStupaca)
    {
        List<List<String>> rezultat = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(putanja), StandardCharsets.UTF_8))
        {
            String linija = br.readLine();
            if (linija == null) 
            {
                return rezultat;
            }

            int brojLinije = 1;

            while ((linija = br.readLine()) != null) 
            {
                brojLinije++;

                String raw = linija;
                linija = linija.trim();
                if (linija.isEmpty()) 
                {
                    continue;
                }

                List<String> stupci = CsvParser.parseCsvLiniju(linija);
                List<String> razloziGreske = new ArrayList<>();

                if (stupci.size() != ocekivaniBrojStupaca)
                {
                    razloziGreske.add("Neispravan broj stupaca: " + stupci.size() + " (očekivano: " + ocekivaniBrojStupaca + ")");
                }

                if (!razloziGreske.isEmpty()) 
                {
                    ++brojGreske;
                    IspisiGresku.ispisiGresku(brojGreske, brojLinije, raw, razloziGreske, putanja);
                    continue;
                }

                rezultat.add(stupci);
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Greška pri čitanju datoteke: " + putanja + " (" + e.getMessage() + ")");
        }

        return rezultat;
    }
}