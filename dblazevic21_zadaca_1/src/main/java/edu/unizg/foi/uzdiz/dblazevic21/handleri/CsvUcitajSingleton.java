package edu.unizg.foi.uzdiz.dblazevic21.handleri;

import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacije;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CsvUcitajSingleton 
{
    private static volatile CsvUcitajSingleton INSTANCE;

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

    public void ucitajRezervacije(String nazivDatoteke)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(nazivDatoteke))) 
        {
            String linija;
            boolean prvaLinija = true;

            while ((linija = br.readLine()) != null) 
            {
                if (prvaLinija) 
                {
                    prvaLinija = false;
                    continue;
                }

                String[] podaci = linija.split(",");
                if (podaci.length < 4) 
                {
                    continue;
                }

                String ime = podaci[0].trim();
                String prezime = podaci[1].trim();
                int oznakaAranzmana = Integer.parseInt(podaci[2].trim());
                String datumVrijeme = podaci[3].trim();

                LocalDateTime datum = parseDatumVrijeme(datumVrijeme);

                if (datum != null) 
                {
                    Rezervacije.getInstance().dodajRezervaciju(ime, prezime, oznakaAranzmana, datum.format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")));
                } 
                else 
                {
                    System.out.println("Neispravan format datuma: " + datumVrijeme);
                }
            }
        } 
        catch (IOException e)
        {
            System.out.println("Greška pri čitanju datoteke: " + nazivDatoteke);
        }
    }

    private LocalDateTime parseDatumVrijeme(String datumVrijeme)
    {
        DateTimeFormatter[] formati = {
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", Locale.getDefault()),
            DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm", Locale.getDefault()),
            DateTimeFormatter.ofPattern("d.M.yyyy H:mm", Locale.getDefault())
        };

        for (DateTimeFormatter formatter : formati)
        {
            try 
            {
                return LocalDateTime.parse(datumVrijeme, formatter);
            } 
            catch (Exception ignored) 
            {
            	
            }
        }
        return null;
    }

    public void ucitajAranzmane(String nazivDatoteke) 
    {
        try (BufferedReader br = new BufferedReader(new FileReader(nazivDatoteke))) 
        {
            String linija;
            boolean prvaLinija = true;

            while ((linija = br.readLine()) != null) 
            {
                if (prvaLinija)
                {
                    prvaLinija = false;
                    continue;
                }
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Greška pri čitanju datoteke: " + nazivDatoteke);
        }
    }
}
