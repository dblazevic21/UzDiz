package edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.unizg.foi.uzdiz.dblazevic21.app.component.TuristickiElementComponent;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.AktivnaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.NaCekanjuConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OdgodenaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OtkazanaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.PrimljenaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.strategy.NullStrategija;
import edu.unizg.foi.uzdiz.dblazevic21.app.strategy.UpravljanjeRezervacijamaStrategija;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.GramatikaIJezikApp;

public class Rezervacije 
{
    private static volatile Rezervacije INSTANCE;
    
    private UpravljanjeRezervacijamaStrategija strategija = new NullStrategija();
    
    public void setStrategija(UpravljanjeRezervacijamaStrategija strategija)
    {
    	this.strategija = (strategija == null) ? new NullStrategija() : strategija;
    }
    
    public UpravljanjeRezervacijamaStrategija getStrategija()
	{
		return this.strategija;
	}
    
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
            for (TuristickiElementComponent dijete : a.getDjeca())
            {
                sve.add((Rezervacija) dijete);
            }
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
        azurirajStatuseRezervacija(aranzmani, true);
    }

    private void azurirajStatuseRezervacija(Map<Integer, Aranzmani> aranzmani, boolean saljiObavijesti)
    {
        Map<Integer, Map<Long, String>> staraStanja = saljiObavijesti ? snimiStanjaPoAranzmanu(aranzmani) : null;

        for (Aranzmani a : aranzmani.values())
        {
            for (var dijete : a.getDjeca())
            {
                Rezervacija r = (Rezervacija) dijete;
                if (!(r.getStatus() instanceof OtkazanaConcreteState))
                {
                    r.setStatus(new PrimljenaConcreteState());
                }
            }
        }

        if (strategija != null)
        {
            strategija.primijeni(aranzmani);
        }

        upravljanjeStatusimaUvjeti(aranzmani);

        if (strategija != null)
        {
            strategija.primijeniNakonKapaciteta(aranzmani);
        }

        upravljanjeStatusimaUvjeti(aranzmani);

        if (saljiObavijesti)
        {
            Map<Integer, Map<Long, String>> novaStanja = snimiStanjaPoAranzmanu(aranzmani);
            posaljiObavijestiOPromjenama(aranzmani, staraStanja, novaStanja);
        }
    }

    
    private Map<Integer, Map<Long, String>> snimiStanjaPoAranzmanu(Map<Integer, Aranzmani> aranzmani)
    {
        Map<Integer, Map<Long, String>> rezultat = new HashMap<>();

        for (Aranzmani a : aranzmani.values())
        {
            Map<Long, String> mapa = new HashMap<>();

            for (TuristickiElementComponent dijete : a.getDjeca())
            {
                Rezervacija r = (Rezervacija) dijete;

                String naziv = (r.getStatus() != null) ? r.getStatus().getNaziv() : "";
                mapa.put(r.getRedniBroj(), naziv);
            }

            rezultat.put(a.getOznaka(), mapa);
        }

        return rezultat;
    }

    private void posaljiObavijestiOPromjenama(
            Map<Integer, Aranzmani> aranzmani,
            Map<Integer, Map<Long, String>> staraStanja,
            Map<Integer, Map<Long, String>> novaStanja)
    {
        for (Aranzmani a : aranzmani.values())
        {
            int oznaka = a.getOznaka();

            Map<Long, String> stara = staraStanja.getOrDefault(oznaka, Collections.emptyMap());
            
            Map<Long, String> nova = novaStanja.getOrDefault(oznaka, Collections.emptyMap());

            List<String> promjene = new ArrayList<>();

            List<Rezervacija> sortirane = a.getDjeca().stream()
                    .map(x -> (Rezervacija) x)
                    .sorted(Comparator.comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                                      .thenComparingLong(Rezervacija::getRedniBroj))
                    .collect(Collectors.toList());

            for (Rezervacija r : sortirane)
            {
                String prije = stara.getOrDefault(r.getRedniBroj(), "");
                String poslije = nova.getOrDefault(r.getRedniBroj(), "");

                if (!GramatikaIJezikApp.equalsIgnorirajCase(prije, poslije))
                {
                    String ime = GramatikaIJezikApp.velikoPocetnoSlovo(r.getIme());
                    String prezime = GramatikaIJezikApp.velikoPocetnoSlovo(r.getPrezime());

                    String lijevo = (prije == null || prije.isBlank()) ? "-" : prije;
                    String desno = (poslije == null || poslije.isBlank()) ? "-" : poslije;

                    promjene.add("Promjena stanja: " + ime + " " + prezime + " - " + lijevo + " -> " + desno);
                }
            }

            if (!promjene.isEmpty())
            {
                a.notifyObservers(String.join("\n", promjene));
            }
        }
    }

    public void upravljanjeStatusimaUvjeti(Map<Integer, Aranzmani> aranzmani)
    {
        for (Aranzmani a : aranzmani.values())
        {
            List<Rezervacija> kandidat = a.getDjeca().stream()
                    .map(x -> (Rezervacija) x)
                    .filter(r -> !(r.getStatus() instanceof OtkazanaConcreteState))
                    .filter(r -> !(r.getStatus() instanceof OdgodenaConcreteState))
                    .sorted(Comparator.comparing(Rezervacija::getDatumVrijeme)
                                      .thenComparingLong(Rezervacija::getRedniBroj))
                    .collect(Collectors.toList());

            int broj = kandidat.size();
            int brojAktivnihZaState = 0;

            if (broj < a.getMinBrojPutnika())
            {
                for (Rezervacija r : kandidat)
                {
                    r.setStatus(new PrimljenaConcreteState());
                }
                brojAktivnihZaState = 0;
            }
            else if (broj <= a.getMaksBrojPutnika())
            {
                for (Rezervacija r : kandidat)
                {
                    r.setStatus(new AktivnaConcreteState());
                }
                brojAktivnihZaState = broj;
            }
            else
            {
                int max = a.getMaksBrojPutnika();
                int aktivniCount = 0;

                for (Rezervacija r : kandidat)
                {
                    if (aktivniCount < max)
                    {
                        r.setStatus(new AktivnaConcreteState());
                        aktivniCount++;
                    }
                    else
                    {
                        r.setStatus(new NaCekanjuConcreteState());
                    }
                }
                brojAktivnihZaState = max;
            }

            if (a.getStatus() != null)
            {
                a.getStatus().azuriraj(a, brojAktivnihZaState);
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
    	ocistiRezervacije(aranzmani);
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

        Map<Integer, Map<Long, String>> staraStanja = snimiStanjaPoAranzmanu(aranzmani);

        Rezervacija r = a.getRezervacije().stream()
            .filter(x -> GramatikaIJezikApp.equalsIgnorirajCase(x.getIme(), ime)
                      && GramatikaIJezikApp.equalsIgnorirajCase(x.getPrezime(), prezime)
                      && !(x.getStatus() instanceof OtkazanaConcreteState))
            .min(Comparator.comparing(Rezervacija::getDatumVrijeme))
            .orElse(null);

        if (r == null) return false;

        r.setStatus(new OtkazanaConcreteState());
        r.setOtkazanoAt(when != null ? when : LocalDateTime.now());

        azurirajStatuseRezervacija(aranzmani, false);

        Map<Integer, Map<Long, String>> novaStanja = snimiStanjaPoAranzmanu(aranzmani);

        posaljiObavijestiOPromjenama(aranzmani, staraStanja, novaStanja);

        return true;
    }


	public void provjeriIObavijesti(Aranzmani a, Map<Long, String> stanjePrije) 
	{
		List<String> promjene = new ArrayList<>();
        for (TuristickiElementComponent dijete : a.getDjeca())
        {
            Rezervacija rr = (Rezervacija) dijete;

            String prije = stanjePrije.getOrDefault(rr.getRedniBroj(), "");
            String poslije = (rr.getStatus() != null) ? rr.getStatus().getNaziv() : "";

            if (!GramatikaIJezikApp.equalsIgnorirajCase(prije, poslije))
            {
                String imeP = GramatikaIJezikApp.velikoPocetnoSlovo(rr.getIme());
                String prezimeP = GramatikaIJezikApp.velikoPocetnoSlovo(rr.getPrezime());

                String prijeFmt = (prije == null || prije.isBlank()) ? "-" : prije.toUpperCase();
                String poslijeFmt = (poslije == null || poslije.isBlank()) ? "-" : poslije.toUpperCase();

                promjene.add("Promjena stanja: " + imeP + " " + prezimeP + " - " + prijeFmt + " -> " + poslijeFmt);
            }
        }

        if (!promjene.isEmpty())
        {
            a.notifyObservers(String.join("\n", promjene));
        }
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
        return aranzman.getDjeca().stream()
        		.map(e -> (Rezervacija) e)
        		.collect(Collectors.toList());
    }

	public boolean dodajRezervaciju(String ime, String prezime, int oznakaAranzmana,
	        LocalDateTime datumVrijeme, Map<Integer, Aranzmani> aranzmani)
	{
	    Aranzmani a = aranzmani.get(oznakaAranzmana);
	    if (a == null) return false;

	    Map<Integer, Map<Long, String>> staraStanja = snimiStanjaPoAranzmanu(aranzmani);

	    Rezervacija novaRezervacija = kreirajRezervaciju(ime, prezime, oznakaAranzmana, datumVrijeme);
	    a.dodajDijete(novaRezervacija);

	    azurirajStatuseRezervacija(aranzmani, false);

	    Map<Integer, Map<Long, String>> novaStanja = snimiStanjaPoAranzmanu(aranzmani);

	    posaljiObavijestiOPromjenama(aranzmani, staraStanja, novaStanja);

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
