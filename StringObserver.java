/* (C) 2013, R. Schiedermeier, rs@cs.hm.edu
 * Oracle Corporation Java 1.7.0, Linux i386 2.6.32.58
 * lilli (Intel CPU U7300/1300 MHz, 2 Cores, 4096 MB RAM)
 */

import java.util.Observable;
import java.util.Observer;

/** Observer, der String des Observable ausgibt.
 * @author Reinhard Schiedermeier, rs@cs.hm.edu
 * @version 2013-04-28
 */
public class StringObserver implements Observer {
    /** Unveränderliches Observable. */
    private final StringObservable observable;

    /** Erzeugt einen neuen Observer und registriert ihn sofort beimObservable .
     * @param observable Observable dieses Observers. Nicht null.
     */
    public StringObserver(final StringObservable observable) {
        this.observable = observable;
        observable.addObserver(this);
    }

    /** Wird vom Observable aufgerufen, wenn es neue Daten gibt.
     * Holt sich neue Daten vom Observable und zeigt sie auf dem Bildschirm.
     * @param notused Ein Stringobservable.
     * @param ignored Unbenutztes Objekt.
     */
    @Override public void update(final Observable notused, final Object ignored) {
        final String theString = observable.getString();
        System.out.printf("%s: new string available: %s%n", this, theString);
    }

}
