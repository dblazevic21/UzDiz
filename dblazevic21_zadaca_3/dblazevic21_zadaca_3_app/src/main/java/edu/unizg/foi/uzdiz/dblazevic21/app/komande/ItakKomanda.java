package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.FormaterZaIspise;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.IspisKonfiguracija;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.StatusFormater;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.DatumParserApp;

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
        	Rezervacije.getInstance().azurirajStatuseRezervacija(aranzmani);
        	
        	TablicaPrinter.ispisUnosa(unos);
            System.out.println("Popis turističkih aranžmana");
            System.out.println();
            ispisiAranzmane(null, null);
            return;
        }

        Pattern p = Pattern.compile("^ITAK\\s+(.+)\\s+(.+)$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(izrezano);
        
        if (m.matches()) 
        {
            String odStr = DatumParserApp.normalizirajDatum(m.group(1));
            String doStr = DatumParserApp.normalizirajDatum(m.group(2));

            LocalDate od = DatumParserApp.parseDatumZaKomandu(odStr);
            LocalDate dO = DatumParserApp.parseDatumZaKomandu(doStr);

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
            
            Rezervacije.getInstance().azurirajStatuseRezervacija(aranzmani);
            
            TablicaPrinter.ispisUnosa(unos);
            System.out.println("Popis turističkih aranžmana");
            System.out.println();
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
        
        if (IspisKonfiguracija.jeObrnutoKronoloski()) 
        {
            Collections.reverse(lista);
        }

        int[] w = {7, 35, 14, 14, 12, 16, 10, 18, 18, 18};
        boolean[] desno = {
                true,
                false,
                false,
                false,
                false,
                false,
                true, 
                true, 
                true,
                false
        };

        TablicaPrinter.printajSeperatorTabliceMulti(w);
        TablicaPrinter.printajRedTabliceMultiAlign(w, desno,
                "Oznaka", "Naziv", "Početni datum", "Završni datum",
                "Kretanje", "Povratak", "Cijena", "Min br. putnika", "Maks br. putnika", "Status"
        );
        TablicaPrinter.printajSeperatorTabliceMulti(w);

        for (Aranzmani a : lista)
        {
            if (a == null) 
        	{
        		continue;
        	}
            TablicaPrinter.printajRedTabliceMultiAlign(w, desno,
                    String.valueOf(a.getOznaka()),
                    FormaterZaIspise.izrezi(a.getNaziv(), 35),
                    FormaterZaIspise.fmtDatum(a.getPocetniDatum()),
                    FormaterZaIspise.fmtDatum(a.getZavrsniDatum()),
                    FormaterZaIspise.fmtVrijeme(a.getVrijemeKretanja()),
                    FormaterZaIspise.fmtVrijeme(a.getVrijemePovratka()),
                    FormaterZaIspise.fmtCijena(a.getCijena()),
                    String.valueOf(a.getMinBrojPutnika()),
                    String.valueOf(a.getMaksBrojPutnika()),
                    StatusFormater.statusOznakaAranzmana(a.getStatus())
                    );
        }
        TablicaPrinter.printajSeperatorTabliceMulti(w);
    }
}
