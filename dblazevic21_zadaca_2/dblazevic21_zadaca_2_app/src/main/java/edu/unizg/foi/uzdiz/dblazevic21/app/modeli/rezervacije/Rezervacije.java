package edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.AktivnaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.NaCekanjuConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.OdgodenaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.OtkazanaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.PrimljenaConcreteState;

public class Rezervacije 
{
    private static volatile Rezervacije INSTANCE;
    
    private final List<Rezervacija> sveRezervacije = new ArrayList<>();
    private long brojac = 0;
    
    private static final Comparator<LocalDateTime> LDT_ORDER = Comparator.nullsLast(Comparator.<LocalDateTime>naturalOrder());

    private Rezervacije() {}

    public static Rezervacije getInstance() 
    {
        if (INSTANCE == null) 
        {
            synchronized (Rezervacije.class) 
            {
                if (INSTANCE == null) 
                {
                    INSTANCE = new Rezervacije();
                }
            }
        }
        return INSTANCE;
    }

    public void dodajRezervaciju(String ime, String prezime, int oznaka, LocalDateTime dt)
    {
    	sveRezervacije.add(new Rezervacija(++brojac, ime, prezime, oznaka, dt));
    }

    public void dodajRezervaciju(String ime, String prezime, int oznaka, String dtRaw) 
    {
    	sveRezervacije.add(new Rezervacija(++brojac, ime, prezime, oznaka, dtRaw));
    }
    
    public List<Rezervacija> getSveRezervacije() {
        return new ArrayList<>(sveRezervacije);
    }

    public List<Rezervacija> getZaAranzman(int oznaka)
    {
        return sveRezervacije.stream()
                .filter(r -> r.getOznakaAranzmana() == oznaka)
                .sorted(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .collect(Collectors.toList());
    }
    
    public List<Rezervacija> getZaOsobu(String ime, String prezime)
    {
        String imeIzrezano = (ime == null) ? "" : ime.trim();
        String prezimeIzrezano = (prezime == null) ? "" : prezime.trim();

        return sveRezervacije.stream()
                .filter(r -> equalsIgnorirajCase(r.getIme(), imeIzrezano)
                          && equalsIgnorirajCase(r.getPrezime(), prezimeIzrezano))
                .sorted(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .collect(Collectors.toList());
    }
    
    public void azurirajStatuseRezervacija(Map<Integer, Aranzmani> aranzmani)
    {
    	Map<Integer, List<Rezervacija>> rezervacijePoAranzmanu = new HashMap<>();

        for (Rezervacija rezervacija : sveRezervacije)
        {
            if (rezervacija.getStatus() instanceof OtkazanaConcreteState)
            {
                continue;
            }

            rezervacijePoAranzmanu
                    .computeIfAbsent(rezervacija.getOznakaAranzmana(), k -> new ArrayList<>())
                    .add(rezervacija);
        }

        for (Map.Entry<Integer, List<Rezervacija>> entry : rezervacijePoAranzmanu.entrySet())
        {
            int oznakaAranzmana = entry.getKey();
            List<Rezervacija> rezervacijeZaAranzman = entry.getValue();
            Aranzmani aranzman = aranzmani.get(oznakaAranzmana);

            if (aranzman == null) continue;

            rezervacijeZaAranzman.sort(Comparator
                    .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                    .thenComparingLong(Rezervacija::getRedniBroj));

            int brojRezervacija = rezervacijeZaAranzman.size();
            int minBrojPutnika = aranzman.getMinBrojPutnika();
            int maksBrojPutnika = aranzman.getMaksBrojPutnika();

            if (brojRezervacija < minBrojPutnika)
            {
                for (Rezervacija rezervacija : rezervacijeZaAranzman)
                {
                    rezervacija.setStatus(new PrimljenaConcreteState());
                }
            }
            else if (brojRezervacija <= maksBrojPutnika)
            {
                for (Rezervacija rezervacija : rezervacijeZaAranzman)
                {
                    rezervacija.setStatus(new AktivnaConcreteState());
                }
            }
            else
            {
                int aktivniCount = 0;
                for (Rezervacija rezervacija : rezervacijeZaAranzman)
                {
                    if (aktivniCount < maksBrojPutnika)
                    {
                        rezervacija.setStatus(new AktivnaConcreteState());
                        aktivniCount++;
                    }
                    else
                    {
                        rezervacija.setStatus(new NaCekanjuConcreteState());
                    }
                }
            }
        }
        azurirajOdgodeneRezervacije(aranzmani);
    }
    
    private void azurirajOdgodeneRezervacije(Map<Integer, Aranzmani> aranzmani)
    {
        Map<String, List<Rezervacija>> rezervacijePoOsobi = new HashMap<>();

        for (Rezervacija r : sveRezervacije)
        {
            if (r.getStatus() instanceof OtkazanaConcreteState)
            {
                continue;
            }

            String kljuc = (r.getIme() + "_" + r.getPrezime()).toLowerCase();
            rezervacijePoOsobi
                    .computeIfAbsent(kljuc, k -> new ArrayList<>())
                    .add(r);
        }

        for (List<Rezervacija> rezervacijeOsobe : rezervacijePoOsobi.values())
        {
            if (rezervacijeOsobe.size() < 2)
            {
                continue;
            }

            rezervacijeOsobe.sort(Comparator
                    .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                    .thenComparingLong(Rezervacija::getRedniBroj));

            for (int i = 0; i < rezervacijeOsobe.size(); i++)
            {
                Rezervacija trenutna = rezervacijeOsobe.get(i);

                if (trenutna.getStatus() instanceof OtkazanaConcreteState ||
                    trenutna.getStatus() instanceof OdgodenaConcreteState)
                {
                    continue;
                }

                Aranzmani aranzmanTrenutni = aranzmani.get(trenutna.getOznakaAranzmana());
                if (aranzmanTrenutni == null) continue;

                for (int j = 0; j < i; j++)
                {
                    Rezervacija ranija = rezervacijeOsobe.get(j);

                    if (ranija.getStatus() instanceof OtkazanaConcreteState)
                    {
                        continue;
                    }

                    Aranzmani aranzmanRaniji = aranzmani.get(ranija.getOznakaAranzmana());
                    if (aranzmanRaniji == null) continue;

                    if (aranzmaniSePreklapaju(aranzmanRaniji, aranzmanTrenutni))
                    {
                        if (ranija.getStatus() instanceof AktivnaConcreteState ||
                            ranija.getStatus() instanceof PrimljenaConcreteState ||
                            ranija.getStatus() instanceof NaCekanjuConcreteState)
                        {
                            trenutna.setStatus(new OdgodenaConcreteState());
                            break;
                        }
                    }
                }
            }
        }
    }
    
    private boolean aranzmaniSePreklapaju(Aranzmani a1, Aranzmani a2)
    {
        if (a1.getPocetniDatum() == null || a1.getZavrsniDatum() == null ||
            a2.getPocetniDatum() == null || a2.getZavrsniDatum() == null)
        {
            return false;
        }

        return !a1.getPocetniDatum().isAfter(a2.getZavrsniDatum()) &&
               !a2.getPocetniDatum().isAfter(a1.getZavrsniDatum());
    }
    
    public boolean otkaziRezervaciju(int oznakaAranzmana, String ime, String prezime, LocalDateTime when) 
    {
        String i = (ime == null) ? "" : ime.trim();
        String p = (prezime == null) ? "" : prezime.trim();

        Rezervacija cilj = sveRezervacije.stream()
                .filter(r -> r.getOznakaAranzmana() == oznakaAranzmana
                        && equalsIgnorirajCase(r.getIme(), i)
                        && equalsIgnorirajCase(r.getPrezime(), p)
                        && !(r.getStatus() instanceof OtkazanaConcreteState))
                .min(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .orElse(null);

        if (cilj == null) return false;

        boolean bilaAktivna = cilj.getStatus() instanceof AktivnaConcreteState;

        cilj.setStatus(new OtkazanaConcreteState());
        cilj.setOtkazanoAt(when != null ? when : LocalDateTime.now());

        if (bilaAktivna)
        {
            Rezervacija generirajAktivna = sveRezervacije.stream()
                    .filter(r -> r.getOznakaAranzmana() == oznakaAranzmana
                            && r.getStatus() instanceof NaCekanjuConcreteState)
                    .min(Comparator
                            .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                            .thenComparingLong(Rezervacija::getRedniBroj))
                    .orElse(null);

            if (generirajAktivna != null) 
            {
                generirajAktivna.setStatus(new AktivnaConcreteState());
            }
            promakniOdgodenuRezervaciju(i, p, cilj);
        }

        return true;
    }

    private void promakniOdgodenuRezervaciju(String ime, String prezime, Rezervacija otkazana)
    {
        Rezervacija odgodena = sveRezervacije.stream()
                .filter(r -> equalsIgnorirajCase(r.getIme(), ime)
                        && equalsIgnorirajCase(r.getPrezime(), prezime)
                        && r.getStatus() instanceof OdgodenaConcreteState)
                .min(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .orElse(null);

        if (odgodena != null)
        {
            boolean imaPreklapanje = sveRezervacije.stream()
                    .anyMatch(r -> equalsIgnorirajCase(r.getIme(), ime)
                            && equalsIgnorirajCase(r.getPrezime(), prezime)
                            && r != odgodena
                            && r.getStatus() instanceof AktivnaConcreteState);

            if (!imaPreklapanje)
            {
                odgodena.setStatus(new AktivnaConcreteState());
            }
        }
    }

    private boolean equalsIgnorirajCase(String rijec1, String rijec2)
    {
        String prvaRijec = (rijec1 == null) ? "" : rijec1.trim();
        String drugaRijec = (rijec2 == null) ? "" : rijec2.trim();
        
        return prvaRijec.equalsIgnoreCase(drugaRijec);
    }
}
