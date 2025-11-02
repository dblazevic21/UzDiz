package edu.unizg.foi.uzdiz.dblazevic21.handleri;

import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.AranzmaniBuilder;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.AranzmaniBuilderConcrete;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacije;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class CsvUcitajSingleton {

    private static volatile CsvUcitajSingleton INSTANCE;

    private final Map<Integer, Aranzmani> aranzmani = new HashMap<>();

    private static final DateTimeFormatter CSV_DATE = new DateTimeFormatterBuilder()
            .parseLenient()
            .appendPattern("d.M.yyyy")
            .optionalStart().appendLiteral('.').optionalEnd()
            .toFormatter(Locale.ROOT);

    private static final DateTimeFormatter CSV_TIME = new DateTimeFormatterBuilder()
            .parseLenient()
            .appendValue(ChronoField.HOUR_OF_DAY)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .optionalEnd()
            .toFormatter(Locale.ROOT);

    private static final DateTimeFormatter[] CSV_DATE_TIME = new DateTimeFormatter[] {
            new DateTimeFormatterBuilder()
                    .parseLenient()
                    .appendPattern("d.M.yyyy")
                    .optionalStart().appendLiteral('.').optionalEnd()
                    .appendLiteral(' ')
                    .appendValue(ChronoField.HOUR_OF_DAY)
                    .appendLiteral(':')
                    .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                    .optionalStart()
                    .appendLiteral(':')
                    .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
                    .optionalEnd()
                    .toFormatter(Locale.ROOT),
                    
                    
            new DateTimeFormatterBuilder()
                    .parseLenient()
                    .appendPattern("d.M.yyyy")
                    .optionalStart().appendLiteral('.').optionalEnd()
                    .appendLiteral(' ')
                    .appendValue(ChronoField.HOUR_OF_DAY)
                    .appendLiteral(':')
                    .appendValue(ChronoField.MINUTE_OF_HOUR, 1)
                    .toFormatter(Locale.ROOT)
    };

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

    public Map<Integer, Aranzmani> getAranzmani() {
        return aranzmani;
    }

    public void ucitajAranzmane(String putanja)
    {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(putanja), StandardCharsets.UTF_8))
        {
            String linija = br.readLine();
            if (linija == null) return;

            while ((linija = br.readLine()) != null) {
                linija = linija.trim();
                if (linija.isEmpty()) continue;

                List<String> cols = parseCsvLiniju(linija);
                if (cols.size() < 16) {
                    continue;
                }

                int oznaka = toInt(cols.get(0));
                String naziv = makniNavodnike(cols.get(1));
                String program = makniNavodnike(cols.get(2));
                LocalDate pocetni = toDate(cols.get(3));
                LocalDate zavrsni = toDate(cols.get(4));
                LocalTime vrijemeKretanja = toTime(cols.get(5));
                LocalTime vrijemePovratka = toTime(cols.get(6));
                float cijena = toFloat(cols.get(7));
                int min = toInt(cols.get(8));
                int max = toInt(cols.get(9));
                int nocenja = toInt(cols.get(10));
                float doplata = toFloat(cols.get(11));
                String prijevoz = makniNavodnike(cols.get(12));
                int dor = toInt(cols.get(13));
                int ruc = toInt(cols.get(14));
                int vec = toInt(cols.get(15));

                AranzmaniBuilder builder = new AranzmaniBuilderConcrete()
                        .kreirajAranzmane(
                                oznaka, naziv, program,
                                pocetni, zavrsni,
                                vrijemeKretanja, vrijemePovratka,
                                cijena, min, max, nocenja, doplata,
                                prijevoz, dor, ruc, vec
                        );

                Aranzmani a = builder.getAranzman();
                if (a != null) {
                    aranzmani.put(oznaka, a);
                }
            }
        } catch (IOException e) {
            System.out.println("Greška pri čitanju datoteke aranžmana: " + putanja + " (" + e.getMessage() + ")");
        }
    }

    public void ucitajRezervacije(String putanja)
    {
        Rezervacije rez = Rezervacije.getInstance();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(putanja), StandardCharsets.UTF_8)) 
        {
            String linija = br.readLine();
            if (linija == null) return;
            
            int brojLinije = 1;

            while ((linija = br.readLine()) != null) 
            {
				brojLinije++;
				
                linija = linija.trim();
                if (linija.isEmpty()) continue;

                List<String> cols = parseCsvLiniju(linija);
                if (cols.size() < 4) 
                {
                    System.out.println("Greška u retku " + brojLinije);
                    continue;
                }

                try
                {
                	String ime = makniNavodnike(cols.get(0));
                    String prezime = makniNavodnike(cols.get(1));
                    int oznaka = toInt(cols.get(2));
                    String dtRaw = makniNavodnike(cols.get(3));
                    LocalDateTime dt = toDateTime(dtRaw);
                    
                    if (!aranzmani.containsKey(oznaka)) 
                    {
                        System.out.println("Upozorenje: Nepostojeći aranžman (" + oznaka + ") u " + putanja + " – red preskočen.");
                        continue;
                    }

                    if (dt == null) 
                    {
                        rez.dodajRezervaciju(ime, prezime, oznaka, dtRaw);
                    } 
                    else 
                    {
                        rez.dodajRezervaciju(ime, prezime, oznaka, dt);
                    }
                }
                catch (Exception e) 
                {
                    System.out.println("Greška u retku " + brojLinije + ": " + e.getMessage() + ". Redak preskočen.");
                }
            }
            
            rez.azurirajStatuseRezervacija(aranzmani);
        } 
        catch (IOException e) 
        {
            System.out.println("Greška pri čitanju datoteke rezervacija: " + putanja + " (" + e.getMessage() + ")");
        }
    }

    private static String makniNavodnike(String s) 
    {
        if (s == null) return null;
        
        String t = s.trim();
        if (t.length() >= 2 && t.startsWith("\"") && t.endsWith("\"")) 
        {
            t = t.substring(1, t.length() - 1);
        }
        return t;
    }

    private static int toInt(String s) 
    {
        try 
        {
            s = (s == null) ? "" : s.trim();
            if (s.isEmpty()) return 0;
            return Integer.parseInt(s);
        } 
        catch (Exception e) 
        {
            return 0;
        }
    }

    private static float toFloat(String s) {
        try 
        {
            s = (s == null) ? "" : s.trim().replace(',', '.');
            if (s.isEmpty()) return 0f;
            return Float.parseFloat(s);
        } 
        catch (Exception e) 
        {
            return 0f;
        }
    }

    private static LocalDate toDate(String s) 
    {
        try
        {
            s = (s == null) ? "" : s.trim();
            if (s.isEmpty()) return null;
            return LocalDate.parse(s, CSV_DATE);
        } 
        catch (Exception e)
        {
            return null;
        }
    }

    private static LocalTime toTime(String s) 
    {
        try 
        {
            s = (s == null) ? "" : s.trim();
            if (s.isEmpty()) return null;
            return LocalTime.parse(s, CSV_TIME);
        } 
        catch (Exception e) 
        {
            return null;
        }
    }

    private static LocalDateTime toDateTime(String s)
    {
        if (s == null) return null;
        
        String t = s.trim();
        for (DateTimeFormatter f : CSV_DATE_TIME)
        {
            try
            {
                return LocalDateTime.parse(t, f);
            } 
            catch (Exception ignored) {}
        }
        return null;
    }

    private static List<String> parseCsvLiniju(String linija) 
    {
        List<String> out = new ArrayList<>();
        StringBuilder trenutnaLinija = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < linija.length(); i++) 
        {
            char c = linija.charAt(i);

            if (c == '\"')
            {
                inQuotes = !inQuotes;
                trenutnaLinija.append(c);
            } 
            else if (c == ',' && !inQuotes)
            {
                out.add(trenutnaLinija.toString().trim());
                trenutnaLinija.setLength(0);
            } 
            else 
            {
                trenutnaLinija.append(c);
            }
        }
        out.add(trenutnaLinija.toString().trim());
        return out;
    }
}
