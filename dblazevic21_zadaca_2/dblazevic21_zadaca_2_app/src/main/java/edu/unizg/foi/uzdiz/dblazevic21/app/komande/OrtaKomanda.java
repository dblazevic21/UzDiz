package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacije;

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
        
        if (a.getStatus().getNaziv().equals("OTKAZAN"))
        {
        	TablicaPrinter.ispisUnosa(unos);
			System.out.println("Aranžman s oznakom " + oznaka + " je već otkazan. Nije moguće otkazati ponovno rezervaciju.");
			return;
        }

        boolean ok = Rezervacije.getInstance().otkaziRezervaciju(oznaka, ime, prezime, LocalDateTime.now(), aranzmani);

        if (ok)
        {
            Rezervacije.getInstance().azurirajStatuseRezervacija(aranzmani);
            TablicaPrinter.ispisUnosa(unos);
            System.out.println("Rezervacija za osobu " + ime + " " + prezime + " na aranžman " + oznaka + " je otkazana.");
        } 
        else 
        {
        	TablicaPrinter.ispisUnosa(unos);
            System.out.println("Ne postoji rezervacija za osobu " + ime + " " + prezime + " na aranžman " + oznaka + ".");
        }
    }
}
