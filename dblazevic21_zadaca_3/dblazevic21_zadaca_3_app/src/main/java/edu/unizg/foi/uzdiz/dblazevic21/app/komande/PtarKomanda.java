package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.observer.PretplatnikObserver;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.GramatikaIJezikApp;

public class PtarKomanda implements Komanda
{

    private final Map<Integer, Aranzmani> aranzmani;

    public PtarKomanda(Map<Integer, Aranzmani> aranzmani) 
    {
        this.aranzmani = aranzmani;
    }

    @Override
    public String getNaziv()
    {
        return "PTAR";
    }

    @Override
    public void izvrsi(String unos) 
    {
    	String s = (unos == null) ? "" : unos.trim();
        String[] d = s.split("\\s+");

        if (d.length != 4)
        {
            System.out.println("Greška: Očekivano: PTAR ime prezime oznaka");
            return;
        }

        TablicaPrinter.ispisUnosa(unos);

        String ime = GramatikaIJezikApp.velikoPocetnoSlovo(d[1]);
        String prezime = GramatikaIJezikApp.velikoPocetnoSlovo(d[2]);

        int oznaka;
        try
        {
            oznaka = Integer.parseInt(d[3]);
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

        PretplatnikObserver obs = new PretplatnikObserver(ime, prezime, oznaka);
        a.attachObserver(obs);

        System.out.println("Osoba " + GramatikaIJezikApp.velikoPocetnoSlovo(ime) + " " + GramatikaIJezikApp.velikoPocetnoSlovo(prezime) + " pretplaćena na obavijesti o aranžmanu " + oznaka + ".");
    }
}
