package ija.project.game;

import ija.project.common.MazeObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract class representing observable object. Contains implementation for adding, removing and notifying observers.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public abstract class AbstractObservableObject implements MazeObject {
    /**
     * Set of observers. Observers are notified when the object is changed.
     */
    private final Set<Observer> observers = new HashSet<>();
    /**
     * These observers are notified when the object is changed and the change is
     * logged.
     */
    private final Set<Observer> logObservers = new HashSet<>();

    public AbstractObservableObject() {
    }

    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    public void removeObserver(Observer o) {
        this.observers.remove(o);
    }

    public void notifyObservers() {
        this.observers.forEach((o) -> o.update(this));
    }

    public void addLogObserver(Observer o) {
        this.logObservers.add(o);
    }

    public void removeLogObserver(Observer o) {
        this.logObservers.remove(o);
    }

    public void notifyLogObservers() {
        this.logObservers.forEach((o) -> o.update(this));
    }

    public List<Observer> getObservers() {
        return List.copyOf(this.observers);
    }

    public List<Observer> getLogObservers() {
        return List.copyOf(this.logObservers);
    }
}
