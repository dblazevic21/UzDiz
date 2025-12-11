package edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.unizg.foi.uzdiz.dblazevic21.app.enumeracije.StatusRezervacije;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
<<<<<<< HEAD
=======
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.AktivnaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.NaCekanjuConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.OtkazanaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.PrimljenaConcreteState;
>>>>>>> b893a38 (Valjda dobar state sada, prca me Linux Git najveće smeće na svitu)

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
<<<<<<< HEAD
            if (rezervacija.getStatus() == StatusRezervacije.OTKAZANA)
=======
            if (rezervacija.getStatus() instanceof OtkazanaConcreteState)
>>>>>>> b893a38 (Valjda dobar state sada, prca me Linux Git najveće smeće na svitu)
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
<<<<<<< HEAD
                    rezervacija.setStatus(StatusRezervacije.PRIMLJENA);
=======
                    rezervacija.setStatus(new PrimljenaConcreteState());
>>>>>>> b893a38 (Valjda dobar state sada, prca me Linux Git najveće smeće na svitu)
                }
            }
            else if (brojRezervacija <= maksBrojPutnika)
            {
                for (Rezervacija rezervacija : rezervacijeZaAranzman)
                {
<<<<<<< HEAD
                    rezervacija.setStatus(StatusRezervacije.AKTIVNA);
=======
                    rezervacija.setStatus(new AktivnaConcreteState());
>>>>>>> b893a38 (Valjda dobar state sada, prca me Linux Git najveće smeće na svitu)
                }
            }
            else
            {
                int aktivniCount = 0;
                for (Rezervacija rezervacija : rezervacijeZaAranzman)
                {
                    if (aktivniCount < maksBrojPutnika)
                    {
<<<<<<< HEAD
                        rezervacija.setStatus(StatusRezervacije.AKTIVNA);
=======
                        rezervacija.setStatus(new AktivnaConcreteState());
>>>>>>> b893a38 (Valjda dobar state sada, prca me Linux Git najveće smeće na svitu)
                        aktivniCount++;
                    }
                    else
                    {
<<<<<<< HEAD
                        rezervacija.setStatus(StatusRezervacije.NA_CEKANJU);
=======
                        rezervacija.setStatus(new NaCekanjuConcreteState());
>>>>>>> b893a38 (Valjda dobar state sada, prca me Linux Git najveće smeće na svitu)
                    }
                }
            }
        }
    }
    
    public boolean otkaziRezervaciju(int oznakaAranzmana, String ime, String prezime, LocalDateTime when) 
    {
        String i = (ime == null) ? "" : ime.trim();
        String p = (prezime == null) ? "" : prezime.trim();

        Rezervacija cilj = sveRezervacije.stream()
<<<<<<< HEAD
                .filter(r -> r.getOznakaAranzmana() == oznakaAranzmana
                        && equalsIgnorirajCase(r.getIme(), i)
                        && equalsIgnorirajCase(r.getPrezime(), p)
                        && r.getStatus() != StatusRezervacije.OTKAZANA)
=======
        		.filter(r -> r.getOznakaAranzmana() == oznakaAranzmana
		                && equalsIgnorirajCase(r.getIme(), i)
		                && equalsIgnorirajCase(r.getPrezime(), p)
		                && !(r.getStatus() instanceof OtkazanaConcreteState))
>>>>>>> b893a38 (Valjda dobar state sada, prca me Linux Git najveće smeće na svitu)
                .min(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .orElse(null);

        if (cilj == null) return false;

<<<<<<< HEAD
        cilj.setStatus(StatusRezervacije.OTKAZANA);
=======
        cilj.setStatus(new OtkazanaConcreteState());
>>>>>>> b893a38 (Valjda dobar state sada, prca me Linux Git najveće smeće na svitu)
        cilj.setOtkazanoAt(when != null ? when : LocalDateTime.now());

        Rezervacija generirajAktivna = sveRezervacije.stream()
                .filter(r -> r.getOznakaAranzmana() == oznakaAranzmana
<<<<<<< HEAD
                        && r.getStatus() == StatusRezervacije.NA_CEKANJU)
=======
			 			&& r.getStatus() instanceof NaCekanjuConcreteState)
>>>>>>> b893a38 (Valjda dobar state sada, prca me Linux Git najveće smeće na svitu)
                .min(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .orElse(null);

        if (generirajAktivna != null) 
        {
<<<<<<< HEAD
            generirajAktivna.setStatus(StatusRezervacije.AKTIVNA);
=======
            generirajAktivna.setStatus(new AktivnaConcreteState());
>>>>>>> b893a38 (Valjda dobar state sada, prca me Linux Git najveće smeće na svitu)
        }
        return true;
    }

    private boolean equalsIgnorirajCase(String rijec1, String rijec2)
    {
        String prvaRijec = (rijec1 == null) ? "" : rijec1.trim();
        String drugaRijec = (rijec2 == null) ? "" : rijec2.trim();
        
        return prvaRijec.equalsIgnoreCase(drugaRijec);
    }
}
