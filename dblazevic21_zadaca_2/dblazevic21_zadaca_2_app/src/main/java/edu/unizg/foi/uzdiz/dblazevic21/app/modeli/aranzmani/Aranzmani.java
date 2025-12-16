package edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.unizg.foi.uzdiz.dblazevic21.app.component.TuristickiElementComponent;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani.AktivanConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani.AranzmaniState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani.PopunjenConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani.UPripremiConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.AktivnaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.NaCekanjuConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OdgodenaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OtkazanaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.PrimljenaConcreteState;

public class Aranzmani implements TuristickiElementComponent
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
    private AranzmaniState status;

    private final List<Rezervacija> rezervacije = new ArrayList<>();

    private static final Comparator<LocalDateTime> LDT_ORDER =
            Comparator.nullsLast(Comparator.<LocalDateTime>naturalOrder());

    public Aranzmani()
    {
        this.status = new UPripremiConcreteState();
    }

    @Override
    public String getOpis()
    {
        return oznaka + " - " + naziv + " (" + pocetniDatum + " - " + zavrsniDatum + ") [" +
               (status != null ? status.getNaziv() : "") + "]";
    }

    @Override
    public int getBrojOsoba()
    {
        return (int) rezervacije.stream()
                .filter(r -> !(r.getStatus() instanceof OtkazanaConcreteState))
                .count();
    }

    @Override
    public List<TuristickiElementComponent> getDjeca()
    {
        return new ArrayList<>(rezervacije);
    }

    @Override
    public void dodajDijete(TuristickiElementComponent element)
    {
        if (element instanceof Rezervacija)
        {
            rezervacije.add((Rezervacija) element);
        }
    }

    @Override
    public void ukloniDijete(TuristickiElementComponent element)
    {
        if (element instanceof Rezervacija)
        {
            rezervacije.remove(element);
        }
    }

    @Override
    public boolean isLeaf()
    {
        return false;
    }

    public void dodajRezervaciju(Rezervacija rezervacija)
    {
        rezervacije.add(rezervacija);
    }

    public List<Rezervacija> getRezervacije()
    {
        return new ArrayList<>(rezervacije);
    }

    public List<Rezervacija> getRezervacijeSortirane()
    {
        return rezervacije.stream()
                .sorted(Comparator
                        .comparing(Rezervacija::getDatumVrijeme, LDT_ORDER)
                        .thenComparingLong(Rezervacija::getRedniBroj))
                .collect(Collectors.toList());
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

    public int getBrojAktivnihRezervacija()
    {
        return (int) rezervacije.stream()
                .filter(r -> !(r.getStatus() instanceof OtkazanaConcreteState))
                .count();
    }

    public void azurirajStatuseRezervacija()
    {
        List<Rezervacija> aktivneRezervacije = getAktivneRezervacije();
        int brojRezervacija = aktivneRezervacije.size();

        if (brojRezervacija < minBrojPutnika)
        {
            for (Rezervacija rezervacija : aktivneRezervacije)
            {
                rezervacija.setStatus(new PrimljenaConcreteState());
            }
        }
        else if (brojRezervacija <= maksBrojPutnika)
        {
            for (Rezervacija rezervacija : aktivneRezervacije)
            {
                rezervacija.setStatus(new AktivnaConcreteState());
            }
        }
        else
        {
            int aktivniCount = 0;
            for (Rezervacija rezervacija : aktivneRezervacije)
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

        if (status != null)
        {
            status.azuriraj(this, brojRezervacija);
        }
    }
    
    public int getBrojJedinstvenihAktivnihRezervacija()
    {
        return (int) rezervacije.stream()
            .filter(r -> !(r.getStatus() instanceof OtkazanaConcreteState)
                      && !(r.getStatus() instanceof OdgodenaConcreteState))
            .map(r -> r.getIme() + "|" + r.getPrezime() + "|" + r.getDatumVrijeme())
            .distinct()
            .count();
    }

    public void rekalkulirajStatus() 
    {
        int brojAktivnih = getBrojJedinstvenihAktivnihRezervacija();

        if (brojAktivnih < minBrojPutnika)
        {
            setStatus(new UPripremiConcreteState());
        }
        else if (brojAktivnih <= maksBrojPutnika) 
        {
            setStatus(new AktivanConcreteState());
        } 
        else 
        {
            setStatus(new PopunjenConcreteState());
        }
    }

    public void otkaziAranzman()
    {
        if (status != null)
        {
            status.otkazi(this);
        }
    }
    
    public void ocistiRezervacije() 
    {
        this.rezervacije.clear();
    }

    public void resetirajStatusRezervacija()
    {
        this.rezervacije.clear();
    }

    public int getOznaka()
    {
        return oznaka;
    }

    public void setOznaka(int oznaka)
    {
        this.oznaka = oznaka;
    }

    public String getNaziv()
    {
        return naziv;
    }

    public void setNaziv(String naziv)
    {
        this.naziv = naziv;
    }

    public String getProgram()
    {
        return program;
    }

    public void setProgram(String program)
    {
        this.program = program;
    }

    public LocalDate getPocetniDatum()
    {
        return pocetniDatum;
    }

    public void setPocetniDatum(LocalDate pocetniDatum)
    {
        this.pocetniDatum = pocetniDatum;
    }

    public LocalDate getZavrsniDatum()
    {
        return zavrsniDatum;
    }

    public void setZavrsniDatum(LocalDate zavrsniDatum)
    {
        this.zavrsniDatum = zavrsniDatum;
    }

    public LocalTime getVrijemeKretanja()
    {
        return vrijemeKretanja;
    }

    public void setVrijemeKretanja(LocalTime vrijemeKretanja)
    {
        this.vrijemeKretanja = vrijemeKretanja;
    }

    public LocalTime getVrijemePovratka()
    {
        return vrijemePovratka;
    }

    public void setVrijemePovratka(LocalTime vrijemePovratka)
    {
        this.vrijemePovratka = vrijemePovratka;
    }

    public float getCijena()
    {
        return cijena;
    }

    public void setCijena(float cijena)
    {
        this.cijena = cijena;
    }

    public int getMinBrojPutnika()
    {
        return minBrojPutnika;
    }

    public void setMinBrojPutnika(int minBrojPutnika)
    {
        this.minBrojPutnika = minBrojPutnika;
    }

    public int getMaksBrojPutnika()
    {
        return maksBrojPutnika;
    }

    public void setMaksBrojPutnika(int maksBrojPutnika)
    {
        this.maksBrojPutnika = maksBrojPutnika;
    }

    public int getBrojNocenja()
    {
        return brojNocenja;
    }

    public void setBrojNocenja(int brojNocenja)
    {
        this.brojNocenja = brojNocenja;
    }

    public float getDoplataSobe()
    {
        return doplataSobe;
    }

    public void setDoplataSobe(float doplataSobe)
    {
        this.doplataSobe = doplataSobe;
    }

    public String getPrijevoz()
    {
        return prijevoz;
    }

    public void setPrijevoz(String prijevoz)
    {
        this.prijevoz = prijevoz;
    }

    public int getBrojDorucka()
    {
        return brojDorucka;
    }

    public void setBrojDorucka(int brojDorucka)
    {
        this.brojDorucka = brojDorucka;
    }

    public int getBrojRucka()
    {
        return brojRucka;
    }

    public void setBrojRucka(int brojRucka)
    {
        this.brojRucka = brojRucka;
    }

    public int getBrojVecera()
    {
        return brojVecera;
    }

    public void setBrojVecera(int brojVecera)
    {
        this.brojVecera = brojVecera;
    }

    public AranzmaniState getStatus()
    {
        return status;
    }

    public void setStatus(AranzmaniState status)
    {
        this.status = status;
    }
}
