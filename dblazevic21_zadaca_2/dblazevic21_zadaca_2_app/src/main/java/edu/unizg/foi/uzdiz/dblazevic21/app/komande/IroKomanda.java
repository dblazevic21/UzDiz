package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.FormaterZaIspise;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.IspisKonfiguracija;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.StatusFormater;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacije;

public class IroKomanda implements Komanda 
{

    private final Map<Integer, Aranzmani> aranzmani;

    public IroKomanda(Map<Integer, Aranzmani> aranzmani) 
    {
        this.aranzmani = aranzmani;
    }

    @Override
    public String getNaziv() 
    {
        return "IRO";
    }

    @Override
    public void izvrsi(String unos) 
    {
        String izrezan = (unos == null) ? "" : unos.trim();
        Pattern p = Pattern.compile("^IRO\\s+(\\S+)\\s+(.+)$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher m = p.matcher(izrezan);
        if (!m.matches()) 
        {
            System.out.println("Nepoznata komanda. Upotrijebite: IRO ime prezime");
            return;
        }

        String ime = m.group(1).trim();
        String prezime = m.group(2).trim();

        List<Rezervacija> lista = Rezervacije.getInstance().getZaOsobu(ime, prezime, aranzmani);
        
        if (lista == null || lista.isEmpty()) 
        {
            System.out.println("Nema rezervacija za osobu " + ime + " " + prezime + ".");
            return;
        }
        
        if (IspisKonfiguracija.jeObrnutoKronoloski())
        {
            Collections.reverse(lista);
        }

        int[] sirine = {22, 22, 35, 17};
        
        boolean[] desno = {false, true, false, false};
        
        ispisPodataka(unos, ime, prezime, lista, sirine, desno);
    }

	public void ispisPodataka(String unos, String ime, String prezime, List<Rezervacija> lista, int[] sirine,
			boolean[] desno) 
	{
		TablicaPrinter.ispisUnosa(unos);
        System.out.println("Popis rezervacija za osobu " + ime + " " + prezime + ":");
        System.out.println();
        TablicaPrinter.printajSeperatorTabliceMulti(sirine);
        TablicaPrinter.printajRedTabliceMultiAlign(sirine, desno,
                "Datum i vrijeme", "Oznaka aranžmana", "Naziv aranžmana", "Vrsta");
        TablicaPrinter.printajSeperatorTabliceMulti(sirine);

        for (Rezervacija r : lista) 
        {
            Integer oznakaAranzmana = r.getOznakaAranzmana();
            Aranzmani a = aranzmani.get(oznakaAranzmana);
            String naziv = (a == null) ? "-" : FormaterZaIspise.val(a.getNaziv());
            String datumVrijeme = FormaterZaIspise.fmtDatumVrijeme(r.getDatumVrijeme(), r.getDatumVrijemeRaw());
            String vrsta = StatusFormater.statusOznaka(r.getStatus());
            TablicaPrinter.printajRedTabliceMultiAlign(sirine, desno,
                    datumVrijeme,
                    String.valueOf(oznakaAranzmana),
                    FormaterZaIspise.izrezi(naziv, sirine[2]),
                    vrsta);
        }

        TablicaPrinter.printajSeperatorTabliceMulti(sirine);
	}
}
