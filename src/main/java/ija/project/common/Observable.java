package ija.project.common;

/**
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 * @brief Interface for observable objects.
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

}
