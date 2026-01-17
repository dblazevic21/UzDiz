package edu.unizg.foi.uzdiz.dblazevic21.komande;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.unizg.foi.uzdiz.dblazevic21.ispis.FormaterZaIspise;
import edu.unizg.foi.uzdiz.dblazevic21.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;

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
            ispisiAranzmanDetaljno(oznaka);
            return;
        }

        Pattern uzorakCijene = Pattern.compile("^ITAP\\s+([0-9]+[\\.,]?[0-9]*)\\s+([0-9]+[\\.,]?[0-9]*)$",
                Pattern.CASE_INSENSITIVE);
        Matcher pogodeniCijene = uzorakCijene.matcher(izrezan);
        if (pogodeniCijene.matches())
        {
            Float min = FormaterZaIspise.parseCijenu(pogodeniCijene.group(1));
            Float max = FormaterZaIspise.parseCijenu(pogodeniCijene.group(2));

            if (min == null || max == null)
            {
                System.out.println("Neispravna cijena.");
                return;
            }
            if (max < min)
            {
                System.out.println("Greška: maksimalna cijena manja od minimalne.");
                return;
            }
            ispisiAranzmanePoCijeni(min, max);
            return;
        }

        System.out.println("Nepoznata komanda. Upotrijebite: ITAP [min max] ili ITAP [oznaka]");
    }

    private void ispisiAranzmanePoCijeni(Float min, Float max)
    {
        List<Aranzmani> lista = new ArrayList<>(aranzmani.values());
        lista.sort(Comparator
                .comparing(Aranzmani::getCijena)
                .thenComparing(Aranzmani::getOznaka));

        if (min != null && max != null)
        {
            lista.removeIf(a -> a == null || a.getCijena() < min || a.getCijena() > max);
        }

        if (lista.isEmpty())
        {
            System.out.println("Nema aranžmana u zadanom rasponu cijena.");
            return;
        }

        int[] w = {7, 35, 14, 14, 12, 16, 10, 18, 18};

        TablicaPrinter.printajSeperatorTabliceMulti(w);
        TablicaPrinter.printajRedTabliceMulti(w,
                "Oznaka", "Naziv", "Početni datum", "Završni datum",
                "Kretanje", "Povratak", "Cijena", "Min br. putnika", "Maks br. putnika"
        );
        TablicaPrinter.printajSeperatorTabliceMulti(w);

        for (Aranzmani a : lista)
        {
            TablicaPrinter.printajRedTabliceMulti(w,
                    String.valueOf(a.getOznaka()),
                    FormaterZaIspise.izrezi(a.getNaziv(), 35),
                    FormaterZaIspise.fmtDatum(a.getPocetniDatum()),
                    FormaterZaIspise.fmtDatum(a.getZavrsniDatum()),
                    FormaterZaIspise.fmtVrijeme(a.getVrijemeKretanja()),
                    FormaterZaIspise.fmtVrijeme(a.getVrijemePovratka()),
                    FormaterZaIspise.fmtCijena(a.getCijena()),
                    String.valueOf(a.getMinBrojPutnika()),
                    String.valueOf(a.getMaksBrojPutnika()));
        }

        TablicaPrinter.printajSeperatorTabliceMulti(w);
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

        TablicaPrinter.printajSeperatorTablice(w1, w2);
    }
}
