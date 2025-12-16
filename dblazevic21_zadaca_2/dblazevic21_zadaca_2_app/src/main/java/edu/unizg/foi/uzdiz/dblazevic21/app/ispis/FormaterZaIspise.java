package edu.unizg.foi.uzdiz.dblazevic21.app.ispis;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormaterZaIspise 
{
    private static final DateTimeFormatter datumIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private static final DateTimeFormatter vrijemeIspis = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final DateTimeFormatter FULL_DATETIME = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");

    public static String fmtDatumVrijeme(LocalDateTime dt) 
    {
        if (dt == null) return "-";
        return dt.format(FULL_DATETIME);
    }
    
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
    
    public static String fmtBroj(long n)
    {
        DecimalFormatSymbols decimalniFormat = DecimalFormatSymbols.getInstance(Locale.ROOT);
        decimalniFormat.setDecimalSeparator('.');
        decimalniFormat.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,##0", decimalniFormat);
        df.setGroupingUsed(true);
        df.setGroupingSize(3);
        return df.format(n);
    }

    public static String fmtCijena(float c)
    {
        DecimalFormatSymbols decimalniFormat = DecimalFormatSymbols.getInstance(Locale.ROOT);
        decimalniFormat.setDecimalSeparator('.');
        decimalniFormat.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,##0.00", decimalniFormat);
        df.setGroupingUsed(true);
        df.setGroupingSize(3);
        return df.format(c);
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
}
