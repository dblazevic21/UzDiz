package edu.unizg.foi.uzdiz.dblazevic21.komande;

import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacije;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DrtaKomanda implements Komanda 
{

    private final Map<Integer, Aranzmani> aranzmani;

    public DrtaKomanda(Map<Integer, Aranzmani> aranzmani) 
    {
        this.aranzmani = aranzmani;
    }

    @Override
    public String getNaziv() 
    {
        return "DRTA";
    }

    @Override
    public void izvrsi(String unos) 
    {
        String odrezano = (unos == null) ? "" : unos.trim();

        Pattern p = Pattern.compile(
                "^DRTA\\s+([\\p{L}\\p{M}]+)\\s+([\\p{L}\\p{M}]+)\\s+(\\d+)\\s+(\\d{1,2}[\\.\\/]\\d{1,2}[\\.\\/]\\d{4}\\.)\\s+(\\d{1,2}:\\d{1,2}(?::\\d{1,2})?)$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS
        );

        Matcher m = p.matcher(odrezano);

        if (!m.matches())
        {
            System.out.println("Nepoznata komanda. Upotrijebite: DRTA ime prezime oznaka datum vrijeme");
            return;
        }

        String ime = m.group(1).trim();
        String prezime = m.group(2).trim();
        int oznaka = Integer.parseInt(m.group(3).trim());
        String datum = m.group(4).trim();
        String vrijeme = m.group(5).trim();

        LocalDateTime datumVrijeme = normalizirajDatumIVrijeme(datum, vrijeme);
        if (datumVrijeme == null) 
        {
            System.out.println("Greška: neispravan format datuma i vremena. Očekivano: dd.MM.yyyy HH:mm:ss");
            return;
        }

        Aranzmani a = aranzmani.get(oznaka);
        if (a == null) 
        {
            System.out.println("Aranžman s oznakom " + oznaka + " ne postoji.");
            return;
        }

        try 
        {
            Rezervacije.getInstance().dodajRezervaciju(ime, prezime, oznaka, datumVrijeme);
            Rezervacije.getInstance().azurirajStatuseRezervacija(aranzmani);
            System.out.println("Rezervacija za osobu " + ime + " " + prezime + " na aranžman " + oznaka + " je dodana.");
        } 
        catch (Exception e)
        {
            System.out.println("Greška prilikom dodavanja rezervacije: " + e.getMessage());
        }
    }

    private LocalDateTime normalizirajDatumIVrijeme(String datum, String vrijeme) 
    {
        try 
        {
            if (datum.endsWith(".")) 
            {
                datum = datum.substring(0, datum.length() - 1);
            }

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
}
