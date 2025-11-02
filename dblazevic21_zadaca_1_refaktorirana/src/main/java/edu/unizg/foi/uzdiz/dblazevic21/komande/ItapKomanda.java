package edu.unizg.foi.uzdiz.dblazevic21.komande;

import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItapKomanda implements Komanda 
{

    private final Map<Integer, Aranzmani> aranzmani;

    private final DateTimeFormatter datumIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private final DateTimeFormatter vrijemeIspis = DateTimeFormatter.ofPattern("HH:mm:ss");

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
            ispisiAranzmanePoCijeni(null, null);
            return;
        }

        Pattern uzorak = Pattern.compile("^ITAP\\s+(\\d+)$", Pattern.CASE_INSENSITIVE);
        Matcher pogodeniUzorak = uzorak.matcher(izrezan);
        if (pogodeniUzorak.matches())
        {
            int oznaka = Integer.parseInt(pogodeniUzorak.group(1));
            ispisiAranzmanDetaljno(oznaka);
            return;
        }

        Pattern ulazUzorak = Pattern.compile("^ITAP\\s+([0-9]+[\\.,]?[0-9]*)\\s+([0-9]+[\\.,]?[0-9]*)$",
                Pattern.CASE_INSENSITIVE);
        Matcher ulazPogodeniUzorak = ulazUzorak.matcher(izrezan);
        if (ulazPogodeniUzorak.matches()) 
        {
            Float min = parseCijenu(ulazPogodeniUzorak.group(1));
            Float max = parseCijenu(ulazPogodeniUzorak.group(2));

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

        int[] w = {7, 35, 14, 14, 12, 16, 10, 18, 18};
        printajSeperator(w);
        printajRedMulti(w,
                "Oznaka", "Naziv", "Početni datum", "Završni datum",
                "Kretanje", "Povratak", "Cijena", "Min br. putnika", "Maks br. putnika"
        );
        printajSeperator(w);

        for (Aranzmani a : lista)
        {
            printajRedMulti(w,
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
        printajSeperatorTablice(w1, w2);
        printajRedTablice("Polje", "Vrijednost", w1, w2);
        printajSeperatorTablice(w1, w2);

        printajRedTablice("Oznaka", String.valueOf(a.getOznaka()), w1, w2);
        printajRedTablice("Naziv", val(a.getNaziv()), w1, w2);
        printajRedTablice("Program", val(a.getProgram()), w1, w2);
        printajRedTablice("Početni datum", fmtDatum(a.getPocetniDatum()), w1, w2);
        printajRedTablice("Završni datum", fmtDatum(a.getZavrsniDatum()), w1, w2);
        printajRedTablice("Vrijeme kretanja", fmtVrijeme(a.getVrijemeKretanja()), w1, w2);
        printajRedTablice("Vrijeme povratka", fmtVrijeme(a.getVrijemePovratka()), w1, w2);
        printajRedTablice("Cijena", fmtCijena(a.getCijena()), w1, w2);
        printajRedTablice("Min broj putnika", String.valueOf(a.getMinBrojPutnika()), w1, w2);
        printajRedTablice("Maks broj putnika", String.valueOf(a.getMaksBrojPutnika()), w1, w2);
        printajRedTablice("Broj noćenja", String.valueOf(a.getBrojNocenja()), w1, w2);
        printajRedTablice("Doplata jednokrevetne", fmtCijena(a.getDoplataSobe()), w1, w2);
        printajRedTablice("Prijevoz", val(a.getPrijevoz()), w1, w2);
        printajRedTablice("Broj doručka", String.valueOf(a.getBrojDorucka()), w1, w2);
        printajRedTablice("Broj ručkova", String.valueOf(a.getBrojRucka()), w1, w2);
        printajRedTablice("Broj večera", String.valueOf(a.getBrojVecera()), w1, w2);

        printajSeperatorTablice(w1, w2);
    }

    private Float parseCijenu(String s) 
    {
        try 
        {
            String t = (s == null) ? "" : s.trim().replace(',', '.');
            if (t.isEmpty()) 
        	{
        		return null;
        	}
            return Float.parseFloat(t);
        }
        catch (Exception e) 
        {
            return null;
        }
    }

    private String fmtDatum(LocalDate d) 
    { 
    	return (d == null) ? "-" : d.format(datumIspis); 
    }
    
    private String fmtVrijeme(LocalTime t) 
    { 
    	return (t == null) ? "-" : t.format(vrijemeIspis);
    }
    
    private String fmtCijena(float c) 
    { 
    	return String.format(Locale.ROOT, "%.2f", c); 
    }
    
    private String val(Object o) 
    {
    	return (o == null) ? "-" : String.valueOf(o); 
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
        		sb.append('-'); sb.append('+'); 
        	} 
        }
        System.out.println(sb.toString());
    }

    private void printajRedMulti(int[] widths, String... cells)
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

    private void printajSeperatorTablice(int w1, int w2) 
    {
        StringBuilder sb = new StringBuilder();
        sb.append('+');
        for (int i = 0; i < w1 + 2; i++) 
        {
        	sb.append('-'); 
        }
        sb.append('+');
        for (int i = 0; i < w2 + 2; i++) 
        {
        	sb.append('-'); 
        }
        sb.append('+');
        System.out.println(sb.toString());
    }

    private void printajRedTablice(String key, String value, int w1, int w2) 
    {
        String v = (value == null || value.isBlank()) ? "-" : value;
        v = v.replace("\\n", "\n");

        List<String> lines = wrappajTekst(v, w2);
        String firstKey = (key == null) ? "-" : key;

        for (int i = 0; i < lines.size(); i++)
        {
            String k = (i == 0) ? firstKey : "";
            
            System.out.printf("| %-" + w1 + "s | %-" + w2 + "s |%n", k, lines.get(i));
        }
    }

    private List<String> wrappajTekst(String text, int width) 
    {
        List<String> out = new ArrayList<>();
        if (text == null) 
        { 
        	out.add("-"); return out; 
        }

        String[] paragraphs = text.replace("\r", "").split("\n", -1);
        for (String p : paragraphs) 
        {
            String s = p.trim();
            if (s.isEmpty()) 
            {
            	out.add(""); continue; 
            }

            String[] words = s.split("\\s+");
            StringBuilder line = new StringBuilder();

            for (String w : words) 
            {
                if (line.length() == 0) 
                {
                    line.append(w);
                } 
                else if (line.length() + 1 + w.length() <= width) 
                {
                    line.append(' ').append(w);
                } 
                else 
                {
                    out.add(line.toString());
                    line.setLength(0);
                    line.append(w);
                }
            }
            if (line.length() > 0) 
            {
            	out.add(line.toString()); 
            }
        }
        if (out.isEmpty()) 
        {
        	out.add("-");
        }
        return out;
    }
}
