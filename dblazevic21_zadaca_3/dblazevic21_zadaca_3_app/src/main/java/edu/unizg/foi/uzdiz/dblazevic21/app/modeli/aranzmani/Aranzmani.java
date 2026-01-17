package edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.unizg.foi.uzdiz.dblazevic21.app.component.TuristickiElementComponent;
import edu.unizg.foi.uzdiz.dblazevic21.app.component.TuristickiVisitor;
import edu.unizg.foi.uzdiz.dblazevic21.app.memento.AranzmanMemento;
import edu.unizg.foi.uzdiz.dblazevic21.app.memento.AranzmanMemento.RezervacijaSnapshot;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.app.observer.TuristickiObserver;
import edu.unizg.foi.uzdiz.dblazevic21.app.observer.TuristickiSubject;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani.AktivanConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani.AranzmaniState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani.PopunjenConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.aranzmani.UPripremiConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.AktivnaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.NaCekanjuConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.NovaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OdgodenaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OtkazanaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.PrimljenaConcreteState;

public class Aranzmani implements TuristickiElementComponent, TuristickiSubject
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
    
    private final List<TuristickiObserver> pretplatnici = new ArrayList<>();

    private static final Comparator<LocalDateTime> LDT_ORDER =
            Comparator.nullsLast(Comparator.<LocalDateTime>naturalOrder());

    public Aranzmani()
    {
        this.status = new UPripremiConcreteState();
    }
    
    @Override
    public void attachObserver(TuristickiObserver observer) {
    	if (observer == null) return;

        if (!pretplatnici.contains(observer))
        {
            pretplatnici.add(observer);
        }
    }

    @Override
    public void detachObserver(TuristickiObserver observer) 
    {
    	if (observer == null) return;
        pretplatnici.remove(observer);
    }
    
    public void detachAllObservers() 
    {
    	pretplatnici.clear();
    }


    @Override
    public void notifyObservers(String message) 
    {
    	if (pretplatnici.isEmpty()) return;

        for (TuristickiObserver o : new ArrayList<>(pretplatnici))
        {
            try
            {
                o.update(getOznaka(), message);
            }
            catch (Exception e)
            {
            	
            }
        }
    }
    
    public int getBrojPretplata()
    {
        return pretplatnici.size();
    }

    public void kopirajPretplateIz(Aranzmani drugi)
    {
        if (drugi == null) return;
        this.pretplatnici.clear();
        this.pretplatnici.addAll(drugi.pretplatnici);
    }
    
    public List<TuristickiObserver> getPretplatniciKopija()
    {
        return new ArrayList<>(pretplatnici);
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
    	if (element == null) return;

        if (!(element instanceof Rezervacija))
        {
            throw new IllegalArgumentException("Aranzmani može imati djecu samo tipa Rezervacija.");
        }

        rezervacije.add((Rezervacija) element);
    }

    @Override
    public void ukloniDijete(TuristickiElementComponent element)
    {
    	if (element == null) return;

        if (!(element instanceof Rezervacija))
        {
            throw new IllegalArgumentException("Aranzmani može ukloniti djecu samo tipa Rezervacija.");
        }

        rezervacije.remove(element);
    }

    @Override
    public boolean isLeaf()
    {
        return false;
    }
    
    @Override
    public void accept(TuristickiVisitor visitor) 
    {
        visitor.visit(this);
        for (var dijete : getDjeca()) 
        {
            dijete.accept(visitor);
        }
    }


    public void dodajRezervaciju(Rezervacija rezervacija)
    {
    	dodajDijete(rezervacija);
    }

    public List<Rezervacija> getRezervacije()
    {
    	return getDjeca().stream()
    			.map(e -> (Rezervacija) e)
                .collect(Collectors.toList());
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
            .filter(r -> !(r.getStatus() instanceof OdgodenaConcreteState))
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
    	for (TuristickiElementComponent dijete : new ArrayList<>(getDjeca()))
        {
            ukloniDijete(dijete);
        }
    }

    public void resetirajStatusRezervacija()
    {
    	ocistiRezervacije();
    }
    
    public AranzmanMemento createMemento() 
    {
        List<AranzmanMemento.RezervacijaSnapshot> snaps = new ArrayList<>();

        for (var e : getDjeca()) 
        {
            Rezervacija r = (Rezervacija) e;
            String statusKljuc = (r.getStatus() != null) ? r.getStatus().getNaziv() : "";
            snaps.add(new AranzmanMemento.RezervacijaSnapshot(
                    r.getRedniBroj(),
                    r.getIme(),
                    r.getPrezime(),
                    r.getOznakaAranzmana(),
                    r.getDatumVrijeme(),
                    statusKljuc,
                    r.getOtkazanoAt()
            ));
        }

        String aStatusKljuc = (getStatus() != null) ? getStatus().getNaziv() : "";

        return new AranzmanMemento(
                getOznaka(), getNaziv(), getProgram(),
                getPocetniDatum(), getZavrsniDatum(),
                getVrijemeKretanja(), getVrijemePovratka(),
                getCijena(), getMinBrojPutnika(), getMaksBrojPutnika(),
                getBrojNocenja(), getDoplataSobe(), getPrijevoz(),
                getBrojDorucka(), getBrojRucka(), getBrojVecera(),
                aStatusKljuc,
                snaps
        );
    }
    
    public void restoreMemento(AranzmanMemento m)
    {
        if (m == null) return;

        this.setNaziv(m.naziv);
        this.setProgram(m.program);
        this.setPocetniDatum(m.pocetniDatum);
        this.setZavrsniDatum(m.zavrsniDatum);
        this.setVrijemeKretanja(m.vrijemeKretanja);
        this.setVrijemePovratka(m.vrijemePovratka);
        this.setCijena(m.cijena);
        this.setMinBrojPutnika(m.minBrojPutnika);
        this.setMaksBrojPutnika(m.maksBrojPutnika);
        this.setBrojNocenja(m.brojNocenja);
        this.setDoplataSobe(m.doplataSobe);
        this.setPrijevoz(m.prijevoz);
        this.setBrojDorucka(m.brojDorucka);
        this.setBrojRucka(m.brojRucka);
        this.setBrojVecera(m.brojVecera);

        this.ocistiRezervacije();

        for (RezervacijaSnapshot rs : m.rezervacije)
        {
            Rezervacija r = new Rezervacija(
                rs.redniBroj,
                rs.ime,
                rs.prezime,
                rs.oznakaAranzmana,
                rs.datumVrijeme
            );

            String sk = (rs.statusKljuc == null) ? "" : rs.statusKljuc.trim().toUpperCase();

            switch (sk)
            {
                case "AKTIVNA" -> r.setStatus(new AktivnaConcreteState());
                case "NA ČEKANJU" -> r.setStatus(new NaCekanjuConcreteState());
                case "ODGOĐENA" -> r.setStatus(new OdgodenaConcreteState());
                case "OTKAZANA" -> r.setStatus(new OtkazanaConcreteState());
                case "PRIMLJENA" -> r.setStatus(new PrimljenaConcreteState());
                default -> r.setStatus(new NovaConcreteState());
            }

            this.dodajRezervaciju(r);
        }
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
