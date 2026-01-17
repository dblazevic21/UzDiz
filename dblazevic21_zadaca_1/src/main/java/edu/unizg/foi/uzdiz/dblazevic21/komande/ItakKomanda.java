package edu.unizg.foi.uzdiz.dblazevic21.komande;

import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.ispis.FormaterZaIspise;
import edu.unizg.foi.uzdiz.dblazevic21.util.DatumParser;
import edu.unizg.foi.uzdiz.dblazevic21.ispis.TablicaPrinter;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItakKomanda implements Komanda 
{

    private final Map<Integer, Aranzmani> aranzmani;


    public ItakKomanda(Map<Integer, Aranzmani> aranzmani) 
    {
        this.aranzmani = aranzmani;
    }

    @Override
    public String getNaziv() 
    {
        return "ITAK";
    }

    @Override
    public void izvrsi(String unos)
    {
        String izrezano = (unos == null) ? "" : unos.trim();

        if (izrezano.equalsIgnoreCase("ITAK"))
        {
            ispisiAranzmane(null, null);
            return;
        }

        Pattern p = Pattern.compile("^ITAK\\s+(.+)\\s+(.+)$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(izrezano);
        
        if (m.matches()) 
        {
            String odStr = DatumParser.normalizirajDatum(m.group(1));
            String doStr = DatumParser.normalizirajDatum(m.group(2));

            LocalDate od = DatumParser.parseDatumZaKomandu(odStr);
            LocalDate dO = DatumParser.parseDatumZaKomandu(doStr);

            if (od == null || dO == null)
            {
                System.out.println("Neispravan format datuma. Očekivano: dd.MM.yyyy.");
                return;
            }
            if (dO.isBefore(od)) 
            {
                System.out.println("Greška: završni datum je prije početnog.");
                return;
            }
            ispisiAranzmane(od, dO);
            return;
        }

        System.out.println("Nepoznata komanda. Upotrijebite: ITAK [od do]");
    }

    private void ispisiAranzmane(LocalDate od, LocalDate dO) 
    {
        List<Aranzmani> lista = new ArrayList<>(aranzmani.values());
        lista.sort(Comparator
                .comparing(Aranzmani::getPocetniDatum, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Aranzmani::getOznaka));

        if (od != null && dO != null)
        {
            lista.removeIf(a -> a == null || a.getPocetniDatum() == null
                    || a.getPocetniDatum().isBefore(od)
                    || a.getPocetniDatum().isAfter(dO));
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
            if (a == null) 
            {
                continue;
            }
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
}
