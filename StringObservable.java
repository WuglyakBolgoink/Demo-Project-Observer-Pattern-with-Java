/* (C) 2013, R. Schiedermeier, rs@cs.hm.edu
 * Oracle Corporation Java 1.7.0, Linux i386 2.6.32.58
 * lilli (Intel CPU U7300/1300 MHz, 2 Cores, 4096 MB RAM)
 */

import java.util.Observable;

/** Observable, das einen String verwaltet.
 * @author Reinhard Schiedermeier, rs@cs.hm.edu
 * @version 2013-04-28
 */
public class StringObservable extends Observable {
    /** String, in dem sich Zeichen sammeln.
     * Daten dieses Observable.
     */
    private String theString = "";

    /** Erzeugt einen Observable ohne Observer. */
    public StringObservable() {
    }

    /** Fügt das Zeichen an den String an und merkt sich, dass es eine Änderung gab.
     * Ein x beginnt den String neu.
     * @param chr Zeichen, das angefügt wird.
     */
    public void addChar(final char chr) {
        if(chr == 'x') {
			theString = "x";
		} else {
			theString += chr;
		}
        setChanged();
    }

     /** Liefert den String.
     * @return String mit den Zeichen, die sich bisher angesammelt haben. Nicht null.
     */
    public String getString() {
        return theString;
    }

}
