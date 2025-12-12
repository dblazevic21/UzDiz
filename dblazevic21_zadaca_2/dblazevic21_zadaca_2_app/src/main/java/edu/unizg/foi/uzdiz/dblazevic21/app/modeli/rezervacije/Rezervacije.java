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
import edu.unizg.foi.uzdiz.dblazevic21.app.turistickaAgencija.TuristickaAgencija;

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

    public List<Rezervacija> getZaAranzman(Aranzmani aranzman)
    {
        return aranzman.getRezervacijeSortirane();
    }
    
    public List<Rezervacija> getZaOsobu(String ime, String prezime, Map<Integer, Aranzmani> aranzmani)
    {
        String imeIzrezano = (ime == null) ? "" : ime.trim();
        String prezimeIzrezano = (prezime == null) ? "" : prezime.trim();

        List<Rezervacija> sve = getSveRezervacije(aranzmani);
        
        return sve.stream()
                .filter(r -> equalsIgnorirajCase(r.getIme(), imeIzrezano)
                          && equalsIgnorirajCase(r.getPrezime(), prezimeIzrezano))
                .sorted(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .collect(Collectors.toList());
    }
    
    public void azurirajStatuseRezervacija(Map<Integer, Aranzmani> aranzmani)
    {
        for (Aranzmani aranzman : aranzmani.values())
        {
            aranzman.azurirajStatuseRezervacija();
        }
        
        azurirajOdgodeneRezervacije(aranzmani);
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
    
    public boolean otkaziRezervaciju(int oznakaAranzmana, String ime, String prezime, 
            LocalDateTime when, Map<Integer, Aranzmani> aranzmani) 
    {
    	String i = (ime == null) ? "" : ime.trim();
    	String p = (prezime == null) ? "" : prezime.trim();

    	Aranzmani aranzman = aranzmani.get(oznakaAranzmana);
    	if (aranzman == null) return false;

    	Rezervacija cilj = aranzman.getRezervacije().stream()
    			.filter(r -> equalsIgnorirajCase(r.getIme(), i)
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
    		Rezervacija generirajAktivna = aranzman.getRezervacije().stream()
    				.filter(r -> r.getStatus() instanceof NaCekanjuConcreteState)
    				.min(Comparator
    						.comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
    						.thenComparingLong(Rezervacija::getRedniBroj))
    				.orElse(null);
    		
    		if (generirajAktivna != null) 
    		{
    			boolean imaAktivnuKojaSePreklapaju = imaAktivnuRezervacijuKojaSePrklapa(
    					generirajAktivna.getIme(), 
    					generirajAktivna.getPrezime(), 
    					aranzman, 
    					aranzmani
    					);
    			
    			if (imaAktivnuKojaSePreklapaju)
    			{
    				generirajAktivna.setStatus(new OdgodenaConcreteState());
    			}
    			else
    			{
    				generirajAktivna.setStatus(new AktivnaConcreteState());
    			}
    		}
    		
    		promakniOdgodenuRezervaciju(i, p, cilj, aranzmani);
    	}
    	
    	return true;
    }
    
    private boolean imaAktivnuRezervacijuKojaSePrklapa(String ime, String prezime, 
                              Aranzmani trenutniAranzman,
                              Map<Integer, Aranzmani> aranzmani)
    {
    	for (Aranzmani a : aranzmani.values())
    	{
    		if (a.getOznaka() == trenutniAranzman.getOznaka())
    		{
    			continue;
    		}
    		
    		if (!aranzmaniSePreklapaju(a, trenutniAranzman))
    		{
    			continue;
    		}
    		
    		for (Rezervacija r : a.getRezervacije())
    		{
    			if (equalsIgnorirajCase(r.getIme(), ime) 
    					&& equalsIgnorirajCase(r.getPrezime(), prezime)
    					&& r.getStatus() instanceof AktivnaConcreteState)
    			{
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    private void azurirajOdgodeneRezervacije(Map<Integer, Aranzmani> aranzmani) {
	    	Map<String, List<Rezervacija>> rezervacijePoOsobi = new HashMap<>();
	
	    for (Aranzmani aranzman : aranzmani.values()) {
	        for (Rezervacija r : aranzman.getRezervacije()) {
	            if (r.getStatus() instanceof OtkazanaConcreteState) {
	                continue;
	            }
	
	            String kljuc = (r.getIme() + "_" + r.getPrezime()).toLowerCase();
	            rezervacijePoOsobi
	                    .computeIfAbsent(kljuc, k -> new ArrayList<>())
	                    .add(r);
	        }
	    }
	
	    for (List<Rezervacija> rezervacijeOsobe : rezervacijePoOsobi.values()) {
	        if (rezervacijeOsobe.size() < 2) {
	            continue;
	        }
	
	        rezervacijeOsobe.sort(Comparator
	                .comparing((Rezervacija r) -> {
	                    Aranzmani a = aranzmani.get(r.getOznakaAranzmana());
	                    return a != null ? a.getPocetniDatum() : null;
	                }, Comparator.nullsLast(Comparator.naturalOrder()))
	                .thenComparingLong(Rezervacija::getRedniBroj));
	
	        for (int i = 0; i < rezervacijeOsobe.size(); i++) {
	            Rezervacija trenutna = rezervacijeOsobe.get(i);
	
	            if (trenutna.getStatus() instanceof OtkazanaConcreteState) {
	                continue;
	            }
	
	            Aranzmani aranzmanTrenutni = aranzmani.get(trenutna.getOznakaAranzmana());
	            if (aranzmanTrenutni == null) continue;
	
	            for (int j = 0; j < i; j++) {
	                Rezervacija ranija = rezervacijeOsobe.get(j);
	
	                if (ranija.getStatus() instanceof OtkazanaConcreteState) {
	                    continue;
	                }
	
	                Aranzmani aranzmanRaniji = aranzmani.get(ranija.getOznakaAranzmana());
	                if (aranzmanRaniji == null) continue;
	
	                if (aranzmaniSePreklapaju(aranzmanRaniji, aranzmanTrenutni)) {
	                    if (ranija.getStatus() instanceof AktivnaConcreteState) {
	                        trenutna.setStatus(new OdgodenaConcreteState());
	                    }
	                    break;
	                }
	            }
	        }
	    }
	}

    
    public List<Rezervacija> getZaOsobu(String ime, String prezime)
    {
        return getZaOsobu(ime, prezime, TuristickaAgencija.getInstance().getAranzmani());
    }

    public List<Rezervacija> getZaAranzman(int oznakaAranzmana)
    {
        Aranzmani aranzman = TuristickaAgencija.getInstance().getAranzmani().get(oznakaAranzmana);
        if (aranzman == null)
        {
            return new ArrayList<>();
        }
        return getZaAranzman(aranzman);
    }

    public boolean dodajRezervaciju(String ime, String prezime, int oznakaAranzmana, LocalDateTime datumVrijeme)
    {
        Map<Integer, Aranzmani> aranzmani = TuristickaAgencija.getInstance().getAranzmani();
        Aranzmani aranzman = aranzmani.get(oznakaAranzmana);
        
        if (aranzman == null)
        {
            return false;
        }
        
        for (Rezervacija r : aranzman.getRezervacije())
        {
            if (r.getIme().equalsIgnoreCase(ime.trim()) 
                && r.getPrezime().equalsIgnoreCase(prezime.trim())
                && !(r.getStatus() instanceof OtkazanaConcreteState))
            {
                return false;
            }
        }
        
        Rezervacija nova = kreirajRezervaciju(ime, prezime, oznakaAranzmana, datumVrijeme);
        aranzman.dodajRezervaciju(nova);
        azurirajStatuseRezervacija(aranzmani);
        
        return true;
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

// Prvo ažuriraj statuse na temelju kapaciteta (PRIMLJENA/AKTIVNA/NA_ČEKANJU)
aranzman.azurirajStatuseRezervacija();

// Zatim ažuriraj ODGOĐENE na temelju preklapanja
azurirajOdgodeneRezervacije(aranzmani);

return true;
}


    private void promakniOdgodenuRezervaciju(String ime, String prezime, 
                                              Rezervacija otkazana, Map<Integer, Aranzmani> aranzmani)
    {
        List<Rezervacija> sve = getSveRezervacije(aranzmani);
        
        Rezervacija odgodena = sve.stream()
                .filter(r -> equalsIgnorirajCase(r.getIme(), ime)
                        && equalsIgnorirajCase(r.getPrezime(), prezime)
                        && r.getStatus() instanceof OdgodenaConcreteState)
                .min(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .orElse(null);

        if (odgodena != null)
        {
            boolean imaPreklapanje = sve.stream()
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
