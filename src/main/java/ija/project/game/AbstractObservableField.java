package ija.project.game;

import ija.project.common.Field;

import java.util.HashSet;
import java.util.Set;

/**
 * @authors Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 * @brief Abstract class representing observable field. Contains implementation for adding, removing and notifying observers.
 */
public abstract class AbstractObservableField implements Field {
    /**
     * Set of observers. Observers are notified when the field is changed.
     */
    private final Set<Observer> observers = new HashSet<>();
    /**
     * These observers are notified when the field is changed and the change is logged.
     */
    private final Set<Observer> logObservers = new HashSet<>();

    public AbstractObservableField() {
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

    public void notifyLogObservers(){this.logObservers.forEach((o) -> o.update(this));}
}
