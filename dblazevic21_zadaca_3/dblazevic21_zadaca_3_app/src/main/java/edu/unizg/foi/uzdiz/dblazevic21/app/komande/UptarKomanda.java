package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.observer.PretplatnikObserver;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.GramatikaIJezikApp;

public class UptarKomanda implements Komanda 
{

    private final Map<Integer, Aranzmani> aranzmani;

    public UptarKomanda(Map<Integer, Aranzmani> aranzmani) 
    {
        this.aranzmani = aranzmani;
    }

    @Override
    public String getNaziv()
    {
        return "UPTAR";
    }

    @Override
    public void izvrsi(String unos)
    {
        String s = (unos == null) ? "" : unos.trim();
        String[] d = s.split("\\s+");

        if (d.length != 2 && d.length != 4)
        {
            System.out.println("Greška: Očekivano: UPTAR oznaka  ILI  UPTAR ime prezime oznaka");
            return;
        }

        TablicaPrinter.ispisUnosa(unos);

        if (d.length == 2)
        {
            ukiniSvePretplateZaAranzman(d[1]);
            return;
        }

        ukiniPretplatuOsobe(d[1], d[2], d[3]);
    }

    private void ukiniSvePretplateZaAranzman(String oznakaRaw)
    {
        int oznaka;
        try
        {
            oznaka = Integer.parseInt(oznakaRaw);
        }
        catch (Exception e)
        {
            System.out.println("Greška: oznaka mora biti broj.");
            return;
        }

        Aranzmani a = aranzmani.get(oznaka);
        if (a == null)
        {
            System.out.println("Greška: ne postoji aranžman s oznakom " + oznaka + ".");
            return;
        }

        a.detachAllObservers();
        System.out.println("Ukidaju se sve pretplate za aranžman " + oznaka + ".");
    }

    private void ukiniPretplatuOsobe(String ime, String prezime, String oznakaRaw)
    {
        int oznaka;
        try
        {
            oznaka = Integer.parseInt(oznakaRaw);
        }
        catch (Exception e)
        {
            System.out.println("Greška: oznaka mora biti broj.");
            return;
        }

        Aranzmani a = aranzmani.get(oznaka);
        if (a == null)
        {
            System.out.println("Greška: ne postoji aranžman s oznakom " + oznaka + ".");
            return;
        }

        PretplatnikObserver osoba = new PretplatnikObserver(ime, prezime, oznaka);
        a.detachObserver(osoba);

        System.out.println("Ukida se pretplata osobe "
                + GramatikaIJezikApp.velikoPocetnoSlovo(ime) + " "
                + GramatikaIJezikApp.velikoPocetnoSlovo(prezime)
                + " za aranžman " + oznaka + ".");
    }
}
