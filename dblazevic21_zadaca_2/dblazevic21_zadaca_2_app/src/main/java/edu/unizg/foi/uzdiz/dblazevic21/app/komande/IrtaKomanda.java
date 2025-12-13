package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.FormaterZaIspise;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.IspisKonfiguracija;
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
        String odrezan = (unos == null) ? "" : unos.trim();

        Pattern p = Pattern.compile(
                "^IRTA\\s+(\\d+)(?:\\s+([A-ZČOD]+))?$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );

        Matcher m = p.matcher(odrezan);
        if (!m.matches())
        {
            System.out.println(
                    "Nepoznata komanda. Upotrijebite: IRTA oznaka [PA|Č|O|OD] i njihove kombinacije (npr. PAČO, ČOPA, ODO)."
            );
            return;
        }

        int oznaka = Integer.parseInt(m.group(1));
        String rawFlags = (m.group(2) == null)
                ? "PA"
                : m.group(2).toUpperCase(Locale.ROOT);

        final boolean[] flags = new boolean[4];

        String s = rawFlags;
        while (!s.isEmpty())
        {
            if (s.startsWith("PA"))
            {
                flags[0] = true;
                s = s.substring(2);
            }
            else if (s.startsWith("OD"))
            {
                flags[3] = true;
                s = s.substring(2);
            }
            else if (s.startsWith("Č"))
            {
                flags[1] = true;
                s = s.substring(1);
            }
            else if (s.startsWith("O"))
            {
                flags[2] = true;
                s = s.substring(1);
            }
            else
            {
                System.out.println(
                        "Greška u zastavicama: dozvoljene komande su samo PA, Č, O, OD (npr. PAČO, ČOPA, ODO)."
                );
                return;
            }
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

        List<Rezervacija> zaIspis = ulaz.stream()
                .filter(r -> {
                    RezervacijeState status = r.getStatus();
                    boolean isPrimljenaAktivna =
                            (status instanceof PrimljenaConcreteState || status instanceof AktivnaConcreteState);
                    boolean isNaCekanju = status instanceof NaCekanjuConcreteState;
                    boolean isOtkazana = status instanceof OtkazanaConcreteState;
                    boolean isOdgodena = status instanceof OdgodenaConcreteState;

                    return (flags[0] && isPrimljenaAktivna) 
                            || (flags[1] && isNaCekanju)    
                            || (flags[2] && isOtkazana)     
                            || (flags[3] && isOdgodena);   
                })
                .collect(Collectors.toList());

        if (IspisKonfiguracija.jeObrnutoKronoloski())
        {
            Collections.reverse(zaIspis);
        }

        if (zaIspis.isEmpty())
        {
            System.out.println("Nema rezervacija koje zadovoljavaju kriterij za aranžman " + oznaka + ".");
            return;
        }

        boolean trebaOtkazStupac = flags[2] || flags[3]; 
        int[] sirine = trebaOtkazStupac
                ? new int[]{20, 20, 22, 14, 35}
                : new int[]{20, 20, 22, 14};
        
        System.out.println();
        System.out.println(unos);
        System.out.println("Ispis i pregled rezervacija za aranžman " + oznaka + " - " + a.getNaziv() + ":");
        System.out.println();

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
