package edu.unizg.foi.uzdiz.dblazevic21.komande;

import edu.unizg.foi.uzdiz.dblazevic21.enumeracije.StatusRezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacije;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IrtaKomanda implements Komanda 
{

    private final Map<Integer, Aranzmani> aranzmani;
    private final DateTimeFormatter datumIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private final DateTimeFormatter vrijemeIspis = DateTimeFormatter.ofPattern("HH:mm:ss");

    public IrtaKomanda(Map<Integer, Aranzmani> aranzmani) 
    {
        this.aranzmani = aranzmani;
    }

    @Override
    public String getNaziv() 
    {
        return "IRTA";
    }

    @Override
    public void izvrsi(String unos) 
    {
        String trimmed = (unos == null) ? "" : unos.trim();
        Pattern p = Pattern.compile("^IRTA\\s+(\\d+)(?:\\s+([PAČO]+))?$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        
        Matcher m = p.matcher(trimmed);
        if (!m.matches())
        {
            System.out.println("Nepoznata komanda. Upotrijebite: IRTA [oznaka] [PA|Č|O|PAČO]");
            return;
        }

        int oznaka = Integer.parseInt(m.group(1));
        String flags = (m.group(2) == null) ? "PA" : m.group(2).toUpperCase(Locale.ROOT);

        if (!flags.matches("[PAČO]+")) 
        {
            System.out.println("Greška: neispravni filteri. Dozvoljeni su samo PA, Č, O.");
            return;
        }

        Aranzmani a = aranzmani.get(oznaka);
        if (a == null)
        {
            System.out.println("Aranžman s oznakom " + oznaka + " ne postoji.");
            return;
        }

        List<Rezervacija> ulaz = Rezervacije.getInstance().getZaAranzman(oznaka);
        if (ulaz.isEmpty()) 
        {
            System.out.println("Nema rezervacija za aranžman " + oznaka + ".");
            return;
        }

        Rezervacije.getInstance().azurirajStatuseRezervacija(aranzmani);

        boolean dodajPA = flags.contains("PA");
        boolean dodajČekaj = flags.contains("Č");
        boolean dodajOtkazan = flags.contains("O");

        List<Rezervacija> zaIspis = ulaz.stream()
                .filter(r -> (dodajPA && (r.getStatus() == StatusRezervacije.PRIMLJENA || r.getStatus() == StatusRezervacije.AKTIVNA))
                        || (dodajČekaj && r.getStatus() == StatusRezervacije.NA_CEKANJU)
                        || (dodajOtkazan && r.getStatus() == StatusRezervacije.OTKAZANA))
                .collect(Collectors.toList());

        if (zaIspis.isEmpty()) 
        {
            System.out.println("Nema rezervacija koje zadovoljavaju kriterij za aranžman " + oznaka + ".");
            return;
        }

        int[] w = dodajOtkazan ? new int[]{20, 20, 22, 14, 35} : new int[]{20, 20, 22, 14};
        printajSeperator(w);

        if (dodajOtkazan) 
        {
            printajRed(w, "Ime", "Prezime", "Datum i vrijeme", "Vrsta", "Otkazano");
        } 
        else 
        {
            printajRed(w, "Ime", "Prezime", "Datum i vrijeme", "Vrsta");
        }
        printajSeperator(w);

        for (Rezervacija r : zaIspis)
        {
            String ime = r.getIme();
            String prezime = r.getPrezime();
            String datumVrijeme = fmtDatumVrijeme(r.getDatumVrijeme(), r.getDatumVrijemeRaw());
            String vrsta = statusOznaka(r.getStatus());

            if (dodajOtkazan)
            {
                String otk = (r.getOtkazanoAt() == null) ? "-" : fmtDatumVrijeme(r.getOtkazanoAt(), null);
                printajRed(w, ime, prezime, datumVrijeme, vrsta, otk);
            } 
            else 
            {
                printajRed(w, ime, prezime, datumVrijeme, vrsta);
            }
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
        if (s == null)
        {
        	return "-";
        }
        return switch (s) {
            case PRIMLJENA -> "PRIMLJENA";
            case AKTIVNA -> "AKTIVNA";
            case NA_CEKANJU -> "NA ČEKANJU";
            case OTKAZANA -> "OTKAZANA";
        };
    }

    private void printajSeperator(int[] widths) 
    {
        StringBuilder sb = new StringBuilder();
        sb.append('+');
        for (int w : widths) 
        {
            for (int i = 0; i < w + 2; i++) sb.append('-');
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
