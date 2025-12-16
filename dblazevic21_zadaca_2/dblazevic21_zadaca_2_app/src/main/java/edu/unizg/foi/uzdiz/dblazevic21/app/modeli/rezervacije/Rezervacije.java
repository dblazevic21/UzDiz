package edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.AktivnaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.NaCekanjuConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OdgodenaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OtkazanaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.PrimljenaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.GramatikaIJezikApp;

public class Rezervacije 
{
    private static volatile Rezervacije INSTANCE;
    
    private long brojac = 0;
    
    private static final Comparator<LocalDateTime> LDT_ORDER = 
            Comparator.nullsLast(Comparator.<LocalDateTime>naturalOrder());

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

    public Rezervacija kreirajRezervaciju(String ime, String prezime, int oznaka, LocalDateTime dt)
    {
        return new Rezervacija(++brojac, ime, prezime, oznaka, dt);
    }

    public Rezervacija kreirajRezervaciju(String ime, String prezime, int oznaka, String dtRaw) 
    {
        return new Rezervacija(++brojac, ime, prezime, oznaka, dtRaw);
    }

    public List<Rezervacija> getSveRezervacije(Map<Integer, Aranzmani> aranzmani) 
    {
        List<Rezervacija> sve = new ArrayList<>();
        for (Aranzmani a : aranzmani.values())
        {
            sve.addAll(a.getRezervacije());
        }
        return sve;
    }

    
    
    public List<Rezervacija> getZaOsobu(String ime, String prezime, Map<Integer, Aranzmani> aranzmani)
    {
        String imeIzrezano = (ime == null) ? "" : ime.trim();
        String prezimeIzrezano = (prezime == null) ? "" : prezime.trim();

        List<Rezervacija> sve = getSveRezervacije(aranzmani);
        
        return sve.stream()
                .filter(r -> GramatikaIJezikApp.equalsIgnorirajCase(r.getIme(), imeIzrezano)
                          && GramatikaIJezikApp.equalsIgnorirajCase(r.getPrezime(), prezimeIzrezano))
                .sorted(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .collect(Collectors.toList());
    }
    
    public void azurirajStatuseRezervacija(Map<Integer, Aranzmani> aranzmani) 
    {
        for (Aranzmani a : aranzmani.values())
        {
            for (Rezervacija r : a.getRezervacije()) 
            {
                if (!(r.getStatus() instanceof OtkazanaConcreteState)) 
                {
                    r.setStatus(new PrimljenaConcreteState());
                }
            }
        }

    
        Map<String, List<Rezervacija>> poOsobi = new HashMap<>();
        for (Aranzmani a : aranzmani.values())
        {
            for (Rezervacija r : a.getRezervacije()) 
            {
                if (r.getStatus() instanceof OtkazanaConcreteState) continue;
                String key = (r.getIme() + "_" + r.getPrezime()).toLowerCase();
                poOsobi.computeIfAbsent(key, k -> new ArrayList<>()).add(r);
            }
        }

        for (List<Rezervacija> rezOsobe : poOsobi.values())
        {
            rezOsobe.sort(Comparator.comparing(Rezervacija::getDatumVrijeme)
                                     .thenComparingLong(Rezervacija::getRedniBroj));
            boolean prvaAktivnaPostavljena = false;
            for (Rezervacija r : rezOsobe)
            {
                if (r.getStatus() instanceof PrimljenaConcreteState)
                {
                    if (!prvaAktivnaPostavljena)
                    {
                        prvaAktivnaPostavljena = true;
                    }
                    else 
                    {
                        r.setStatus(new OdgodenaConcreteState());
                    }
                }
            }
        }

        for (Aranzmani a : aranzmani.values()) 
        {
            List<Rezervacija> aktivneRezervacije = a.getRezervacije().stream()
                    .filter(r -> r.getStatus() instanceof PrimljenaConcreteState)
                    .sorted(Comparator.comparing(Rezervacija::getDatumVrijeme)
                                      .thenComparingLong(Rezervacija::getRedniBroj))
                    .collect(Collectors.toList());

            int brojRezervacija = aktivneRezervacije.size();

            if (brojRezervacija < a.getMinBrojPutnika())
            {
                for (Rezervacija r : aktivneRezervacije) 
                {
                    r.setStatus(new PrimljenaConcreteState());
                }
            } 
            else if (brojRezervacija <= a.getMaksBrojPutnika())
            {
                for (Rezervacija r : aktivneRezervacije) 
                {
                    r.setStatus(new AktivnaConcreteState());
                }
            }
            else 
            {
                int aktivniCount = 0;
                for (Rezervacija r : aktivneRezervacije) 
                {
                    if (aktivniCount < a.getMaksBrojPutnika()) 
                    {
                        r.setStatus(new AktivnaConcreteState());
                        aktivniCount++;
                    } 
                    else 
                    {
                        r.setStatus(new NaCekanjuConcreteState());
                    }
                }
            }

            if (a.getStatus() != null) {
                a.getStatus().azuriraj(a, a.getBrojAktivnihRezervacija());
            }
        }
    }

    public void ocistiRezervacije(Map<Integer, Aranzmani> aranzmani) 
    {
        for (Aranzmani aranzman : aranzmani.values()) 
        {
            aranzman.ocistiRezervacije();
        }
    }

    public void ocistiSve(Map<Integer, Aranzmani> aranzmani) 
    {
        for (Aranzmani aranzman : aranzmani.values()) 
        {
            aranzman.ocistiRezervacije();
        }
        aranzmani.clear();
    }

    public void reset() 
    {
        this.brojac = 0;
    }
 
    public boolean otkaziRezervaciju(int oznakaAranzmana, String ime, String prezime,
            LocalDateTime when, Map<Integer, Aranzmani> aranzmani)
    {
        Aranzmani a = aranzmani.get(oznakaAranzmana);
        if (a == null) return false;

        Rezervacija r = a.getRezervacije().stream()
            .filter(x -> GramatikaIJezikApp.equalsIgnorirajCase(x.getIme(), ime)
                      && GramatikaIJezikApp.equalsIgnorirajCase(x.getPrezime(), prezime)
                      && !(x.getStatus() instanceof OtkazanaConcreteState))
            .min(Comparator.comparing(Rezervacija::getDatumVrijeme))
            .orElse(null);

        if (r == null) return false;

        r.setStatus(new OtkazanaConcreteState());
        r.setOtkazanoAt(when != null ? when : LocalDateTime.now());

        azurirajStatuseRezervacija(aranzmani);
        return true;
    }


	public List<Rezervacija> getZaAranzman(int oznakaAranzmana, Map<Integer, Aranzmani> aranzmani)
	{
	    Aranzmani aranzman = aranzmani.get(oznakaAranzmana);
	    if (aranzman == null)
	    {
	        return new ArrayList<>();
	    }
	    return getZaAranzman(aranzman);
	}
	
	public List<Rezervacija> getZaAranzman(Aranzmani aranzman)
    {
        return new ArrayList<>(aranzman.getRezervacije()); 
    }

	public boolean dodajRezervaciju(String ime, String prezime, int oznakaAranzmana,
	        LocalDateTime datumVrijeme, Map<Integer, Aranzmani> aranzmani)
	{
	    Aranzmani aranzman = aranzmani.get(oznakaAranzmana);
	    if (aranzman == null)
	    {
	        return false;
	    }


	    Rezervacija novaRezervacija = kreirajRezervaciju(ime, prezime, oznakaAranzmana, datumVrijeme);
	    aranzman.dodajRezervaciju(novaRezervacija);

	    return true;
	}

	public boolean dodajRezervacijuBezStatusa(String ime, String prezime, int oznakaAranzmana,
	                                           LocalDateTime datumVrijeme, Map<Integer, Aranzmani> aranzmani)
	{
	    Aranzmani aranzman = aranzmani.get(oznakaAranzmana);
	    if (aranzman == null)
	    {
	        return false;
	    }


	    Rezervacija nova = kreirajRezervaciju(ime, prezime, oznakaAranzmana, datumVrijeme);
	    aranzman.dodajRezervaciju(nova);
	    return true;
	}
}
