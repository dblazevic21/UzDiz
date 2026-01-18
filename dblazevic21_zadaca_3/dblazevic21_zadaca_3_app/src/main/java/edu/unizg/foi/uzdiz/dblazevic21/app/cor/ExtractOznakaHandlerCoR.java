package edu.unizg.foi.uzdiz.dblazevic21.app.cor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractOznakaHandlerCoR extends BaseHandlerCoR 
{
	private static final Pattern DRTA_PATTERN = Pattern.compile(
	        "^DRTA\\s+(.+?)\\s+(.+?)\\s+(\\d+)\\s+([\\d]{1,2}\\.[\\d]{1,2}\\.[\\d]{4})\\.?\\s+(\\d{1,2}:\\d{2}(?::\\d{2})?)$",
	        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
	    );

	    @Override
	    public void handle(KomandaContext context)
	    {
	        try
	        {
	            String unos = context.unos.trim();
	            String naziv = context.nazivKomande;

	            if ("DRTA".equalsIgnoreCase(naziv))
	            {
	                Matcher m = DRTA_PATTERN.matcher(unos);
	                if (!m.matches())
	                {
	                    System.out.println("Neispravan format DRTA. Očekivano: DRTA ime prezime oznaka datum vrijeme");
	                    return;
	                }
	                context.oznakaAranzmana = Integer.parseInt(m.group(3));
	            }
	            else
	            {
	                String[] dijelovi = unos.split("\\s+");
	                context.oznakaAranzmana = Integer.parseInt(dijelovi[dijelovi.length - 1]);
	            }
	        }
	        catch (Exception e)
	        {
	            System.out.println("Neispravna ili nedostajuća oznaka aranžmana.");
	            return;
	        }

	        sljedeci(context);
	    }
}
