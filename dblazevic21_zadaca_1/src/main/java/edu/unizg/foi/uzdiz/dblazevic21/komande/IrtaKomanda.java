package edu.unizg.foi.uzdiz.dblazevic21.komande;

import edu.unizg.foi.uzdiz.dblazevic21.enumeracije.StatusRezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.ispis.FormaterZaIspise;
import edu.unizg.foi.uzdiz.dblazevic21.ispis.StatusFormater;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class IrtaKomanda implements Komanda 
{

    private final Map<Integer, Aranzmani> aranzmani;

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

        int[] sirine = dodajOtkazan ? new int[]{20, 20, 22, 14, 35} : new int[]{20, 20, 22, 14};

        TablicaPrinter.printajTablicuZaglavlje(sirine, dodajOtkazan);

        for (Rezervacija r : zaIspis) 
        {
            String ime = r.getIme();
            String prezime = r.getPrezime();
            String datumVrijeme = FormaterZaIspise.fmtDatumVrijeme(r.getDatumVrijeme(), r.getDatumVrijemeRaw());
            String vrsta = StatusFormater.statusOznaka(r.getStatus());

            if (dodajOtkazan) 
            {
                String otk = (r.getOtkazanoAt() == null) ? "-" : FormaterZaIspise.fmtDatumVrijeme(r.getOtkazanoAt(), null);
                TablicaPrinter.printajTablicuRedak(sirine, ime, prezime, datumVrijeme, vrsta, otk);
            }
            else 
            {
                TablicaPrinter.printajTablicuRedak(sirine, ime, prezime, datumVrijeme, vrsta);
            }
        }

        TablicaPrinter.printajTablicuZatvaranje(sirine);
    }
}
