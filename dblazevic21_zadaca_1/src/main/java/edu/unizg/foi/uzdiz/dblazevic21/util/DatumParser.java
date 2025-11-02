package edu.unizg.foi.uzdiz.dblazevic21.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DatumParser
{

    public static LocalDateTime parseDatumIVrijeme(String datum, String vrijeme)
    {
        try
        {
            if (datum.endsWith(".")) 
            {
                datum = datum.substring(0, datum.length() - 1);
            }

            DateTimeFormatter df = new DateTimeFormatterBuilder()
                    .parseLenient()
                    .appendPattern("[d.M.yyyy][dd.MM.yyyy][d/M/yyyy][dd/MM/yyyy]")
                    .toFormatter();

            LocalDate d = LocalDate.parse(datum.replace("/", ".").trim(), df);

            if (!vrijeme.matches("\\d{1,2}:\\d{1,2}:\\d{1,2}")) 
            {
                vrijeme += ":00";
            }

            DateTimeFormatter tf = new DateTimeFormatterBuilder()
                    .parseLenient()
                    .appendPattern("[H:mm:ss][HH:mm:ss][H:mm][HH:mm]")
                    .toFormatter();

            LocalTime t = LocalTime.parse(vrijeme.trim(), tf);

            return LocalDateTime.of(d, t);
        } 
        catch (Exception e) 
        {
            return null;
        }
    }
}
