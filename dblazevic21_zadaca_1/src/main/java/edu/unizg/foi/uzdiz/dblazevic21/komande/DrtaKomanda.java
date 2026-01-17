package edu.unizg.foi.uzdiz.dblazevic21.komande;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

import edu.unizg.foi.uzdiz.dblazevic21.util.DatumParser;
import edu.unizg.foi.uzdiz.dblazevic21.util.GramatikaIJezik;
import edu.unizg.foi.uzdiz.dblazevic21.ispis.FormaterZaIspise;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacija;

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

        String ime = GramatikaIJezik.velikoPocetnoSlovo(m.group(1).trim());
        String prezime = GramatikaIJezik.velikoPocetnoSlovo(m.group(2).trim());
        int oznaka = Integer.parseInt(m.group(3).trim());
        String datum = m.group(4).trim();
        String vrijeme = m.group(5).trim();

        LocalDateTime datumVrijeme = DatumParser.normalizirajDatumIVrijeme(datum, vrijeme);
        
        if (datumVrijeme == null) 
        {
            System.out.println("Greška: neispravan format datuma i vremena. Očekivano: dd.MM.yyyy. HH:mm:ss");
            return;
        }

        Aranzmani a = aranzmani.get(oznaka);
        if (a == null) 
        {
            System.out.println("Aranžman s oznakom " + oznaka + " ne postoji.");
            return;
        }

        provjeriIDodajRezervaciju(ime, prezime, oznaka, datumVrijeme);
    }

	public void provjeriIDodajRezervaciju(String ime, String prezime, int oznaka, LocalDateTime datumVrijeme) 
	{
		List<Rezervacija> postojeceRezervacije = Rezervacije.getInstance().getZaOsobu(ime,  prezime);
        
        for (Rezervacija r : postojeceRezervacije)
        {
        	if (r.getOznakaAranzmana() == oznaka)
        	{
        		System.out.println("Osoba " + ime + " " + prezime + " već ima rezervaciju za aranžman " + oznaka + ".");
				return;
        	}
        	
        	if (r.getDatumVrijeme().equals(datumVrijeme))
        	{
        		System.out.println("Osoba " + ime + " " + prezime + " već ima rezervaciju u terminu " + FormaterZaIspise.fmtDatumVrijeme(datumVrijeme, r.getDatumVrijemeRaw()) + ".");
				return;
        	}
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
}
