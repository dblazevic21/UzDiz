package edu.unizg.foi.uzdiz.dblazevic21.komande;

import edu.unizg.foi.uzdiz.dblazevic21.enumeracije.StatusRezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacije;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IroKomanda implements Komanda
{

    private final Map<Integer, Aranzmani> aranzmani;
    private final DateTimeFormatter datumIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private final DateTimeFormatter vrijemeIspis = DateTimeFormatter.ofPattern("HH:mm:ss");

    public IroKomanda(Map<Integer, Aranzmani> aranzmani) 
    {
        this.aranzmani = aranzmani;
    }

    @Override
    public String getNaziv() 
    {
        return "IRO";
    }

    @Override
    public void izvrsi(String unos) 
    {
        String trimmed = (unos == null) ? "" : unos.trim();
        Pattern p = Pattern.compile("^IRO\\s+(\\S+)\\s+(.+)$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher m = p.matcher(trimmed);
        if (!m.matches()) 
        {
            System.out.println("Nepoznata komanda. Upotrijebite: IRO ime prezime");
            return;
        }

        String ime = m.group(1).trim();
        String prezime = m.group(2).trim();

        List<Rezervacija> lista = Rezervacije.getInstance().getZaOsobu(ime, prezime);

        if (lista == null || lista.isEmpty()) 
        {
            System.out.println("Nema rezervacija za osobu " + ime + " " + prezime + ".");
            return;
        }

        int[] w = {22, 22, 35, 17};
        printajSeperator(w);
        printajRed(w, "Datum i vrijeme", "Oznaka aranžmana", "Naziv aranžmana", "Vrsta");
        printajSeperator(w);

        for (Rezervacija r : lista)
        {
            Integer oznakaAranzmana = r.getOznakaAranzmana();
            Aranzmani a = aranzmani.get(oznakaAranzmana);
            String naziv = (a == null) ? "-" : val(a.getNaziv());
            String datumVrijeme = fmtDatumVrijeme(r.getDatumVrijeme(), r.getDatumVrijemeRaw());
            String vrsta = statusOznaka(r.getStatus());
            printajRed(w, datumVrijeme, String.valueOf(oznakaAranzmana), izrezi(naziv, w[2]), vrsta);
        }

        printajSeperator(w);
    }

    private String fmtDatumVrijeme(LocalDateTime dt, String raw) 
    {
        if (dt != null) 
        {
            return (dt.toLocalDate().format(datumIspis) + " " + dt.toLocalTime().format(vrijemeIspis)).trim();
        }
        if (raw != null && !raw.isBlank()) 
        {
        	return raw.trim();
        }
        return "-";
    }

    private String statusOznaka(StatusRezervacije s)
    {
        if (s == null) return "-";
        return switch (s) {
            case PRIMLJENA -> "PRIMLJENA";
            case AKTIVNA -> "AKTIVNA";
            case NA_CEKANJU -> "NA ČEKANJU";
            case OTKAZANA -> "OTKAZANA";
        };
    }

    private String val(Object o) 
    { 
    	return (o == null) ? "-" : String.valueOf(o); 
    }

    private String izrezi(String s, int max) 
    {
        if (s == null) return "-";
        
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