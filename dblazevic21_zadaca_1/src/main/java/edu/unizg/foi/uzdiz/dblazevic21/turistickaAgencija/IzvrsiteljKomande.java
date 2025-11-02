package edu.unizg.foi.uzdiz.dblazevic21.turistickaAgencija;

import edu.unizg.foi.uzdiz.dblazevic21.komande.Komanda;
import edu.unizg.foi.uzdiz.dblazevic21.komande.DrtaKomanda;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class IzvrsiteljKomande 
{
//    private final TuristickaAgencija agencija;
//    private final Map<String, Komanda> komande = new HashMap<>();
//
//    public KomandaExecutor(TuristickaAgencija agencija) 
//    {
//        this.agencija = agencija;
//        registrirajKomande();
//    }
//
//    private void registrirajKomande() 
//    {
//        List<Komanda> lista = List.of(
//            new DrtaKomanda(),
//            new OrtaKomanda(),
//            new IroKomanda(),
//            new IrtaKomanda(),
//            new ItakKomanda(),
//            new ItapKomanda()
//        );
//        for (Komanda k : lista)
//        {
//            komande.put(k.getNaziv().toUpperCase(), k);
//        }
//    }

//    public void izvrsi(String unos)
//    {
//        if (unos == null || unos.isBlank()) 
//        {
//            System.out.println("Neispravna komanda.");
//            return;
//        }
//
//        String[] dijelovi = unos.trim().split("\\s+", 2);
//        String kljuc = dijelovi[0].toUpperCase(Locale.ROOT);
//
//        Komanda komanda = komande.get(kljuc);
//        
//        if (komanda != null) 
//        {
//            komanda.izvrsi(unos, agencija);
//        } 
//        else 
//        {
//            System.out.println("Nepoznata komanda: " + kljuc);
//        }
//    }
}
