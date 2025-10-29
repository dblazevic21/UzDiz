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
            int brojac = 0;
            DateTimeFormatter izlazniFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy. hh:mm:ss");

            while ((linija = br.readLine()) != null) 
            {
                if (prvaLinija) 
                {
                    prvaLinija = false;
                    continue;
                }

                if (linija.trim().isEmpty()) continue;

                String[] podaci = linija.split(",");
                if (podaci.length < 4) 
                {
                    continue;
                }

                String ime = podaci[0].trim();
                String prezime = podaci[1].trim();
                int oznakaAranzmana = parseIntOrDefault(podaci[2].trim(), -1);
                String datumVrijeme = podaci[3].trim();

                LocalDateTime datum = parseDatumVrijeme(datumVrijeme);

                if (datum != null) 
                {
                    String formatiraniDatum = datum.format(izlazniFormat);
                    //System.out.println("✅ Parsiran datum: " + formatiraniDatum + " (" + ime + " " + prezime + ")");
                    Rezervacije.getInstance().dodajRezervaciju(ime, prezime, oznakaAranzmana, formatiraniDatum);
                    brojac++;
                } 
                else 
                {
                    
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
        if (datumVrijeme == null || datumVrijeme.isBlank()) return null;

        DateTimeFormatter[] formati = formatiDatuma();

        String normaliziran = datumVrijeme.trim();

        for (DateTimeFormatter formatter : formati) 
        {
            try 
            {
                LocalDateTime parsed = LocalDateTime.parse(normaliziran, formatter);
                LocalDateTime minDate = LocalDateTime.of(2000, 1, 1, 0, 0);
                LocalDateTime maxDate = LocalDateTime.of(2100, 12, 31, 23, 59);
                if (parsed.isBefore(minDate) || parsed.isAfter(maxDate)) 
                {
                    System.out.println("Datum izvan raspona: " + datumVrijeme);
                    return null;
                }
                return parsed;
            } 
            catch (Exception e) 
            {
            	
            }
        }

        try 
        {
            String[] parts = normaliziran.split(" ");
            if (parts.length == 2) 
            {
                String datePart = parts[0];
                String timePart = parts[1];
                String[] dateParts = datePart.split("\\.");
                String[] timeParts = timePart.split(":");
                int d = Integer.parseInt(dateParts[0]);
                int m = Integer.parseInt(dateParts[1]);
                int y = Integer.parseInt(dateParts[2]);
                int h = Integer.parseInt(timeParts[0]);
                int min = Integer.parseInt(timeParts[1]);
                int s = (timeParts.length >= 3) ? Integer.parseInt(timeParts[2]) : 0;
                return LocalDateTime.of(y, m, d, h, min, s);
            }
        } 
        catch (Exception ex) 
        {
        	
        }

        System.out.println("Neuspjelo parsiranje datuma: " + datumVrijeme);
        return null;
    }

	public DateTimeFormatter[] formatiDatuma() 
	{
		DateTimeFormatter[] formati = {
            DateTimeFormatter.ofPattern("d.M.yyyy H:m[:s]"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"),
            DateTimeFormatter.ofPattern("d.M.yyyy H:mm:ss"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"),
            DateTimeFormatter.ofPattern("d.M.yyyy H:mm"),
            DateTimeFormatter.ofPattern("d.M.yyyy HH:mm"),
            DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm")
        };
		return formati;
	}

    private int parseIntOrDefault(String value, int defaultValue) 
    {
        try 
        {
            return value != null && !value.trim().isEmpty() ? Integer.parseInt(value.trim()) : defaultValue;
        } 
        catch (NumberFormatException e) 
        {
            return defaultValue;
        }
    }

    private double parseDoubleOrDefault(String value, double defaultValue) 
    {
        try 
        {
            return value != null && !value.trim().isEmpty() ? Double.parseDouble(value.trim()) : defaultValue;
        } 
        catch (NumberFormatException e) 
        {
            return defaultValue;
        }
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

                String[] podaci = linija.split(",");
                if (podaci.length < 5)
                {
                    System.out.println("Preskačem liniju zbog nedovoljno podataka: " + linija);
                    continue;
                }

                provjeriAtribute(linija, podaci);
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Greška pri čitanju datoteke: " + nazivDatoteke);
        }
    }

    public void provjeriAtribute(String linija, String[] podaci) 
    {
        try 
        {
            int oznaka = Integer.parseInt(podaci[0].trim());
            String naziv = podaci[1].trim();
            String program = podaci[2].trim();
            String pocetniDatum = podaci[3].trim();
            String zavrsniDatum = podaci[4].trim();

            String vrijemeKretanja = podaci.length > 5 && !podaci[5].trim().isEmpty() ? podaci[5].trim() : null;
            String vrijemePovratka = podaci.length > 6 && !podaci[6].trim().isEmpty() ? podaci[6].trim() : null;
            double cijena = parseDoubleOrDefault(podaci.length > 7 ? podaci[7].trim() : "", 0.0);
            int minBrojPutnika = parseIntOrDefault(podaci.length > 8 ? podaci[8].trim() : "", 0);
            int maxBrojPutnika = parseIntOrDefault(podaci.length > 9 ? podaci[9].trim() : "", 0);
            int brojNocenja = parseIntOrDefault(podaci.length > 10 ? podaci[10].trim() : "", 0);
            double doplataJednokrevetna = parseDoubleOrDefault(podaci.length > 11 ? podaci[11].trim() : "", 0.0);
            String prijevoz = podaci.length > 12 && !podaci[12].trim().isEmpty() ? podaci[12].trim() : null;
            int brojDorucka = parseIntOrDefault(podaci.length > 13 ? podaci[13].trim() : "", 0);
            int brojRuckova = parseIntOrDefault(podaci.length > 14 ? podaci[14].trim() : "", 0);
            int brojVecera = parseIntOrDefault(podaci.length > 15 ? podaci[15].trim() : "", 0);
        } 
        catch (NumberFormatException e) 
        {
            
        }
    }
}
