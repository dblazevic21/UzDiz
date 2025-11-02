package edu.unizg.foi.uzdiz.dblazevic21.komande;

import edu.unizg.foi.uzdiz.dblazevic21.turistickaAgencija.TuristickaAgencija;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.util.DatumParser;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.format.DateTimeFormatter;

public class DrtaKomanda implements Komanda
{
	@Override
	public String getNaziv() 
	{
		return "DRTA";
	}
	
	@Override
	public void izvrsi(String unos, TuristickaAgencija agencija)
	{
//		Pattern p = Pattern.compile(
//            "^DRTA\\s+(\\S+)\\s+(\\S+)\\s+(\\d+)\\s+(\\d{1,2}[./]\\d{1,2}[./]\\d{4})\\.?\\s+(\\d{1,2}:\\d{1,2}(?::\\d{1,2})?)$",
//            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
//        );
//        Matcher m = p.matcher(unos.trim());
//        if (!m.matches())
//        {
//            System.out.println("Nepoznata komanda. Upotrijebite: DRTA ime prezime oznaka datum vrijeme");
//            return;
//        }
//
//        String ime = m.group(1);
//        String prezime = m.group(2);
//        int oznaka = Integer.parseInt(m.group(3));
//        String datum = m.group(4);
//        String vrijeme = m.group(5);
//
//        LocalDateTime datumVrijeme = DatumParser.parseDatumIVrijeme(datum, vrijeme);
//        if (datumVrijeme == null) 
//        {
//            System.out.println("Greška: neispravan format datuma i vremena.");
//            return;
//        }
//
//        Aranzmani a = agencija.getAranzman(oznaka);
//        if (a == null) 
//        {
//            System.out.println("Aranžman s oznakom " + oznaka + " ne postoji.");
//            return;
//        }
//
//        Rezervacije.getInstance().dodajRezervaciju(ime, prezime, oznaka, datumVrijeme);
//        Rezervacije.getInstance().azurirajStatuseRezervacija(agencija.getAranzmani());
//        System.out.println("Rezervacija dodana.");
    }
}
