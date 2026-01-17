package edu.unizg.foi.uzdiz.dblazevic21.app.ispis;

public class IspisKonfiguracija
{
    public enum NacinSortiranja
    {
        N,
        S 
    }
    
    private static NacinSortiranja nacin = NacinSortiranja.N;

    public static NacinSortiranja getNacin()
    {
        return nacin;
    }

    public static void setNacin(NacinSortiranja novi)
    {
        if (novi != null)
        {
            nacin = novi;
        }
    }

    public static boolean jeKronoloski()
    {
        return nacin == NacinSortiranja.N;
    }

    public static boolean jeObrnutoKronoloski()
    {
        return nacin == NacinSortiranja.S;
    }
}
