package edu.unizg.foi.uzdiz.dblazevic21.ispis;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormaterZaIspise 
{
    private static final DateTimeFormatter datumIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private static final DateTimeFormatter vrijemeIspis = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String fmtDatum(LocalDate d) 
    {
        return (d == null) ? "-" : d.format(datumIspis);
    }

    public static String fmtVrijeme(LocalTime t)
    {
        return (t == null) ? "-" : t.format(vrijemeIspis);
    }

    public static String fmtDatumVrijeme(LocalDateTime dt, String raw) 
    {
        if (dt != null) 
        {
            return (dt.toLocalDate().format(datumIspis) + " " + dt.toLocalTime().format(vrijemeIspis)).trim();
        }
        if (raw != null && !raw.isBlank()) 
        {
            return raw.trim();
        }
        return "-";
    }
    
    public static String fmtCijena(float c) 
    {
        return String.format(Locale.ROOT, "%.2f", c);
    }
    
    public static Float parseCijenu(String s)
    {
        try
        {
            String t = (s == null) ? "" : s.trim().replace(',', '.');
            if (t.isEmpty())
            {
                return null;
            }
            return Float.parseFloat(t);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static String val(Object o) 
    {
        return (o == null) ? "-" : o.toString();
    }

    public static String izrezi(String s, int max) 
    {
        if (s == null) return "-";
        
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }
    public static String normalizirajDatum(String s)
    {
        if (s == null) return "";
        String x = s.trim().replaceAll("\\.+", ".");
        if (!x.endsWith(".")) 
        {
            x += ".";
        }
        return x;
    }

    public static LocalDate parseDatumZaKomandu(String s)
    {
        try 
        {
            String t = (s == null) ? "" : s.trim();
            if (t.endsWith(".")) 
            {
                t = t.substring(0, t.length() - 1);
            }
            DateTimeFormatter inputDate = new DateTimeFormatterBuilder()
                    .parseLenient()
                    .appendPattern("d.M.yyyy")
                    .optionalStart().appendLiteral('.').optionalEnd()
                    .toFormatter(Locale.ROOT);
            return LocalDate.parse(t, inputDate);
        } 
        catch (Exception ignored) 
        {
            return null;
        }
    }
    
    public static LocalDateTime normalizirajDatumIVrijeme(String datum, String vrijeme) 
    {
        try 
        {
            datum = datum.trim().replaceAll("\\.+$", "");

            DateTimeFormatter fleksibilniDatum = new DateTimeFormatterBuilder()
                    .parseLenient()
                    .appendPattern("[d.M.yyyy][dd.MM.yyyy][d/M/yyyy][dd/MM/yyyy]")
                    .toFormatter(Locale.ROOT);

            LocalDate parsiranDatum = LocalDate.parse(datum.replace("/", ".").trim(), fleksibilniDatum);

            if (!vrijeme.matches("\\d{1,2}:\\d{1,2}:\\d{1,2}")) 
            {
                vrijeme += ":00";
            }

            DateTimeFormatter fleksibilnoVrijeme = new DateTimeFormatterBuilder()
                    .parseLenient()
                    .appendPattern("[H:mm:ss][HH:mm:ss][H:mm][HH:mm]")
                    .toFormatter(Locale.ROOT);

            LocalTime parsiranoVrijeme = LocalTime.parse(vrijeme.trim(), fleksibilnoVrijeme);

            return LocalDateTime.of(parsiranDatum, parsiranoVrijeme);
        } 
        catch (Exception e) 
        {
            return null;
        }
    }

    public static LocalDateTime parseDatumIVrijeme(String datum, String vrijeme)
    {
        return normalizirajDatumIVrijeme(datum, vrijeme);
    }
}
