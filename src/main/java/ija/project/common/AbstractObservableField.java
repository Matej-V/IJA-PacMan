//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ija.project
        .common;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractObservableField implements Field {
    private final Set<Observable.Observer> observers = new HashSet<>();

    public AbstractObservableField() {
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
}
