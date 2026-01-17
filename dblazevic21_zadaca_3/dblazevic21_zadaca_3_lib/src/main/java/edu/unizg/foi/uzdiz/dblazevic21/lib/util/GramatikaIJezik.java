package edu.unizg.foi.uzdiz.dblazevic21.lib.util;

public class GramatikaIJezik 
{
	public static String velikoPocetnoSlovo(String ulaz) 
	{
	    if (ulaz == null || ulaz.isEmpty()) 
	    {
	        return ulaz;
	    }
	    return ulaz.substring(0, 1).toUpperCase() + ulaz.substring(1).toLowerCase();
	}
	
	public static String makniNavodnike(String s) 
	{
	    if (s == null) 
	    {
	    	return null;
    	}
	    String t = s.trim();
	    
	    if (t.length() >= 2 && t.startsWith("\"") && t.endsWith("\"")) 
	    {
	        t = t.substring(1, t.length() - 1);
	    }
	    return t;
	}
}
