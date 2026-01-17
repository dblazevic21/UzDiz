package edu.unizg.foi.uzdiz.dblazevic21.app.observer;

public interface TuristickiSubject 
{
	void attachObserver(TuristickiObserver observer);
	void detachObserver(TuristickiObserver observer);
	void notifyObservers(String message);
}
