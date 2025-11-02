package edu.unizg.foi.uzdiz.dblazevic21.ispis;

import java.time.LocalDate;
import java.time.LocalTime;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Formater 
{
    private static final DateTimeFormatter datumFmt = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private static final DateTimeFormatter vrijemeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String fmtDatum(LocalDate d) 
    {
        return (d == null) ? "-" : d.format(datumFmt);
    }

    public static String fmtVrijeme(LocalTime t)
    {
        return (t == null) ? "-" : t.format(vrijemeFmt);
    }

    public static String fmtCijena(float c) 
    {
        return String.format(Locale.ROOT, "%.2f", c);
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
}
