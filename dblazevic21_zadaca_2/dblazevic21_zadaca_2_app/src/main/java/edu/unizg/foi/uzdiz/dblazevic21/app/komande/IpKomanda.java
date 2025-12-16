package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.IspisKonfiguracija;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;

public class IpKomanda implements Komanda
{
    @Override
    public String getNaziv()
    {
        return "IP";
    }

    @Override
    public void izvrsi(String unos)
    {
        String odrezan = (unos == null) ? "" : unos.trim();

        Pattern p = Pattern.compile("^IP(?:\\s+([NS]))?$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher m = p.matcher(odrezan);

        if (!m.matches())
        {
            System.out.println("Nepoznata komanda. Upotrijebite: IP [N|S]");
            return;
        }

        String arg = m.group(1);
        char odabir = (arg == null) ? 'N' : Character.toUpperCase(arg.charAt(0));

        TablicaPrinter.ispisUnosa(unos);
        
        switch (odabir)
        {
            case 'N' -> {
                IspisKonfiguracija.setNacin(IspisKonfiguracija.NacinSortiranja.N);
                System.out.println("Način ispisa postavljen na N (kronološki redoslijed: prvo stari, zatim novi).");
            }
            case 'S' -> {
                IspisKonfiguracija.setNacin(IspisKonfiguracija.NacinSortiranja.S);
                System.out.println("Način ispisa postavljen na S (obrnuti kronološki redoslijed: prvo novi, zatim stari).");
            }
            default -> {
                System.out.println("Greška: dozvoljene vrijednosti su samo N ili S.");
            }
        }
    }
}
