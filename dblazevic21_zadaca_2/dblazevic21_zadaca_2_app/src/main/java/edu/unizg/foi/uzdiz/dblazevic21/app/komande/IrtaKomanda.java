package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.FormaterZaIspise;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.StatusFormater;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.AktivnaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.NaCekanjuConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OdgodenaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OtkazanaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.PrimljenaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.RezervacijeState;

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

        Pattern p = Pattern.compile(
                "^IRTA\\s+(\\d+)(?:\\s+(PA|Č|O|PAČO|ODO))?$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );

        Matcher m = p.matcher(trimmed);
        if (!m.matches())
        {
            System.out.println("Nepoznata komanda. Upotrijebite: IRTA oznaka [PA|Č|O|PAČO|ODO]");
            return;
        }

        int oznaka = Integer.parseInt(m.group(1));
        String flags = (m.group(2) == null) ? "PA" : m.group(2).toUpperCase(Locale.ROOT);

        if (!(flags.equals("PA")
                || flags.equals("Č")
                || flags.equals("O")
                || flags.equals("PAČO")
                || flags.equals("ODO")))
        {
            System.out.println("Greška: neispravni filteri. Dozvoljeni su samo PA, Č, O, PAČO ili ODO.");
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

        boolean dodajPA = flags.equals("PA") || flags.equals("PAČO");
        boolean dodajČekaj = flags.equals("Č") || flags.equals("PAČO");
        boolean dodajOtkazan = flags.equals("O") || flags.equals("PAČO") || flags.equals("ODO");
        boolean dodajOdgođen = flags.equals("ODO");

        List<Rezervacija> zaIspis = ulaz.stream()
                .filter(r -> {
                    RezervacijeState status = r.getStatus();
                    boolean isPrimljenaAktivna =
                            (status instanceof PrimljenaConcreteState || status instanceof AktivnaConcreteState);
                    boolean isNaCekanju = (status instanceof NaCekanjuConcreteState);
                    boolean isOtkazana = (status instanceof OtkazanaConcreteState);
                    boolean isOdgođena = (status instanceof OdgodenaConcreteState);

                    return (dodajPA && isPrimljenaAktivna)
                            || (dodajČekaj && isNaCekanju)
                            || (dodajOtkazan && isOtkazana)
                            || (dodajOdgođen && isOdgođena);
                })
                .collect(Collectors.toList());

        if (zaIspis.isEmpty())
        {
            System.out.println("Nema rezervacija koje zadovoljavaju kriterij za aranžman " + oznaka + ".");
            return;
        }

        boolean trebaOtkazStupac = flags.equals("O") || flags.equals("PAČO") || flags.equals("ODO");
        int[] sirine = trebaOtkazStupac
                ? new int[]{20, 20, 22, 14, 35}
                : new int[]{20, 20, 22, 14};

        TablicaPrinter.printajTablicuZaglavlje(sirine, trebaOtkazStupac);

        for (Rezervacija r : zaIspis)
        {
            String ime = r.getIme();
            String prezime = r.getPrezime();
            String datumVrijeme = FormaterZaIspise.fmtDatumVrijeme(r.getDatumVrijeme(), r.getDatumVrijemeRaw());
            String vrsta = StatusFormater.statusOznaka(r.getStatus());

            if (trebaOtkazStupac)
            {
                String otk = (r.getOtkazanoAt() == null)
                        ? "-"
                        : FormaterZaIspise.fmtDatumVrijeme(r.getOtkazanoAt(), null);
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
