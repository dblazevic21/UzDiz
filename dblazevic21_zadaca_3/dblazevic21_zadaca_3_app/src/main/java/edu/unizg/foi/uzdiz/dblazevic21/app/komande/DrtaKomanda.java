package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.DatumParserApp;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.GramatikaIJezikApp;

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
                "^DRTA\\s+" + "([\\p{L}\\p{M}]+)\\s+" + "([\\p{L}\\p{M}\\s'-]+)\\s+" + "(\\d+)\\s+" +
                "([\\d]{1,2}\\.[\\d]{1,2}\\.[\\d]{4})\\.?" + "\\s+" + "(\\d{1,2}:\\d{2}(?::\\d{2})?)$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
            );


        Matcher m = p.matcher(odrezano);

        if (!m.matches())
        {
            System.out.println("Nepoznata komanda. Upotrijebite: DRTA ime prezime oznaka datum vrijeme");
            return;
        }

        String ime = GramatikaIJezikApp.velikoPocetnoSlovo(m.group(1).trim());
        String prezime = GramatikaIJezikApp.velikoPocetnoSlovo(m.group(2).trim());
        int oznaka = Integer.parseInt(m.group(3).trim());
        String datum = m.group(4).trim();
        String vrijeme = m.group(5).trim();

        LocalDateTime datumVrijeme = DatumParserApp.normalizirajDatumIVrijeme(datum, vrijeme);
        
        if (datumVrijeme == null) 
        {
        	TablicaPrinter.ispisUnosa(unos);
            System.out.println("Greška: neispravan format datuma i vremena. Očekivano: dd.MM.yyyy. HH:mm:ss");
            return;
        }

        Aranzmani a = aranzmani.get(oznaka);
        if (a == null)
        {
        	TablicaPrinter.ispisUnosa(unos);
            System.out.println("Aranžman s oznakom " + oznaka + " ne postoji.");
            return;
        }

        if (a.getStatus().getNaziv().equals("OTKAZAN")) 
        {
        	TablicaPrinter.ispisUnosa(unos);
            System.out.println("Nije moguće dodati rezervaciju. Aranžman je OTKAZAN.");
            return;
        }
        
        TablicaPrinter.ispisUnosa(unos);
        provjeriIDodajRezervaciju(ime, prezime, oznaka, datumVrijeme);
    }

    public void provjeriIDodajRezervaciju(String ime, String prezime, int oznaka, LocalDateTime datumVrijeme) 
    {
        try 
        {
            boolean ok = Rezervacije.getInstance()
                    .dodajRezervaciju(ime, prezime, oznaka, datumVrijeme, aranzmani);
            if (!ok)
            {
                System.out.println("Rezervacija nije dodana (neispravan aranžman ili duplikat).");
                return;
            }

            
            Rezervacije.getInstance().azurirajStatuseRezervacija(aranzmani);

            System.out.println("Rezervacija za osobu " + ime + " " + prezime
                    + " na aranžman " + oznaka + " je dodana.");
        } 
        catch (Exception e)
        {
            System.out.println("Greška prilikom dodavanja rezervacije: " + e.getMessage());
        }
    }

}
