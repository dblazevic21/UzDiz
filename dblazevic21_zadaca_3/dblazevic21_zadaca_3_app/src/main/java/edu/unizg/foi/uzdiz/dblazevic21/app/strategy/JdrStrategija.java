package edu.unizg.foi.uzdiz.dblazevic21.app.strategy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OdgodenaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OtkazanaConcreteState;

public class JdrStrategija implements UpravljanjeRezervacijamaStrategija
{
	@Override
	public void primijeni(Map<Integer, Aranzmani> aranzmani)
	{
		Map<String, List<Rezervacija>> poOsobi = new HashMap<>();
		
		for (Aranzmani a : aranzmani.values()) 
		{
	        for (var dijete : a.getDjeca()) 
	        {
	            Rezervacija r = (Rezervacija) dijete;
	            if (r.getStatus() instanceof OtkazanaConcreteState) continue;

	            String key = IntervalStrategija.keyOsobe(r);
	            poOsobi.computeIfAbsent(key, k -> new ArrayList<>()).add(r);
	        }
	    }
		
		for (List<Rezervacija> lista : poOsobi.values()) {
            lista.sort(Comparator.comparing(Rezervacija::getDatumVrijeme, Comparator.nullsLast(Comparator.naturalOrder()))
                                 .thenComparingLong(Rezervacija::getRedniBroj));

            List<IntervalStrategija> dopusteni = new ArrayList<>();

            for (Rezervacija r : lista) {
            	IntervalStrategija intervalR = intervalZaRezervaciju(r, aranzmani);

                if (intervalR == null) continue;

                boolean preklapaSe = dopusteni.stream().anyMatch(x -> x.preklapaSe(intervalR));

                if (preklapaSe) {
                    r.setStatus(new OdgodenaConcreteState());
                } else {
                    dopusteni.add(intervalR);
                }
            }
        }
	}
	
	@Override
    public String naziv() 
	{
        return "JDR";
    }
	
	private static IntervalStrategija intervalZaRezervaciju(Rezervacija r, Map<Integer, Aranzmani> aranzmani) 
	{
        Aranzmani a = aranzmani.get(r.getOznakaAranzmana());
        if (a == null) return null;

        LocalDate pd = a.getPocetniDatum();
        LocalDate zd = a.getZavrsniDatum();
        if (pd == null || zd == null) return null;

        LocalTime vk = a.getVrijemeKretanja();
        LocalTime vp = a.getVrijemePovratka();

        LocalDateTime start = LocalDateTime.of(pd, vk != null ? vk : LocalTime.MIDNIGHT);
        LocalDateTime end   = LocalDateTime.of(zd, vp != null ? vp : LocalTime.MIDNIGHT);

        return new IntervalStrategija(start, end);
    }
}
