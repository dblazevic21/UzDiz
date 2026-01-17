package edu.unizg.foi.uzdiz.dblazevic21.app.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CsvParserApp 
{
	
	public static final DateTimeFormatter CSV_DATUM = new DateTimeFormatterBuilder()
            .parseLenient()
            .appendPattern("d.M.yyyy")
            .optionalStart().appendLiteral('.').optionalEnd()
            .toFormatter(Locale.ROOT);

    public static final DateTimeFormatter CSV_VRIJEME = new DateTimeFormatterBuilder()
            .parseLenient()
            .appendValue(ChronoField.HOUR_OF_DAY)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
            .optionalEnd()
            .toFormatter(Locale.ROOT);

    public static final DateTimeFormatter[] CSV_DATUM_VRIJEME = new DateTimeFormatter[] {
        new DateTimeFormatterBuilder()
            .parseLenient()
            .appendPattern("d.M.yyyy")
            .optionalStart().appendLiteral('.').optionalEnd()
            .appendLiteral(' ')
            .appendValue(ChronoField.HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 1, 2, SignStyle.NOT_NEGATIVE)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 1, 2, SignStyle.NOT_NEGATIVE)
            .optionalEnd()
            .toFormatter(Locale.ROOT),
        new DateTimeFormatterBuilder()
            .parseLenient()
            .appendPattern("dd.MM.yyyy")
            .optionalStart().appendLiteral('.').optionalEnd()
            .appendLiteral(' ')
            .appendValue(ChronoField.HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE)
            .appendLiteral(':')
            .appendValue(ChronoField.MINUTE_OF_HOUR, 1, 2, SignStyle.NOT_NEGATIVE)
            .optionalStart()
            .appendLiteral(':')
            .appendValue(ChronoField.SECOND_OF_MINUTE, 1, 2, SignStyle.NOT_NEGATIVE)
            .optionalEnd()
            .toFormatter(Locale.ROOT)
    };



    public static List<String> parseCsvLiniju(String linija) 
    {
        List<String> izlaz = new ArrayList<>();
        StringBuilder trenutnaLinija = new StringBuilder();
        boolean uNavodnicima = false;

        for (int i = 0; i < linija.length(); i++)
        {
            char c = linija.charAt(i);

            if (c == '\"') 
            {
                uNavodnicima = !uNavodnicima;
                trenutnaLinija.append(c);
            } 
            else if (c == ',' && !uNavodnicima) 
            {
                izlaz.add(trenutnaLinija.toString().trim());
                trenutnaLinija.setLength(0);
            } 
            else 
            {
                trenutnaLinija.append(c);
            }
        }
        izlaz.add(trenutnaLinija.toString().trim());
        
        return izlaz;
    }
    
    public static int uInt(String s) 
    {
        try 
        {
            s = (s == null) ? "" : s.trim();
            if (s.isEmpty())
            {
            	return 0;	
            }
            return Integer.parseInt(s);
        } 
        catch (Exception e) 
        {
            return 0;
        }
    }

    public static float uFloat(String s)
    {
        try 
        {
            s = (s == null) ? "" : s.trim().replace(',', '.');
            if (s.isEmpty())
            {
            	return 0f;
            }
            return Float.parseFloat(s);
        } 
        catch (Exception e) 
        {
            return 0f;
        }
    }
    
    public static LocalDate uDatum(String s) 
    {
        try
        {
            s = (s == null) ? "" : s.trim();
            if (s.isEmpty()) return null;
            return LocalDate.parse(s, CSV_DATUM);
        } 
        catch (Exception e)
        {
            return null;
        }
    }

    public static LocalTime uVrijeme(String s) 
    {
        try 
        {
            s = (s == null) ? "" : s.trim();
            if (s.isEmpty()) return null;
            return LocalTime.parse(s, CSV_VRIJEME);
        } 
        catch (Exception e) 
        {
            return null;
        }
    }

    public static LocalDateTime uDatumVrijeme(String s)
    {
        if (s == null) return null;
        String t = s.trim();
        for (DateTimeFormatter f : CSV_DATUM_VRIJEME)
        {
            try
            {
                return LocalDateTime.parse(t, f);
            }
            catch (Exception ignoriran) {}
        }
        System.out.println("Neuspjelo parsanje datuma: '" + s + "'");
        return null;
    }

}
