package edu.unizg.foi.uzdiz.dblazevic21.app.memento;

import java.util.HashMap;
import java.util.Map;

public class AranzmanCaretaker 
{
    private final Map<Integer, AranzmanMemento> storage = new HashMap<>();

    public boolean store(int oznaka, AranzmanMemento memento) 
    {
        if (storage.containsKey(oznaka)) return false; 
        storage.put(oznaka, memento);
        return true;
    }

    public AranzmanMemento get(int oznaka) 
    {
        return storage.get(oznaka);
    }

    public boolean remove(int oznaka) 
    {
        return storage.remove(oznaka) != null;
    }

    public boolean contains(int oznaka) 
    {
        return storage.containsKey(oznaka);
    }
}
