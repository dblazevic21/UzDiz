package edu.unizg.foi.uzdiz.dblazevic21.interfaces;

import edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani.Aranzmani;
import java.time.LocalDate;
import java.time.LocalTime;


public interface AranzmaniBuilder
{
	public AranzmaniBuilder kreirajAranzmane(
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
			);
	
	public AranzmaniBuilder setProgram(String program);
	public AranzmaniBuilder setPocetniDatum(LocalDate pocetniDatum);
	public AranzmaniBuilder setZavrsniDatum(LocalDate zavrsniDatum);
	public AranzmaniBuilder setCijena(float cijena);
	public AranzmaniBuilder setMinBrojPutnika(int minBrojPutnika);
	public AranzmaniBuilder setMaksBrojPutnika(int maksBrojPutnika);
	public AranzmaniBuilder setBrojNocenja(int brojNocenja);
	public AranzmaniBuilder setDoplataSobe(float doplataSobe);
	public AranzmaniBuilder setPrijevoz(String prijevoz);
	public AranzmaniBuilder setBrojDorucka(int brojDorucka);
	public AranzmaniBuilder setBrojRucka(int brojRucka);
	public AranzmaniBuilder setBrojVecera(int brojVecera);
	public Aranzmani getAranzman();
	
}
