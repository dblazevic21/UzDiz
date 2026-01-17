package edu.unizg.foi.uzdiz.dblazevic21.app.component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;

public class StatsKomandaConcreteDecorator extends AnalitikaRezervacijeDecorator
{
	private final List<Rezervacija> rezervacije;
	
	public StatsKomandaConcreteDecorator(TuristickiElementComponent component, List<Rezervacija> rezervacije) 
	{
        super(component);
        this.rezervacije = rezervacije;
    }
	
	public void prikaziStatistiku(LocalDate od, LocalDate doDatuma) 
	{
        List<Rezervacija> filtrirane = rezervacije;
        if (od != null && doDatuma != null) 
        {
            filtrirane = rezervacije.stream()
                .filter(r -> {
                    LocalDate d = r.getDatumVrijeme().toLocalDate();
                    return !d.isBefore(od) && !d.isAfter(doDatuma);
                })
                .collect(Collectors.toList());
        }
        
        prikaziStatistiku(filtrirane);
    }
	
	public void prikaziStatistiku() 
	{
        prikaziStatistiku(rezervacije);
    }

	private void prikaziStatistiku(List<Rezervacija> lista)
	{
	    System.out.println("Analitika rezervacija: " + component.getOpis());
	    System.out.println();

	    if (lista.isEmpty())
	    {
	        System.out.println("Nema rezervacija za zadani kriterij.");
	        return;
	    }

	    int ukupno = lista.size();

	    Map<String, Long> poStatusu = lista.stream()
	        .collect(Collectors.groupingBy(
	            r -> r.getStatus().getNaziv(),
	            Collectors.counting()
	        ));

	    poStatusu.forEach((status, count) -> {
	        double postotak = (count * 100.0) / ukupno;
	        System.out.printf(
	            "%-10s %3.0f%% | %s%n",
	            status,
	            postotak,
	            graf(postotak)
	        );
	    });

	    System.out.println();

	    poStatusu.entrySet().stream()
	        .max(Map.Entry.comparingByValue())
	        .ifPresent(e ->
	            System.out.println(
	                "Najčešći status: " + e.getKey() + " (" + e.getValue() + ")"
	            )
	        );
	}
    
	private String graf(double postotak)
	{
	    int max = 20;
	    int punih = (int) Math.round((postotak / 100) * max);
	    return "█".repeat(punih);
	}
	
}
