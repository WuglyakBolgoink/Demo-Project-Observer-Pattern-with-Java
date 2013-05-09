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
/*
 $ java ObserverMain StringObserver EchoObserver
ab
StringObserver@15660ef: new string available: aA
EchoObserver@9d1714: new string available: aA
StringObserver@15660ef: new string available: aAAAAAA
StringObserver@15660ef: new string available: aAAAAAAbB
EchoObserver@9d1714: new string available: aAAAAAAbB
*/
public class EchoObserver implements Observer{

	/** Unveränderliches Observable. */
    private final StringObservable observable;

    private boolean isBlock = false;

    private boolean bool = false;

    public static int MAX_COUNT_VOKAL = 5;
	/**
	 * @param observable
	 */
	public EchoObserver(final StringObservable observable) {
		this.observable = observable;
		observable.addObserver(this);
	}

	@Override
	public void update(final Observable o, final Object arg) {
		final String str = observable.getString();
		final char lastChar = str.charAt(str.length()-1);

		if (!isBlock()) {
			System.out.printf("%s: new string available: %s%n", this, str);
			if (!isBool()) {
				if (isVokal(lastChar)) {
					for (int i=0;i < MAX_COUNT_VOKAL;i++) {
						observable.addChar(lastChar);
					}
					setBlock(true);
					observable.notifyObservers();
				}
				setBool(false);
			}
		} else {
			setBlock(false);
			setBool(true);
		}
	}

	boolean isVokal(final char inChar) {
		return String.valueOf(inChar).matches("A|E|O|U|I");
    }

	public boolean isBlock() {
		return isBlock;
	}

	public void setBlock(final boolean isBlock) {
		this.isBlock = isBlock;
	}

	public boolean isBool() {
		return bool;
	}

	public void setBool(final boolean bool) {
		this.bool = bool;
	}

}