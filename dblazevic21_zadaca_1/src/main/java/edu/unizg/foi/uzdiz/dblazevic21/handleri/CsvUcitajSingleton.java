package edu.unizg.foi.uzdiz.dblazevic21.handleri;

import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacije;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.ArrayList;

public class CsvUcitajSingleton 
{
    private static volatile CsvUcitajSingleton INSTANCE;
    
    private List<LocalDateTime> rezervacijeDatumi = new ArrayList<>();
    private List<LocalDateTime> aranzmaniDatumi = new ArrayList<>();

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
        int brojUspjesnih = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(nazivDatoteke)))
        {
            String linija;
            boolean prvaLinija = true;
            int brojac = 0;
            Rezervacije rezervacije = Rezervacije.getInstance();

            while ((linija = br.readLine()) != null) 
            {
                brojac++;
                if (prvaLinija)
                {
                    prvaLinija = false;
                    continue;
                }

                if (linija.trim().isEmpty()) 
                {
                    System.out.println("Pogreška u retku " + brojac + ": Prazan redak. Sadržaj: " + linija);
                    continue;
                }

                List<String> podaciLista = parseCSVLine(linija);
                String[] podaci = podaciLista.toArray(new String[0]);

                if (podaci.length < 4) 
                {
                    System.out.println("Pogreška u retku " + brojac + ": Nedovoljno podataka. Sadržaj: " + linija);
                    continue;
                }

                String ime = podaci[0].trim();
                String prezime = podaci[1].trim();
                int oznakaAranzmana = parseIntOrDefault(podaci[2].trim(), -1);
                String datumVrijeme = podaci[3].trim();
                

                if (ime.isEmpty() || prezime.isEmpty()) 
                {
                    System.out.println("Pogreška u retku " + brojac + ": Ime ili prezime je prazno. Sadržaj: " + linija);
                    continue;
                }

                if (oznakaAranzmana == -1) 
                {
                    System.out.println("Pogreška u retku " + brojac + ": Neispravna oznaka aranžmana. Sadržaj: " + linija);
                    continue;
                }

                LocalDateTime datum = parseDatumVrijeme(datumVrijeme);
                if (datum != null) 
                {
                    rezervacijeDatumi.add(datum);
                }
                
                if (datum == null)
                {
                    System.out.println("Pogreška u retku " + brojac + ": Neispravan datum i vrijeme. Sadržaj: " + linija);
                    continue;
                }

                boolean dodano = rezervacije.dodajRezervaciju(ime, prezime, oznakaAranzmana, datum);
                if (dodano) brojUspjesnih++;
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Greška pri čitanju datoteke: " + nazivDatoteke);
        }

        System.out.println("Ukupno učitano rezervacija iz " + nazivDatoteke + ": " + brojUspjesnih);
    }
    
    
    public void ucitajAranzmane(String nazivDatoteke) 
    {
        try (BufferedReader br = new BufferedReader(new FileReader(nazivDatoteke)))
        {
            StringBuilder sb = new StringBuilder();
            String linija;
            boolean prvaLinija = true;
            int brojac = 0;
            int brojUspjesnih = 0;

            while ((linija = br.readLine()) != null) 
            {
                brojac++;

                if (prvaLinija)
                {
                    prvaLinija = false;
                    continue;
                }

                sb.append(linija).append("\n");

                brojUspjesnih = provjeraAranzmana(sb, brojac, brojUspjesnih);
            }

            System.out.println("Ukupno učitano aranžmana: " + brojUspjesnih);

        } 
        catch (IOException e) 
        {
            System.out.println("Greška pri čitanju datoteke: " + nazivDatoteke);
        }
    }

    public int provjeraAranzmana(StringBuilder sb, int brojac, int brojUspjesnih) 
    {
        if (brojNavodnika(sb.toString()) % 2 == 0) 
        {
            String cijeliRed = sb.toString().trim();
            sb.setLength(0); 

            if (cijeliRed.isEmpty())
                return brojUspjesnih;

            List<String> podaciLista = parseCSVLine(cijeliRed);
            String[] podaci = podaciLista.toArray(new String[0]);

            if (podaci.length < 5) 
            {
                System.out.println("Pogreška u retku " + brojac + ": Nedovoljno podataka. Sadržaj: " + cijeliRed);
                return brojUspjesnih;
            }

            try 
            {
                int oznaka = Integer.parseInt(podaci[0].trim());
                String naziv = podaci[1].trim();
                String program = podaci[2].trim();
                String pocetniDatum = podaci[3].trim();
                String zavrsniDatum = podaci[4].trim();

                if (naziv.isEmpty() || program.isEmpty())
                {
                    System.out.println("Pogreška u retku " + brojac + ": Naziv ili program je prazan. Sadržaj: " + cijeliRed);
                    return brojUspjesnih;
                }

                LocalDateTime pocetak = parseDatumVrijeme(pocetniDatum);
                LocalDateTime kraj = parseDatumVrijeme(zavrsniDatum);

                if (pocetak != null) 
                {
                    aranzmaniDatumi.add(pocetak);
                }
                if (kraj != null) 
                {
                    aranzmaniDatumi.add(kraj);
                }
                
                if (pocetak == null || kraj == null) 
                {
                    System.out.println("Pogreška u retku " + brojac + ": Neispravan datum. Sadržaj: " + cijeliRed);
                    return brojUspjesnih;
                }

                brojUspjesnih++;
            } 
            catch (NumberFormatException e) 
            {
                System.out.println("Pogreška u retku " + brojac + ": Neispravan broj. Sadržaj: " + cijeliRed);
            }
        }
        return brojUspjesnih;
    }
    
    
    private LocalDateTime parseDatumVrijeme(String datumVrijeme) 
    {
        if (datumVrijeme == null || datumVrijeme.isBlank()) return null;

        String s = datumVrijeme.trim();

        DateTimeFormatter[] formati = new DateTimeFormatter[] {
            // 24h, opcionalne sekundice i vrime
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("d.M.yyyy")
                .optionalStart().appendLiteral('.').optionalEnd()
                .optionalStart()
                    .appendLiteral(' ')
                    .appendPattern("H:mm")
                    .optionalStart().appendLiteral(':').appendPattern("ss").optionalEnd()
                .optionalEnd()
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter(),
                
            // 12h AM/PM, opcionalne sekundice
            new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("d.M.yyyy")
                .optionalStart().appendLiteral('.').optionalEnd()
                .appendLiteral(' ')
                .appendPattern("hh:mm")
                .optionalStart().appendLiteral(':').appendPattern("ss").optionalEnd()
                .appendLiteral(' ')
                .appendPattern("a")
                .toFormatter()
        };

        for (DateTimeFormatter f : formati) 
        {
            try 
            {
                return LocalDateTime.parse(s, f);
            } 
            catch (Exception ignored) {}
        }

        System.out.println("Neuspjelo parsiranje datuma: " + datumVrijeme);
        return null;
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

    private List<String> parseCSVLine(String linija) 
    {
        List<String> result = new ArrayList<>();
        StringBuilder trenutni = new StringBuilder();
        boolean uNavodnicima = false;

        for (int i = 0; i < linija.length(); i++) 
        {
            char c = linija.charAt(i);

            if (c == '"') 
            {
                uNavodnicima = !uNavodnicima;
            }
            else if (c == ',' && !uNavodnicima) 
            {
                result.add(trenutni.toString().trim());
                trenutni.setLength(0);
            } 
            else 
            {
                trenutni.append(c);
            }
        }
        result.add(trenutni.toString().trim());
        return result;
    }
    
    private int brojNavodnika(String tekst) 
    {
        int count = 0;
        for (char c : tekst.toCharArray()) 
        {
            if (c == '"') count++;
        }
        return count;
    }
    
    public void ispisiDatume()
    {
    	System.out.println("Rezervacije datumi:");
        for (LocalDateTime datum : rezervacijeDatumi) 
        {
            System.out.println(datum);
        }

        System.out.println("Aranzmani datumi:");
        for (LocalDateTime datum : aranzmaniDatumi) 
        {
            System.out.println(datum);
        }
    }
    
}
