package ija.project.common;

import java.util.List;

/**
 * Interface for observable objects.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public interface Observable {
    interface Observer {
        /**
         * Updates the observer. Public method that every observer implements own way of
         * updating.
         * 
         * @param var1 Object that is being observed.
         */
        void update(Observable var1);
    }

    /**
     * Adds an observer to the observable object.
     * 
     * @param var1 Observer to be added.
     */
    void addObserver(Observer var1);

    /**
     * Removes an observer from the observable object.
     * 
     * @param var1 Observer to be removed.
     */
    void removeObserver(Observer var1);

    /**
     * Notifies all observers of the observable object.
     */
    void notifyObservers();

    /**
     * Adds an observer to the observable object. Used only for logging.
     * 
     * @param var1 Observer to be added.
     */
    void addLogObserver(Observer var1);

    /**
     * Removes an observer from the observable object. Used only for logging.
     * 
     * @param var1 Observer to be removed.
     */
    void removeLogObserver(Observer var1);

    /**
     * Notifies all observers of the observable object. Used only for logging.
     */
    void notifyLogObservers();

    /**
     * Returns list of observers of the observable object.
     * 
     * @return List of observers.
     */
    List<Observer> getObservers();

    /**
     * Returns list of logging observers of the observable object.
     *
     * @return List of logging observers.
     */
    List<Observer> getLogObservers();

}
