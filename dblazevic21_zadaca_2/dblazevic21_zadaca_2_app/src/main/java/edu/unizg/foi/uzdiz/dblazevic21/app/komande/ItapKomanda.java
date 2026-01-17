package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.FormaterZaIspise;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.StatusFormater;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class ItapKomanda implements Komanda
{

    private final Map<Integer, Aranzmani> aranzmani;

    public ItapKomanda(Map<Integer, Aranzmani> aranzmani)
    {
        this.aranzmani = aranzmani;
    }

    @Override
    public String getNaziv()
    {
        return "ITAP";
    }

    @Override
    public void izvrsi(String unos)
    {
        String izrezan = (unos == null) ? "" : unos.trim();

        if (izrezan.equalsIgnoreCase("ITAP"))
        {
	    	System.out.println("Nepoznata komanda. Upotrijebite: ITAP oznaka");
	        return;
        }

        Pattern uzorakOznake = Pattern.compile("^ITAP\\s+(\\d+)$", Pattern.CASE_INSENSITIVE);
        Matcher pogodeniOznaka = uzorakOznake.matcher(izrezan);
        if (pogodeniOznaka.matches())
        {
            int oznaka = Integer.parseInt(pogodeniOznaka.group(1));
            TablicaPrinter.ispisUnosa(unos);
            ispisiAranzmanDetaljno(oznaka);
            return;
        }
    }

    private void ispisiAranzmanDetaljno(int oznaka)
    {
        Aranzmani a = aranzmani.get(oznaka);
        if (a == null)
        {
            System.out.println("Aranžman s oznakom " + oznaka + " ne postoji.");
            return;
        }

        final int w1 = 28;
        final int w2 = 90;

        System.out.println("Detalji turističkog aranžmana");
        System.out.println();
        TablicaPrinter.printajSeperatorTablice(w1, w2);
        TablicaPrinter.printajRedTablice("Polje", "Vrijednost", w1, w2);
        TablicaPrinter.printajSeperatorTablice(w1, w2);

        TablicaPrinter.printajRedTablice("Oznaka", String.valueOf(a.getOznaka()), w1, w2);
        TablicaPrinter.printajRedTablice("Naziv", FormaterZaIspise.val(a.getNaziv()), w1, w2);
        TablicaPrinter.printajRedTablice("Program", FormaterZaIspise.val(a.getProgram()), w1, w2);
        TablicaPrinter.printajRedTablice("Početni datum", FormaterZaIspise.fmtDatum(a.getPocetniDatum()), w1, w2);
        TablicaPrinter.printajRedTablice("Završni datum", FormaterZaIspise.fmtDatum(a.getZavrsniDatum()), w1, w2);
        TablicaPrinter.printajRedTablice("Vrijeme kretanja", FormaterZaIspise.fmtVrijeme(a.getVrijemeKretanja()), w1, w2);
        TablicaPrinter.printajRedTablice("Vrijeme povratka", FormaterZaIspise.fmtVrijeme(a.getVrijemePovratka()), w1, w2);
        TablicaPrinter.printajRedTablice("Cijena", FormaterZaIspise.fmtCijena(a.getCijena()), w1, w2);
        TablicaPrinter.printajRedTablice("Min broj putnika", String.valueOf(a.getMinBrojPutnika()), w1, w2);
        TablicaPrinter.printajRedTablice("Maks broj putnika", String.valueOf(a.getMaksBrojPutnika()), w1, w2);
        TablicaPrinter.printajRedTablice("Broj noćenja", String.valueOf(a.getBrojNocenja()), w1, w2);
        TablicaPrinter.printajRedTablice("Doplata jednokrevetne", FormaterZaIspise.fmtCijena(a.getDoplataSobe()), w1, w2);
        TablicaPrinter.printajRedTablice("Prijevoz", FormaterZaIspise.val(a.getPrijevoz()), w1, w2);
        TablicaPrinter.printajRedTablice("Broj doručka", String.valueOf(a.getBrojDorucka()), w1, w2);
        TablicaPrinter.printajRedTablice("Broj ručkova", String.valueOf(a.getBrojRucka()), w1, w2);
        TablicaPrinter.printajRedTablice("Broj večera", String.valueOf(a.getBrojVecera()), w1, w2);
        TablicaPrinter.printajRedTablice("Status", StatusFormater.statusOznakaAranzmana(a.getStatus()), w1, w2);

        TablicaPrinter.printajSeperatorTablice(w1, w2);
    }
}
