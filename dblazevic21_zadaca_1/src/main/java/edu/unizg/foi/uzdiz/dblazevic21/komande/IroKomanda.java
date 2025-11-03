package edu.unizg.foi.uzdiz.dblazevic21.komande;

import edu.unizg.foi.uzdiz.dblazevic21.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.ispis.FormaterZaIspise;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.ispis.StatusFormater;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        List<Rezervacija> lista = Rezervacije.getInstance().getZaOsobu(ime, prezime);

        if (lista == null || lista.isEmpty()) 
        {
            System.out.println("Nema rezervacija za osobu " + ime + " " + prezime + ".");
            return;
        }

        int[] sirine = {22, 22, 35, 17};

        TablicaPrinter.printajSeperatorTabliceMulti(sirine);
        TablicaPrinter.printajRedTabliceMulti(sirine,
                "Datum i vrijeme", "Oznaka aranžmana", "Naziv aranžmana", "Vrsta");
        TablicaPrinter.printajSeperatorTabliceMulti(sirine);

        for (Rezervacija r : lista) 
        {
            Integer oznakaAranzmana = r.getOznakaAranzmana();
            Aranzmani a = aranzmani.get(oznakaAranzmana);
            String naziv = (a == null) ? "-" : FormaterZaIspise.val(a.getNaziv());
            String datumVrijeme = FormaterZaIspise.fmtDatumVrijeme(r.getDatumVrijeme(), r.getDatumVrijemeRaw());
            String vrsta = StatusFormater.statusOznaka(r.getStatus());
            TablicaPrinter.printajRedTabliceMulti(sirine,
                    datumVrijeme,
                    String.valueOf(oznakaAranzmana),
                    FormaterZaIspise.izrezi(naziv, sirine[2]),
                    vrsta);
        }

        TablicaPrinter.printajSeperatorTabliceMulti(sirine);
    }
}
