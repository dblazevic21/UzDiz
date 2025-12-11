package edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.unizg.foi.uzdiz.dblazevic21.app.composite.TuristickiElement;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani.AranzmaniState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani.UPripremiConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.AktivnaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.NaCekanjuConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OtkazanaConcreteState;

public class Aranzmani implements TuristickiElement
{
    private int oznaka;
    private String naziv;
    private String program;
    private LocalDate pocetniDatum;
    private LocalDate zavrsniDatum;
    private LocalTime vrijemeKretanja;
    private LocalTime vrijemePovratka;
    private float cijena;
    private int minBrojPutnika;
    private int maksBrojPutnika;
    private int brojNocenja;
    private float doplataSobe;
    private String prijevoz;
    private int brojDorucka;
    private int brojRucka;
    private int brojVecera;

    private final List<Rezervacija> rezervacije;
    private AranzmaniState status;
    private long brojacRezervacija = 0;

    private static final Comparator<LocalDateTime> LDT_ORDER = 
        Comparator.nullsLast(Comparator.<LocalDateTime>naturalOrder());

    public Aranzmani() 
    {
        this.rezervacije = new ArrayList<>();
        this.status = new UPripremiConcreteState();
    }

    public Aranzmani(int oznaka, String naziv, String program,
                     LocalDate pocetniDatum, LocalDate zavrsniDatum,
                     LocalTime vrijemeKretanja, LocalTime vrijemePovratka,
                     float cijena, int minBrojPutnika, int maksBrojPutnika,
                     int brojNocenja, float doplataSobe, String prijevoz,
                     int brojDorucka, int brojRucka, int brojVecera)
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
        this.rezervacije = new ArrayList<>();
        this.status = new UPripremiConcreteState();
    }

    public void setOznaka(int oznaka) 
    {
        this.oznaka = oznaka;
    }

    public void setNaziv(String naziv) 
    {
        this.naziv = naziv;
    }

    public void setProgram(String program) 
    {
        this.program = program;
    }

    public void setPocetniDatum(LocalDate pocetniDatum) 
    {
        this.pocetniDatum = pocetniDatum;
    }

    public void setZavrsniDatum(LocalDate zavrsniDatum) 
    {
        this.zavrsniDatum = zavrsniDatum;
    }

    public void setVrijemeKretanja(LocalTime vrijemeKretanja) 
    {
        this.vrijemeKretanja = vrijemeKretanja;
    }

    public void setVrijemePovratka(LocalTime vrijemePovratka) 
    {
        this.vrijemePovratka = vrijemePovratka;
    }

    public void setCijena(float cijena) 
    {
        this.cijena = cijena;
    }

    public void setMinBrojPutnika(int minBrojPutnika) 
    {
        this.minBrojPutnika = minBrojPutnika;
    }

    public void setMaksBrojPutnika(int maksBrojPutnika) 
    {
        this.maksBrojPutnika = maksBrojPutnika;
    }

    public void setBrojNocenja(int brojNocenja) 
    {
        this.brojNocenja = brojNocenja;
    }

    public void setDoplataSobe(float doplataSobe) 
    {
        this.doplataSobe = doplataSobe;
    }

    public void setPrijevoz(String prijevoz) 
    {
        this.prijevoz = prijevoz;
    }

    public void setBrojDorucka(int brojDorucka) 
    {
        this.brojDorucka = brojDorucka;
    }

    public void setBrojRucka(int brojRucka) 
    {
        this.brojRucka = brojRucka;
    }

    public void setBrojVecera(int brojVecera) 
    {
        this.brojVecera = brojVecera;
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

    public List<Rezervacija> getRezervacije() 
    {
        return new ArrayList<>(rezervacije);
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

    @Override
    public String getOpis() 
    {
        return "Aranžman: " + naziv + " [" + status.getNaziv() + "]";
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
        Rezervacija r = new Rezervacija(++brojacRezervacija, ime, prezime, oznaka, datumVrijeme);
        rezervacije.add(r);
        azurirajStatuseRezervacija();
        status.azuriraj(this, getBrojAktivnihRezervacija());
    }

    public void dodajRezervaciju(String ime, String prezime, String datumVrijemeRaw)
    {
        Rezervacija r = new Rezervacija(++brojacRezervacija, ime, prezime, oznaka, datumVrijemeRaw);
        rezervacije.add(r);
        azurirajStatuseRezervacija();
        status.azuriraj(this, getBrojAktivnihRezervacija());
    }

    public boolean imaRezervaciju(String ime, String prezime)
    {
        String i = (ime == null) ? "" : ime.trim();
        String p = (prezime == null) ? "" : prezime.trim();

        return rezervacije.stream()
                .anyMatch(r -> equalsIgnorirajCase(r.getIme(), i)
                        && equalsIgnorirajCase(r.getPrezime(), p)
                        && !(r.getStatus() instanceof OtkazanaConcreteState));
    }

    public boolean ukloniRezervaciju(String ime, String prezime)
    {
        String i = (ime == null) ? "" : ime.trim();
        String p = (prezime == null) ? "" : prezime.trim();

        Rezervacija cilj = rezervacije.stream()
                .filter(r -> equalsIgnorirajCase(r.getIme(), i)
                        && equalsIgnorirajCase(r.getPrezime(), p)
                        && !(r.getStatus() instanceof OtkazanaConcreteState))
                .min(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .orElse(null);

        if (cilj == null) return false;

        boolean bilaAktivna = cilj.getStatus() instanceof AktivnaConcreteState;

        cilj.otkazi();

        if (bilaAktivna)
        {
            Rezervacija naCekanju = rezervacije.stream()
                    .filter(r -> r.getStatus() instanceof NaCekanjuConcreteState)
                    .min(Comparator
                            .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                            .thenComparingLong(Rezervacija::getRedniBroj))
                    .orElse(null);

            if (naCekanju != null)
            {
                naCekanju.aktiviraj();
            }
        }

        status.azuriraj(this, getBrojAktivnihRezervacija());
        return true;
    }

    private void azurirajStatuseRezervacija()
    {
        List<Rezervacija> aktivne = rezervacije.stream()
                .filter(r -> !(r.getStatus() instanceof OtkazanaConcreteState))
                .sorted(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .toList();

        int count = 0;
        for (Rezervacija r : aktivne)
        {
            if (aktivne.size() < minBrojPutnika)
            {
                r.obradi();
            }
            else if (count < maksBrojPutnika)
            {
                r.aktiviraj();
                count++;
            }
            else
            {
                r.staviNaCekanje();
            }
        }
    }

    private int getBrojAktivnihRezervacija()
    {
        return (int) rezervacije.stream()
                .filter(r -> r.getStatus() instanceof AktivnaConcreteState)
                .count();
    }

    private boolean equalsIgnorirajCase(String rijec1, String rijec2)
    {
        String prvaRijec = (rijec1 == null) ? "" : rijec1.trim();
        String drugaRijec = (rijec2 == null) ? "" : rijec2.trim();
        return prvaRijec.equalsIgnoreCase(drugaRijec);
    }
}
