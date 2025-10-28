package edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani;

import edu.unizg.foi.uzdiz.dblazevic21.interfaces.AranzmaniBuilder;
import java.time.LocalDate;
import java.time.LocalTime;


public class AranzmaniBuilderConcrete implements AranzmaniBuilder  
{
	protected Aranzmani aranzman;
	
	public AranzmaniBuilderConcrete() { }
	
	public AranzmaniBuilder kreirajAranzmane(
			String oznaka,
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
		this.aranzman = new Aranzmani();
		this.aranzman.setOznaka(oznaka);
		this.aranzman.setNaziv(naziv);
		this.aranzman.setProgram(program);
		this.aranzman.setPocetniDatum(pocetniDatum);
		this.aranzman.setZavrsniDatum(zavrsniDatum);
		this.aranzman.setVrijemeKretanja(vrijemeKretanja);
		this.aranzman.setVrijemePovratka(vrijemePovratka);
		this.aranzman.setCijena(cijena);
		this.aranzman.setMinBrojPutnika(minBrojPutnika);
		this.aranzman.setMaksBrojPutnika(maksBrojPutnika);
		this.aranzman.setBrojNocenja(brojNocenja);
		this.aranzman.setDoplataSobe(doplataSobe);
		this.aranzman.setPrijevoz(prijevoz);
		this.aranzman.setBrojDorucka(brojDorucka);
		this.aranzman.setBrojRucka(brojRucka);
		this.aranzman.setBrojVecera(brojVecera);
		return this;		
	}
	
	public AranzmaniBuilder setBrojDorucka(int brojDorucka) {
		this.aranzman.setBrojDorucka(brojDorucka);
		return this;
	}
	
	public AranzmaniBuilder setBrojNocenja(int brojNocenja) {
		this.aranzman.setBrojNocenja(brojNocenja);
		return this;
	}
	
	public AranzmaniBuilder setBrojRucka(int brojRucka) {
		this.aranzman.setBrojRucka(brojRucka);
		return this;
	}
	
	public AranzmaniBuilder setBrojVecera(int brojVecera) {
		this.aranzman.setBrojVecera(brojVecera);
		return this;
	}
	
	public AranzmaniBuilder setCijena(float cijena) {
		this.aranzman.setCijena(cijena);
		return this;
	}
	
	public AranzmaniBuilder setDoplataSobe(float doplataSobe) {
		this.aranzman.setDoplataSobe(doplataSobe);
		return this;
	}
	
	public AranzmaniBuilder setMaksBrojPutnika(int maksBrojPutnika) {
		this.aranzman.setMaksBrojPutnika(maksBrojPutnika);
		return this;
	}
	
	public AranzmaniBuilder setMinBrojPutnika(int minBrojPutnika) {
		this.aranzman.setMinBrojPutnika(minBrojPutnika);
		return this;
	}
	
	public AranzmaniBuilder setPocetniDatum(LocalDate pocetniDatum) {
		this.aranzman.setPocetniDatum(pocetniDatum);
		return this;
	}
	
	public AranzmaniBuilder setPrijevoz(String prijevoz) {
		this.aranzman.setPrijevoz(prijevoz);
		return this;
	}
	
	public AranzmaniBuilder setProgram(String program) {
		this.aranzman.setProgram(program);
		return this;
	}
	
	public AranzmaniBuilder setZavrsniDatum(LocalDate zavrsniDatum) {
		this.aranzman.setZavrsniDatum(zavrsniDatum);
		return this;
	}
	
	public Aranzmani getAranzman() {
		return this.aranzman;
	}
	
}
