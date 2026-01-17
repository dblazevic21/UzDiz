package edu.unizg.foi.uzdiz.dblazevic21.app.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.GramatikaIJezikApp;

public class PretplatnikObserver implements TuristickiObserver
{

    private final String ime;
    private final String prezime;
    private final int oznakaAranzmana;

    public PretplatnikObserver(String ime, String prezime, int oznakaAranzmana)
{
        this.ime = (ime == null) ? "" : ime.trim();
        this.prezime = (prezime == null) ? "" : prezime.trim();
        this.oznakaAranzmana = oznakaAranzmana;
    }

    @Override
    public void update(int oznaka, String message)
    {
        if (oznaka != this.oznakaAranzmana) return;

        String imeFmt = GramatikaIJezikApp.velikoPocetnoSlovo(this.ime);
        String prezimeFmt = GramatikaIJezikApp.velikoPocetnoSlovo(this.prezime);

        List<String> linije = new ArrayList<>();
        if (message != null && !message.isBlank())
        {
            String[] split = message.split("\\R");
            for (String l : split)
            {
                if (l != null && !l.isBlank()) linije.add(l.trim());
            }
        }

        int[] sirine = new int[] { 62 };

        System.out.println();
        TablicaPrinter.printajSeperatorTabliceMulti(sirine);
        TablicaPrinter.printajRedTabliceMulti(sirine, "OBAVIJEST ZA: " + imeFmt + " " + prezimeFmt);
        TablicaPrinter.printajRedTabliceMulti(sirine, "ARANŽMAN: " + oznaka);
        TablicaPrinter.printajSeperatorTabliceMulti(sirine);

        if (linije.isEmpty())
        {
            TablicaPrinter.printajRedTabliceMulti(sirine, "-");
        }
        else
        {
            for (String l : linije)
            {
                TablicaPrinter.printajRedTabliceMulti(sirine, l);
            }
        }

        TablicaPrinter.printajSeperatorTabliceMulti(sirine);
        System.out.println();
    }

    @Override
    public boolean equals(Object o) 
    {
        if (this == o) return true;
        if (!(o instanceof PretplatnikObserver)) return false;
        PretplatnikObserver other = (PretplatnikObserver) o;
        return oznakaAranzmana == other.oznakaAranzmana
                && ime.equalsIgnoreCase(other.ime)
                && prezime.equalsIgnoreCase(other.prezime);
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(ime.toLowerCase(), prezime.toLowerCase(), oznakaAranzmana);
    }
}
