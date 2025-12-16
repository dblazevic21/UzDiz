package edu.unizg.foi.uzdiz.dblazevic21.app.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class DatumParserApp
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
        String d = (datum == null) ? "" : datum.trim();
        String v = (vrijeme == null) ? "" : vrijeme.trim();
        String combined = (d + " " + v).trim();
        return CsvParserApp.uDatumVrijeme(combined);
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
            if (t.endsWith("."))
            {
                t = t.substring(0, t.length() - 1);
            }

            DateTimeFormatter csvDatum = new DateTimeFormatterBuilder()
                    .parseLenient()
                    .appendPattern("d.M.yyyy")
                    .optionalStart().appendLiteral('.').optionalEnd()
                    .toFormatter(Locale.ROOT);
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
