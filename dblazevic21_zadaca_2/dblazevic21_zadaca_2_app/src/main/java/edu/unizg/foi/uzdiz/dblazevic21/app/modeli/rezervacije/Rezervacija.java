package edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import edu.unizg.foi.uzdiz.dblazevic21.app.component.TuristickiElementComponent;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.FormaterZaIspise;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.NovaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.RezervacijeState;
import edu.unizg.foi.uzdiz.dblazevic21.app.utils.CsvParserApp;

public class Rezervacija implements TuristickiElementComponent
{
    private final long redniBroj;
    private final String ime;
    private final String prezime;
    private final int oznakaAranzmana;
    private LocalDateTime datumVrijeme;
    private RezervacijeState status;
    private LocalDateTime otkazanoAt;

    public Rezervacija(long redniBroj, String ime, String prezime, int oznakaAranzmana, LocalDateTime datumVrijeme)
    {
        this.redniBroj = redniBroj;
        this.ime = ime;
        this.prezime = prezime;
        this.oznakaAranzmana = oznakaAranzmana;
        this.datumVrijeme = datumVrijeme;
        this.status = new NovaConcreteState();
        this.otkazanoAt = null;
    }

    public Rezervacija(long redniBroj, String ime, String prezime, int oznakaAranzmana, String dtRaw)
    {
        this.redniBroj = redniBroj;
        this.ime = ime;
        this.prezime = prezime;
        this.oznakaAranzmana = oznakaAranzmana;
        this.datumVrijeme = CsvParserApp.uDatumVrijeme(dtRaw);
        this.status = new NovaConcreteState();
        this.otkazanoAt = null;
    }
    
    public String getDatumVrijemeRaw() 
    {
        if (datumVrijeme == null) 
        {
            return "";
        }
        return datumVrijeme.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rezervacija that = (Rezervacija) obj;
        return oznakaAranzmana == that.oznakaAranzmana &&
               Objects.equals(ime, that.ime) &&
               Objects.equals(prezime, that.prezime) &&
               Objects.equals(datumVrijeme, that.datumVrijeme);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(ime, prezime, oznakaAranzmana, datumVrijeme);
    }


    @Override
    public String getOpis() 
    {
        String dtStr = FormaterZaIspise.fmtDatumVrijeme(datumVrijeme);
        return ime + " " + prezime + " | " + dtStr + " | " + status.getNaziv();
    }

    @Override
    public int getBrojOsoba()
    {
        return 1;
    }

    @Override
    public List<TuristickiElementComponent> getDjeca()
    {
        return Collections.emptyList();
    }

    @Override
    public void dodajDijete(TuristickiElementComponent element)
    {
        throw new UnsupportedOperationException("Rezervacija je leaf - ne može imati djecu.");
    }

    @Override
    public void ukloniDijete(TuristickiElementComponent element)
    {
        throw new UnsupportedOperationException("Rezervacija je leaf - ne može imati djecu.");
    }

    @Override
    public boolean isLeaf()
    {
        return true;
    }

    public long getRedniBroj()
    {
        return redniBroj;
    }

    public String getIme()
    {
        return ime;
    }

    public String getPrezime()
    {
        return prezime;
    }
    
    public void setDatumVrijeme(LocalDateTime datumVrijeme) 
    {
        this.datumVrijeme = datumVrijeme;
    }

    public int getOznakaAranzmana()
    {
        return oznakaAranzmana;
    }

    public LocalDateTime getDatumVrijeme()
    {
        return datumVrijeme;
    }

    public RezervacijeState getStatus()
    {
        return status;
    }

    public void setStatus(RezervacijeState status)
    {
        this.status = status;
    }

    public LocalDateTime getOtkazanoAt()
    {
        return otkazanoAt;
    }

    public void setOtkazanoAt(LocalDateTime otkazanoAt)
    {
        this.otkazanoAt = otkazanoAt;
    }
}
