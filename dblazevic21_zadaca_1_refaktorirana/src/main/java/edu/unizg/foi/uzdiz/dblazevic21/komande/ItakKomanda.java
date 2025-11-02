package edu.unizg.foi.uzdiz.dblazevic21.komande;

import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItakKomanda implements Komanda 
{

    private final Map<Integer, Aranzmani> aranzmani;

    private final DateTimeFormatter datumIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private final DateTimeFormatter vrijemeIspis = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final DateTimeFormatter inputDate = new DateTimeFormatterBuilder()
            .parseLenient()
            .appendPattern("d.M.yyyy")
            .optionalStart().appendLiteral('.').optionalEnd()
            .toFormatter(Locale.ROOT);

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
        String trimmed = (unos == null) ? "" : unos.trim();

        if (trimmed.equalsIgnoreCase("ITAK"))
        {
            ispisiAranzmane(null, null);
            return;
        }

        Pattern p = Pattern.compile("^ITAK\\s+(.+)\\s+(.+)$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(trimmed);
        
        if (m.matches()) 
        {
            String odStr = normalizirajDatum(m.group(1));
            String doStr = normalizirajDatum(m.group(2));

            LocalDate od = parseDatumZaKomandu(odStr);
            LocalDate dO = parseDatumZaKomandu(doStr);

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

        printajSeperator(w);
        printajRed(w,
                "Oznaka", "Naziv", "Početni datum", "Završni datum",
                "Kretanje", "Povratak", "Cijena", "Min br. putnika", "Maks br. putnika"
        );
        printajSeperator(w);

        for (Aranzmani a : lista) 
        {
            if (a == null) 
            {
            	continue;
            }
            printajRed(w,
                    String.valueOf(a.getOznaka()),
                    izrezi(a.getNaziv(), 35),
                    fmtDatum(a.getPocetniDatum()),
                    fmtDatum(a.getZavrsniDatum()),
                    fmtVrijeme(a.getVrijemeKretanja()),
                    fmtVrijeme(a.getVrijemePovratka()),
                    fmtCijena(a.getCijena()),
                    String.valueOf(a.getMinBrojPutnika()),
                    String.valueOf(a.getMaksBrojPutnika()));
        }
        printajSeperator(w);
    }

    private String fmtDatum(LocalDate d) 
    {
    	return (d == null) ? "-" : d.format(datumIspis); 
    }
    
    private String fmtVrijeme(java.time.LocalTime t) 
    { 
    	return (t == null) ? "-" : t.format(vrijemeIspis); 
    }
    
    private String fmtCijena(float c) 
    { 
    	return String.format(Locale.ROOT, "%.2f", c); 
    }

    private String normalizirajDatum(String s) 
    {
        String x = s.trim().replaceAll("\\.+", ".");
        if (!x.endsWith("."))
        {
        	x += ".";
        }
        return x;
    }

    private LocalDate parseDatumZaKomandu(String s) {
        try
        {
            String t = (s == null) ? "" : s.trim();
            
            if (t.endsWith("."))
            {
            	t = t.substring(0, t.length() - 1);
            }
            return LocalDate.parse(t, inputDate);
        } 
        catch (Exception ignored) 
        {
            return null;
        }
    }

    private String izrezi(String s, int max)
    {
        if (s == null)
    	{
    		return "-";
    	}
        return s.length() <= max ? s : s.substring(0, Math.max(0, max - 1)) + "…";
    }

    private void printajSeperator(int[] widths)
    {
        StringBuilder sb = new StringBuilder();
        sb.append('+');
        
        for (int w : widths)
        {
            for (int i = 0; i < w + 2; i++) 
            {
                sb.append('-');
            }
            sb.append('+');
        }
        System.out.println(sb.toString());
    }

    private void printajRed(int[] widths, String... cells) 
    {
        StringBuilder fmt = new StringBuilder();
        fmt.append('|');
        for (int w : widths) 
        {
        	fmt.append(" %-" + w + "s |");
        }
        fmt.append("%n");
        Object[] out = new Object[widths.length];
        for (int i = 0; i < widths.length; i++) 
        {
            String v = (i < cells.length) ? cells[i] : "-";
            out[i] = (v == null || v.isBlank()) ? "-" : v;
        }
        System.out.printf(fmt.toString(), out);
    }
}