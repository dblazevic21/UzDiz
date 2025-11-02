package edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani;

import java.time.LocalDate;
import java.time.LocalTime;

public class Aranzmani 
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
    
	public Aranzmani() {}
	
	public Aranzmani(
			int oznaka,
			String naziv, 
			String program, 
			LocalDate pocetniDatum, 
			LocalDate zavrsniDatum,
			LocalTime vrijemeKretanja, 
			LocalTime vrijemePovratka, 
			float cijena, 
			int minBrojPutnika, 
			int maksBrojPutnika,
			int brojNocenja, 
			float doplataSobe, 
			String prijevoz, 
			int brojDorucka, 
			int brojRucka, 
			int brojVecera
			) 
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
	}
	
	public int getOznaka() 
	{
		return oznaka;
	}
	
	public int setOznaka(int oznaka) 
	{
		return this.oznaka = oznaka;
	}
	
	public String getNaziv()
	{
		return naziv;
	}
	
	public String setNaziv(String naziv) 
	{
		return this.naziv = naziv;
	}
	
	public String getProgram() 
	{
		return program;
	}
	
	public String setProgram(String program) 
	{
		return this.program = program;
	}
	
	public LocalDate getPocetniDatum() 
	{
		return pocetniDatum;
	}
	
	public LocalDate setPocetniDatum(LocalDate pocetniDatum) 
	{
		return this.pocetniDatum = pocetniDatum;
	}
	
	public LocalDate getZavrsniDatum() 
	{
		return zavrsniDatum;
	}
	
	public LocalDate setZavrsniDatum(LocalDate zavrsniDatum) 
	{
		return this.zavrsniDatum = zavrsniDatum;
	}
	
	public LocalTime getVrijemeKretanja() 
	{
		return vrijemeKretanja;
	}
	
	public LocalTime setVrijemeKretanja(LocalTime vrijemeKretanja) 
	{
		return this.vrijemeKretanja = vrijemeKretanja;
	}
	
	public LocalTime getVrijemePovratka() 
	{
		return vrijemePovratka;
	}
	
	public LocalTime setVrijemePovratka(LocalTime vrijemePovratka) 
	{
		return this.vrijemePovratka = vrijemePovratka;
	}
	
	public float getCijena() 
	{
		return cijena;
	}
	
	public float setCijena(float cijena) 
	{
		return this.cijena = cijena;
	}
	
	public int getMinBrojPutnika() 
	{
		return minBrojPutnika;
	}
	
	public int setMinBrojPutnika(int minBrojPutnika) 
	{
		return this.minBrojPutnika = minBrojPutnika;
	}
	
	public int getMaksBrojPutnika() 
	{
		return maksBrojPutnika;
	}
	
	public int setMaksBrojPutnika(int maksBrojPutnika) 
	{
		return this.maksBrojPutnika = maksBrojPutnika;
	}
	
	public int getBrojNocenja() 
	{
		return brojNocenja;
	}
	
	public int setBrojNocenja(int brojNocenja) 
	{
		return this.brojNocenja = brojNocenja;
	}
	
	public float getDoplataSobe() 
	{
		return doplataSobe;
	}
	
	public float setDoplataSobe(float doplataSobe) 
	{
		return this.doplataSobe = doplataSobe;
	}
	
	public String getPrijevoz() 
	{
		return prijevoz;
	}
	
	public String setPrijevoz(String prijevoz) 
	{
		return this.prijevoz = prijevoz;
	}
	
	public int getBrojDorucka() 
	{
		return brojDorucka;
	}
	
	public int setBrojDorucka(int brojDorucka) 
	{
		return this.brojDorucka = brojDorucka;
	}
	
	public int getBrojRucka() 
	{
		return brojRucka;
	}
	
	public int setBrojRucka(int brojRucka) 
	{
		return this.brojRucka = brojRucka;
	}
	
	public int getBrojVecera() 
	{
		return brojVecera;
	}
	
	public int setBrojVecera(int brojVecera) 
	{
		return this.brojVecera = brojVecera;
	}
}
