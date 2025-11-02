package edu.unizg.foi.uzdiz.dblazevic21.modeli.rezervacije;

import edu.unizg.foi.uzdiz.dblazevic21.enumeracije.StatusRezervacije;

import java.time.LocalDateTime;

public class Rezervacija 
{
    private final long redniBroj;
    private final String ime;
    private final String prezime;
    private final int oznakaAranzmana;
    private final LocalDateTime datumVrijeme;
    private final String datumVrijemeRaw;

    private StatusRezervacije status;
    private LocalDateTime otkazanoAt;

    public Rezervacija(long redniBroj, String ime, String prezime, int oznakaAranzmana, LocalDateTime datumVrijeme) 
    {
        this.redniBroj = redniBroj;
        this.ime = ime;
        this.prezime = prezime;
        this.oznakaAranzmana = oznakaAranzmana;
        this.datumVrijeme = datumVrijeme;
        this.datumVrijemeRaw = null;
        this.status = StatusRezervacije.PRIMLJENA;
    }

    public Rezervacija(long redniBroj, String ime, String prezime, int oznakaAranzmana, String datumVrijemeRaw) 
    {
        this.redniBroj = redniBroj;
        this.ime = ime;
        this.prezime = prezime;
        this.oznakaAranzmana = oznakaAranzmana;
        this.datumVrijeme = null;
        this.datumVrijemeRaw = datumVrijemeRaw;
        this.status = StatusRezervacije.PRIMLJENA;
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

    public StatusRezervacije getStatus() 
    { 
    	return status; 
    }
    public void setStatus(StatusRezervacije status) 
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
