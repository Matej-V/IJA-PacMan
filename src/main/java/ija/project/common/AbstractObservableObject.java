package ija.project.common;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractObservableObject implements  MazeObject{
    private final Set<Observable.Observer> observers = new HashSet<>();
    private final Set<Observable.Observer> logObservers = new HashSet<>();

    public AbstractObservableObject() {
    }

    public void addObserver(Observable.Observer o) {
        this.observers.add(o);
    }

    public void removeObserver(Observable.Observer o) {
        this.observers.remove(o);
    }

    public void notifyObservers() {
        this.observers.forEach((o) -> o.update(this));
    }

    public void addLogObserver(Observable.Observer o) {
        this.logObservers.add(o);
    }

    public void removeLogObserver(Observable.Observer o) {this.logObservers.remove(o);}

    public void notifyLogObservers(){this.logObservers.forEach((o) -> o.update(this));}
}
