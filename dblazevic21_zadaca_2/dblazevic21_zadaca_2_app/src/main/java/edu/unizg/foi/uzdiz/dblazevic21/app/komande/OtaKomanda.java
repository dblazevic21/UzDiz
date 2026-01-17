package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OtkazanaConcreteState;

public class OtaKomanda implements Komanda
{
    private final Map<Integer, Aranzmani> aranzmani;

    public OtaKomanda(Map<Integer, Aranzmani> aranzmani)
    {
        this.aranzmani = aranzmani;
    }

    @Override
    public String getNaziv()
    {
        return "OTA";
    }

    @Override
    public void izvrsi(String unos)
    {
        String trimmed = (unos == null) ? "" : unos.trim();

        Pattern p = Pattern.compile("^OTA\\s+(\\d+)$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher m = p.matcher(trimmed);

        if (!m.matches())
        {
            System.out.println("Nepoznata komanda. Upotrijebite: OTA oznakaAranžmana");
            return;
        }

        int oznaka = Integer.parseInt(m.group(1));
        Aranzmani aranzman = aranzmani.get(oznaka);
        if (aranzman == null)
        {
        	TablicaPrinter.ispisUnosa(unos);
            System.out.println("Aranžman s oznakom " + oznaka + " ne postoji.");
            return;
        }
        
        List<Rezervacija> rezervacije = Rezervacije.getInstance().getZaAranzman(oznaka, aranzmani);
        if (rezervacije.isEmpty())
        {
            aranzman.otkaziAranzman();
            TablicaPrinter.ispisUnosa(unos);
            System.out.println("Aranžman " + oznaka + " je otkazan (nije bilo rezervacija).");
            return;
        }

        LocalDateTime sada = LocalDateTime.now();
        int brojac = 0;

        brojac = otkazivanjeAranzmana(aranzman, rezervacije, sada, brojac);

        System.out.println("Uspješno otkazan aranžman " + oznaka + ". Otkazano rezervacija: " + brojac + ".");
    }

	public int otkazivanjeAranzmana(Aranzmani aranzman, List<Rezervacija> rezervacije, LocalDateTime sada, int brojac) 
	{
		for (Rezervacija r : rezervacije)
        {
            r.setStatus(new OtkazanaConcreteState());
            try
            {
                r.setOtkazanoAt(sada);
            }
            catch (Exception e)
            {
            	
            }
            brojac++;
        }

        Rezervacije.getInstance().azurirajStatuseRezervacija(aranzmani);

        aranzman.otkaziAranzman();
		return brojac;
	}
}
