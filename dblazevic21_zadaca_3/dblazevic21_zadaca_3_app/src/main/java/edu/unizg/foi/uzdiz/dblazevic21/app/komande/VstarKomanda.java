package edu.unizg.foi.uzdiz.dblazevic21.app.komande;

import java.util.Map;

import edu.unizg.foi.uzdiz.dblazevic21.app.command.VstarReceiver;
import edu.unizg.foi.uzdiz.dblazevic21.app.ispis.TablicaPrinter;
import edu.unizg.foi.uzdiz.dblazevic21.app.memento.AranzmanCaretaker;
import edu.unizg.foi.uzdiz.dblazevic21.app.modeli.aranzmani.Aranzmani;

public class VstarKomanda implements Komanda 
{
    private final Map<Integer, Aranzmani> aranzmani;
    private final AranzmanCaretaker caretaker;

    public VstarKomanda(Map<Integer, Aranzmani> aranzmani, AranzmanCaretaker caretaker) {
        this.aranzmani = aranzmani;
        this.caretaker = caretaker;
    }

    @Override
    public String getNaziv() { return "VSTAR"; }

    @Override
    public void izvrsi(String unos) {
        String s = (unos == null) ? "" : unos.trim();
        String[] dijelovi = s.split("\\s+");

        if (dijelovi.length != 2) {
            System.out.println("Greška: Očekivano: VSTAR oznaka");
            return;
        }

        TablicaPrinter.ispisUnosa(unos);

        int oznaka;
        try {
            oznaka = Integer.parseInt(dijelovi[1]);
        } catch (Exception e) {
            System.out.println("Greška: oznaka mora biti broj.");
            return;
        }

        VstarReceiver receiver = new VstarReceiver(aranzmani, caretaker);
        System.out.println(receiver.vrati(oznaka));
    }
}
