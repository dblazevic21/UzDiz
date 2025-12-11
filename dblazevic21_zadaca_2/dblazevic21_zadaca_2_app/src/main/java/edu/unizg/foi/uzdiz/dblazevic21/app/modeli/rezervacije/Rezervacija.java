package edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije;

import java.time.LocalDateTime;

import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.PrimljenaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.RezervacijeState;

public class Rezervacija 
{
    private final long redniBroj;
    private final String ime;
    private final String prezime;
    private final int oznakaAranzmana;
    private final LocalDateTime datumVrijeme;
    private final String datumVrijemeRaw;

    private RezervacijeState status;
    private LocalDateTime otkazanoAt;

    public Rezervacija(long redniBroj, String ime, String prezime, int oznakaAranzmana, LocalDateTime datumVrijeme) 
    {
        this.redniBroj = redniBroj;
        this.ime = ime;
        this.prezime = prezime;
        this.oznakaAranzmana = oznakaAranzmana;
        this.datumVrijeme = datumVrijeme;
        this.datumVrijemeRaw = null;
        this.status = new PrimljenaConcreteState();
    }

    public Rezervacija(long redniBroj, String ime, String prezime, int oznakaAranzmana, String datumVrijemeRaw) 
    {
        this.redniBroj = redniBroj;
        this.ime = ime;
        this.prezime = prezime;
        this.oznakaAranzmana = oznakaAranzmana;
        this.datumVrijeme = null;
        this.datumVrijemeRaw = datumVrijemeRaw;
        this.status = new PrimljenaConcreteState();
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
    public int getOznakaAranzmana() 
    { 
    	return oznakaAranzmana; 
    }
    public LocalDateTime getDatumVrijeme() 
    { 
    	return datumVrijeme; 
    }
    public String getDatumVrijemeRaw() 
    { 
    	return datumVrijemeRaw; 
    }

    public void obradi() 
    {
        status.obradi(this);
    }

    public void otkazi() 
    {
        status.otkazi(this);
        this.otkazanoAt = LocalDateTime.now();
    }

    public void aktiviraj() 
    {
        status.aktiviraj(this);
    }

    public void staviNaCekanje() 
    {
        status.staviNaCekanje(this);
    }

    public void odgodi() 
    {
        status.odgodi(this);
    }

    public void setStatus(RezervacijeState status) 
    {
        this.status = status;
    }

    public RezervacijeState getStatus() 
    {
        return status;
    }

    public String getStatusNaziv() 
    {
        return status.getNaziv();
    }

    public boolean isNova() 
    {
        return "NOVA".equals(status.getNaziv());
    }

    public boolean isPrimljena() 
    {
        return "PRIMLJENA".equals(status.getNaziv());
    }

    public boolean isAktivna() 
    {
        return "AKTIVNA".equals(status.getNaziv());
    }

    public boolean isNaCekanju() 
    {
        return "NA ČEKANJU".equals(status.getNaziv());
    }

    public boolean isOdgodena() 
    {
        return "ODGOĐENA".equals(status.getNaziv());
    }

    public boolean isOtkazana() 
    {
        return "OTKAZANA".equals(status.getNaziv());
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

