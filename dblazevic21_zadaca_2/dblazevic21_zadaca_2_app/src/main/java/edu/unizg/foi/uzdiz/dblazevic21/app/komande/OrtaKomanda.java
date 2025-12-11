package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class OrtaKomanda implements Komanda 
{
    private final Map<Integer, Aranzmani> aranzmani;

    public OrtaKomanda(Map<Integer, Aranzmani> aranzmani)
    {
        this.aranzmani = aranzmani;
    }

    @Override
    public String getNaziv()
    {
        return "ORTA";
    }

    @Override
    public void izvrsi(String unos) 
    {
        String odrezano = (unos == null) ? "" : unos.trim();

        Pattern p = Pattern.compile("^ORTA\\s+(\\S+)\\s+(.+)\\s+(\\d+)$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher m = p.matcher(odrezano);

        if (!m.matches()) 
        {
            System.out.println("Nepoznata komanda. Upotrijebite: ORTA ime prezime oznaka");
            return;
        }

        String ime = m.group(1).trim();
        String prezime = m.group(2).trim();
        int oznaka = Integer.parseInt(m.group(3).trim());

        Aranzmani a = aranzmani.get(oznaka);
        if (a == null) 
        {
            System.out.println("Aranžman s oznakom " + oznaka + " ne postoji.");
            return;
        }

        boolean ok = a.ukloniRezervaciju(ime, prezime);

        if (ok)
        {
            System.out.println("Rezervacija za osobu " + ime + " " + prezime + 
                " na aranžman " + oznaka + " je otkazana.");
        } 
        else 
        {
            System.out.println("Ne postoji rezervacija za osobu " + ime + " " + prezime + 
                " na aranžman " + oznaka + ".");
        }
    }
}
