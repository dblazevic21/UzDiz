package modeli.rezervacije;

import java.time.LocalDateTime;

public record Rezervacije(
		String ime_osobe, 
		String prezime_osobe, 
		String oznaka_aranzmana, 
		LocalDateTime prijema_rezervacije) {

}
