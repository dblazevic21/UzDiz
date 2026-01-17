package edu.unizg.foi.uzdiz.dblazevic21.app.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.rezervacije.Rezervacija;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.AktivnaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OdgodenaConcreteState;
import edu.unizg.foi.uzdiz.dblazevic21.app.statusi.rezervacije.OtkazanaConcreteState;

public class VdrStrategija implements UpravljanjeRezervacijamaStrategija
{
	@Override
    public void primijeni(Map<Integer, Aranzmani> aranzmani) 
	{

    }
	
	@Override
    public void primijeniNakonKapaciteta(Map<Integer, Aranzmani> aranzmani)
    {
        for (Aranzmani a : aranzmani.values())
        {
            int max = a.getMaksBrojPutnika();
            int limit = max / 4;
            if (limit < 1) limit = 1;

            Map<String, List<Rezervacija>> aktivnePoOsobi = new HashMap<>();

            for (var dijete : a.getDjeca())
            {
                Rezervacija r = (Rezervacija) dijete;

                if (r.getStatus() instanceof OtkazanaConcreteState) continue;

                if (!(r.getStatus() instanceof AktivnaConcreteState)) continue;

                String key = IntervalStrategija.keyOsobe(r);
                aktivnePoOsobi.computeIfAbsent(key, k -> new ArrayList<>()).add(r);
            }
            
            for (List<Rezervacija> lista : aktivnePoOsobi.values())
            {
                lista.sort(Comparator.comparing(Rezervacija::getDatumVrijeme, Comparator.nullsLast(Comparator.naturalOrder()))
                                     .thenComparingLong(Rezervacija::getRedniBroj));

                for (int i = limit; i < lista.size(); i++)
                {
                    lista.get(i).setStatus(new OdgodenaConcreteState());
                }
            }
        }
    }

    @Override
    public String naziv()
    {
        return "VDR";
    }
}
