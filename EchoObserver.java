import java.util.Observable;
import java.util.Observer;

/**
 * Definieren Sie eine neue Observerklasse EchoObserver,
 * die nach jedem großen Vokal,
 * den sie am Ende des Strings beobachtet,
 * den gleichen Vokal noch fünfmal nacheinander an das Observable anfügt
 * (Aufruf von addChar) und dann die Beobachter benachrichtigen lässt.
 *
 * Sorgen Sie dafür, dass der EchoObserver nicht auf das eigene Echo antwortet!
 *
 * Hinweis: Zwei EchoObserver produzieren nach Eingabe eines a zusammen zwanzig A.
 *
 * @author "Elderov Ali, IF4B"
 */

public class EchoObserver implements Observer{

	/** maximale Anzahl vokalen. */
	public static final int MAX_COUNT_VOKAL = 5;

	/** Unveränderliches Observable. */
    private final StringObservable observable;

    /** LOCK für selber. */
    private boolean mSelf;

	/**
	 * Konstruktor.
	 *
	 * @param observable - StringObservable
	 */
	public EchoObserver(final StringObservable observable) {
		this.observable = observable;
		mSelf = false;
		observable.addObserver(this);
	}

	@Override
	public void update(final Observable notused, final Object ignored) {
		if (!isSelf()) {
			final String str = observable.getString();
			final char lastChar = str.charAt(str.length()-1);
			System.out.printf("%s: new string available: %s%n", this, str);
			if (String.valueOf(lastChar).matches("A|E|O|U|I")) {
				setSelf(true);
				for (int i=0;i<MAX_COUNT_VOKAL;i++) {
					observable.addChar(lastChar);
				}
				observable.notifyObservers();
				setSelf(false);
			} // if vokal
		}
	}

	public boolean isSelf() {
		return mSelf;
	}

	public void setSelf(final boolean self) {
		mSelf = self;
	}

}