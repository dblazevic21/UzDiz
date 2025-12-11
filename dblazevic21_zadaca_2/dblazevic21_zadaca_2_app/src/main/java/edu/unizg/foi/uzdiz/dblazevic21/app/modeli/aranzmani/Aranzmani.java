package edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.unizg.foi.uzdiz.dblazevic21.app.composite.TuristickiElement;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani.AranzmaniState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani.UPripremiConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.AktivnaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.NaCekanjuConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OtkazanaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.PrimljenaConcreteState;

public class Aranzmani implements TuristickiElement
{
    private final int oznaka;
    private final String naziv;
    private final String program;
    private final LocalDate pocetniDatum;
    private final LocalDate zavrsniDatum;
    private final LocalTime vrijemeKretanja;
    private final LocalTime vrijemePovratka;
    private final float cijena;
    private final int minBrojPutnika;
    private final int maksBrojPutnika;
    private final int brojNocenja;
    private final float doplataSobe;
    private final String prijevoz;
    private final int brojDorucka;
    private final int brojRucka;
    private final int brojVecera;

    private AranzmaniState status;
    
    private final List<Rezervacija> rezervacije = new ArrayList<>();
    private long brojacRezervacija = 0;
    
    private static final Comparator<LocalDateTime> LDT_ORDER = 
        Comparator.nullsLast(Comparator.<LocalDateTime>naturalOrder());

    public Aranzmani(int oznaka, String naziv, String program, LocalDate pocetniDatum,
                     LocalDate zavrsniDatum, LocalTime vrijemeKretanja, LocalTime vrijemePovratka,
                     float cijena, int minBrojPutnika, int maksBrojPutnika, int brojNocenja,
                     float doplataSobe, String prijevoz, int brojDorucka, int brojRucka, int brojVecera) 
    {
        this.oznaka = oznaka;
        this.naziv = naziv;
        this.program = program;
        this.pocetniDatum = pocetniDatum;
        this.zavrsniDatum = zavrsniDatum;
        this.vrijemeKretanja = vrijemeKretanja;
        this.vrijemePovratka = vrijemePovratka;
        this.cijena = cijena;
        this.minBrojPutnika = minBrojPutnika;
        this.maksBrojPutnika = maksBrojPutnika;
        this.brojNocenja = brojNocenja;
        this.doplataSobe = doplataSobe;
        this.prijevoz = prijevoz;
        this.brojDorucka = brojDorucka;
        this.brojRucka = brojRucka;
        this.brojVecera = brojVecera;
        this.status = new UPripremiConcreteState();
    }
    
    @Override
    public String getOpis() 
    {
        return "Aranžman: " + naziv + " (" + oznaka + ") - " + rezervacije.size() + " rezervacija";
    }

    @Override
    public int getBrojOsoba() 
    {
        return (int) rezervacije.stream()
                .filter(r -> !(r.getStatus() instanceof OtkazanaConcreteState))
                .count();
    }

    public void dodajRezervaciju(String ime, String prezime, LocalDateTime datumVrijeme) 
    {
        Rezervacija r = new Rezervacija(++brojacRezervacija, ime, prezime, this.oznaka, datumVrijeme);
        rezervacije.add(r);
        azurirajStatuseRezervacija();
    }

    public void dodajRezervaciju(String ime, String prezime, String datumVrijemeRaw) 
    {
        Rezervacija r = new Rezervacija(++brojacRezervacija, ime, prezime, this.oznaka, datumVrijemeRaw);
        rezervacije.add(r);
        azurirajStatuseRezervacija();
    }
    
    public void dodajRezervaciju(Rezervacija rezervacija)
    {
        rezervacije.add(rezervacija);
    }

    public boolean ukloniRezervaciju(String ime, String prezime) 
    {
        Rezervacija cilj = rezervacije.stream()
                .filter(r -> equalsIgnorirajCase(r.getIme(), ime)
                        && equalsIgnorirajCase(r.getPrezime(), prezime)
                        && !(r.getStatus() instanceof OtkazanaConcreteState))
                .min(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .orElse(null);

        if (cilj == null) 
        {
            return false;
        }

        boolean bilaAktivna = cilj.getStatus() instanceof AktivnaConcreteState;
        
        cilj.setStatus(new OtkazanaConcreteState());
        cilj.setOtkazanoAt(LocalDateTime.now());

        if (bilaAktivna) 
        {
            promovirajSaCekanja();
        }
        
        azurirajStatuseRezervacija();
        return true;
    }

    public List<Rezervacija> getRezervacije() 
    {
        return new ArrayList<>(rezervacije);
    }

    public List<Rezervacija> getAktivneRezervacije() 
    {
        return rezervacije.stream()
                .filter(r -> !(r.getStatus() instanceof OtkazanaConcreteState))
                .sorted(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .collect(Collectors.toList());
    }

    public void azurirajStatuseRezervacija() 
    {
        List<Rezervacija> aktivneRez = rezervacije.stream()
                .filter(r -> !(r.getStatus() instanceof OtkazanaConcreteState))
                .sorted(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .collect(Collectors.toList());

        int brojRezervacija = aktivneRez.size();

        if (brojRezervacija < minBrojPutnika) 
        {
            for (Rezervacija r : aktivneRez) 
            {
                r.setStatus(new PrimljenaConcreteState());
            }
        } 
        else if (brojRezervacija <= maksBrojPutnika) 
        {
            for (Rezervacija r : aktivneRez) 
            {
                r.setStatus(new AktivnaConcreteState());
            }
        } 
        else 
        {
            int aktivniCount = 0;
            for (Rezervacija r : aktivneRez) 
            {
                if (aktivniCount < maksBrojPutnika) 
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
        
        status.azuriraj(this, brojRezervacija);
    }

    private void promovirajSaCekanja() 
    {
        Rezervacija naCekanju = rezervacije.stream()
                .filter(r -> r.getStatus() instanceof NaCekanjuConcreteState)
                .min(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .orElse(null);

        if (naCekanju != null) 
        {
            naCekanju.setStatus(new AktivnaConcreteState());
        }
    }

    public boolean imaRezervaciju(String ime, String prezime) 
    {
        return rezervacije.stream()
                .anyMatch(r -> equalsIgnorirajCase(r.getIme(), ime)
                        && equalsIgnorirajCase(r.getPrezime(), prezime)
                        && !(r.getStatus() instanceof OtkazanaConcreteState));
    }

    private boolean equalsIgnorirajCase(String rijec1, String rijec2) 
    {
        String prvaRijec = (rijec1 == null) ? "" : rijec1.trim();
        String drugaRijec = (rijec2 == null) ? "" : rijec2.trim();
        return prvaRijec.equalsIgnoreCase(drugaRijec);
    }
    
    public int getOznaka() 
    { 
        return oznaka; 
    }
    
    public String getNaziv() 
    { 
        return naziv; 
    }
    
    public String getProgram() 
    { 
        return program; 
    }
    
    public LocalDate getPocetniDatum() 
    { 
        return pocetniDatum; 
    }
    
    public LocalDate getZavrsniDatum() 
    { 
        return zavrsniDatum; 
    }
    
    public LocalTime getVrijemeKretanja() 
    { 
        return vrijemeKretanja; 
    }
    
    public LocalTime getVrijemePovratka() 
    { 
        return vrijemePovratka; 
    }
    
    public float getCijena() 
    { 
        return cijena; 
    }
    
    public int getMinBrojPutnika() 
    { 
        return minBrojPutnika; 
    }
    
    public int getMaksBrojPutnika() 
    { 
        return maksBrojPutnika; 
    }
    
    public int getBrojNocenja() 
    { 
        return brojNocenja; 
    }
    
    public float getDoplataSobe() 
    { 
        return doplataSobe; 
    }
    
    public String getPrijevoz() 
    { 
        return prijevoz; 
    }
    
    public int getBrojDorucka() 
    { 
        return brojDorucka; 
    }
    
    public int getBrojRucka() 
    { 
        return brojRucka; 
    }
    
    public int getBrojVecera() 
    { 
        return brojVecera; 
    }

    public AranzmaniState getStatus() 
    { 
        return status; 
    }
    
    public void setStatus(AranzmaniState status) 
    { 
        this.status = status; 
    }
    
    public String getStatusNaziv() 
    {
        return status.getNaziv();
    }
}
