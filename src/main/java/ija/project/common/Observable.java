//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ija.project.common;

public interface Observable {
    void addObserver(Observer var1);

    void removeObserver(Observer var1);

    void notifyObservers();

    interface Observer {
        void update(Observable var1);
    }

    void addLogObserver(Observer var1);

    void removeLogObserver(Observer var1);

    void notifyLogObservers(MazeObject o);

}
