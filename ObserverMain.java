/* (C) 2013, R. Schiedermeier, rs@cs.hm.edu
 * Oracle Corporation Java 1.7.0, Linux i386 2.6.32.58
 * lilli (Intel CPU U7300/1300 MHz, 2 Cores, 4096 MB RAM)
 */

import java.io.IOException;

/** Erzeugt einen Observable und ein paar Observer.
 * @author Reinhard Schiedermeier, rs@cs.hm.edu
 * @version 2013-04-28
 */
public class ObserverMain {
    /** Hauptprogramm.
     * @param args Kommandozeilenargumente.
     * Für jedes Argument wird ein Subscriber erzeugt.
     * Der Wortlaut des Argumentes spielt keine Rolle.
     * @throws IOException Lesefehler von der Tastatur. Kann eigentlich nicht sein.
     */
    public static void main(final String... args) throws IOException {
    	final StringObservable observable = new StringObservable();
    	for(final String arg: args) {
			ObserverFactory.make(arg, observable);
		}
    	 int key = System.in.read();
    	 while(key >= 0) {
    	 if(Character.isLetter(key)) {
    	 observable.addChar((char)key);
    	 if(Character.isLowerCase(key)) {
			observable.addChar((char)Character.toUpperCase(key));
		}
    	 observable.notifyObservers();
    	 }
    	 key = System.in.read();
    	 }
    }

}
