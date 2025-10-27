package edu.unizg.foi.uzdiz.dblazevic21.modeli.aranzmani;

import java.util.Date;

public record Aranzmani(
		String oznaka, 
		String naziv,
		String program, 
		Date pocetni_datum, 
		Date zavrsni_datum, 
		long vrijeme_kretanja, 
		long vrijeme_povratka, 
		float cijena) {
	
}
