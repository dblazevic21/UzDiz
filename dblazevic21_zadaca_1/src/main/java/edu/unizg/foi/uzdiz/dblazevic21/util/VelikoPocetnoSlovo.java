package edu.unizg.foi.uzdiz.dblazevic21.util;

public class VelikoPocetnoSlovo 
{
	public static String velikoPocetnoSlovo(String input) 
	{
	    if (input == null || input.isEmpty()) 
	    {
	        return input;
	    }
	    return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
	}
}
