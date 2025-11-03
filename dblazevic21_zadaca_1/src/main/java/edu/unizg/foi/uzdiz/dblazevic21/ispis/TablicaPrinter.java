package edu.unizg.foi.uzdiz.dblazevic21.ispis;

import java.util.ArrayList;
import java.util.List;

public class TablicaPrinter {

    public static void printajSeperatorTabliceMulti(int[] widths) 
    {
        StringBuilder sb = new StringBuilder("+");
        for (int w : widths) 
        {
            sb.append("-".repeat(w + 2)).append("+");
        }
        System.out.println(sb);
    }

    public static void printajRedTabliceMulti(int[] widths, String... cells) 
    {
        StringBuilder fmt = new StringBuilder("|");
        for (int w : widths)
        {
    		fmt.append(" %-" + w + "s |");
        }
        fmt.append("%n");

        Object[] out = new Object[widths.length];
        
        for (int i = 0; i < widths.length; i++) 
        {
            String v = (i < cells.length && cells[i] != null && !cells[i].isBlank()) ? cells[i] : "-";
            out[i] = v;
        }
        System.out.printf(fmt.toString(), out);
    }

    public static List<String> wrapajTekst(String text, int width) 
    {
        List<String> out = new ArrayList<>();
        
        if (text == null) 
        {
            out.add("-");
            return out;
        }

        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();

        for (String w : words) 
        {
            if (line.length() + w.length() + 1 > width) 
            {
                out.add(line.toString());
                line.setLength(0);
            }
            if (line.length() > 0)
            {
            	line.append(" ");
            }
            line.append(w);
        }

        if (line.length() > 0)
        {
        	out.add(line.toString()); 
        }
        return out;
    }

    public static void printajTablicuZaglavlje(int[] sirine, boolean sOtkazano) 
    {
        printajSeperatorTabliceMulti(sirine);
        if (sOtkazano)
        {
        	printajRedTabliceMulti(sirine, "Ime", "Prezime", "Datum i vrijeme", "Vrsta", "Otkazano");
        }
        else
        {
        	printajRedTabliceMulti(sirine, "Ime", "Prezime", "Datum i vrijeme", "Vrsta");
        }
        printajSeperatorTabliceMulti(sirine);
    }

    public static void printajTablicuRedak(int[] sirine, String... vrijednosti) 
    {
        printajRedTabliceMulti(sirine, vrijednosti);
    }

    public static void printajTablicuZatvaranje(int[] sirine) 
    {
        printajSeperatorTabliceMulti(sirine);
    }
    
    public static void printajSeperatorTablice(int w1, int w2)
    {
        StringBuilder sb = new StringBuilder();
        sb.append('+');
        sb.append("-".repeat(w1 + 2));
        sb.append('+');
        sb.append("-".repeat(w2 + 2));
        sb.append('+');
        System.out.println(sb);
    }

    public static void printajRedTablice(String key, String value, int w1, int w2)
    {
        String v = (value == null || value.isBlank()) ? "-" : value;
        List<String> lines = wrapajTekst(v, w2);
        String firstKey = (key == null) ? "-" : key;

        for (int i = 0; i < lines.size(); i++)
        {
            String k = (i == 0) ? firstKey : "";
            System.out.printf("| %-" + w1 + "s | %-" + w2 + "s |%n", k, lines.get(i));
        }
    }
    
}
