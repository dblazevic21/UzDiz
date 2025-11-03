package edu.unizg.foi.uzdiz.dblazevic21.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class DatumParser
{
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
            DateTimeFormatter ulazDatum = new DateTimeFormatterBuilder()
                    .parseLenient()
                    .appendPattern("d.M.yyyy")
                    .optionalStart().appendLiteral('.').optionalEnd()
                    .toFormatter(Locale.ROOT);
            return LocalDate.parse(t, ulazDatum);
        } 
        catch (Exception ignoriran) 
        {
            return null;
        }
    }
    
    public static LocalDateTime normalizirajDatumIVrijeme(String datum, String vrijeme) 
    {
        try 
        {
            String d = (datum == null) ? "" : datum.trim();
            d = d.replaceAll("\\s+", "");
            d = d.replace('-', '.').replace('/', '.');
            d = d.replaceAll("\\.+$", "");

            DateTimeFormatter fleksibilniDatum = new DateTimeFormatterBuilder()
                    .parseLenient()
                    .appendPattern("[d.M.yyyy][dd.MM.yyyy]")
                    .toFormatter(Locale.ROOT);

            LocalDate parsiranDatum = LocalDate.parse(d, fleksibilniDatum);

            String v = (vrijeme == null) ? "" : vrijeme.trim();
            if (v.matches("\\d{1,2}:\\d{1,2}")) 
            {
                v += ":00";
            }

            DateTimeFormatter fleksibilnoVrijeme = new DateTimeFormatterBuilder()
                    .parseLenient()
                    .appendPattern("[H:mm:ss][HH:mm:ss][H:mm][HH:mm]")
                    .toFormatter(Locale.ROOT);

            LocalTime parsiranoVrijeme = LocalTime.parse(v, fleksibilnoVrijeme);

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
    
    public static LocalDate parseCsvDatum(String s) 
    {
        try 
        {
            String t = (s == null) ? "" : s.trim();
            if (t.isEmpty()) return null;
            DateTimeFormatter csvDatum = DateTimeFormatter.ofPattern("d.M.yyyy").withLocale(Locale.ROOT);
            return LocalDate.parse(t, csvDatum);
        } 
        catch (Exception e) 
        {
            return null;
        }
    }

    public static LocalTime parseCsvVrijeme(String s) 
    {
        try 
        {
            String t = (s == null) ? "" : s.trim();
            if (t.isEmpty()) return null;
            DateTimeFormatter csvVrijeme = DateTimeFormatter.ofPattern("H:mm[:ss]").withLocale(Locale.ROOT);
            return LocalTime.parse(t, csvVrijeme);
        } 
        catch (Exception e)
        {
            return null;
        }
    }

    public static LocalDateTime parseCsvDatumVrijeme(String s, DateTimeFormatter[] formateri)
    {
        if (s == null) 
        {
        	return null;
        }
        String t = s.trim();
        
        for (DateTimeFormatter f : formateri) 
        {
            try 
            {
                return LocalDateTime.parse(t, f);
            } 
            catch (Exception ignoriran) 
            {
            	
            }
        }
        return null;
    }

}
