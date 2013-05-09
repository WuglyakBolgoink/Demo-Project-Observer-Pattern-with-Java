import java.util.Observable;
import java.util.Observer;

/**
 * Definieren Sie eine neue Observerklasse TemporaryObserver,
 * die sich nach jedem T,
 * die sie am Ende des Strings beobachtet,
 * abmeldet und fünf Sekunden später automatisch wieder anmeldet.
 *
 * Die anderen Observer sollen in der Zwischenzeit behelligt weiter arbeiten.
 * Das bedeutet, dass update des TemporaryObserver in jedem Fall
 * ohne Verzögerung zurückkehrt.
 *
 * @author "Elderov Ali, IF4B"
 */

public class TemporaryObserver implements Observer {

	/** Anzahl msec die ein Thread schlafen soll. */
	public static final int SLEEP_TIME = 5000;
	/** LOCK-Object. */
	private static boolean mIsBlock = false;
	/** Unveränderliches Observable. */
	private final StringObservable mObservable;
	/** Unveränderliches Observer. */
	private final TemporaryObserver mSubscriber;

	/**
	 * Konstruktor.
	 *
	 * @param observable - StringObservable
	 */
	public TemporaryObserver(final StringObservable observable) {
		mObservable = observable;
		mSubscriber = this;
		observable.addObserver(mSubscriber);
	}


	@Override
	public void update(final Observable notused, final Object ignored) {
		final String str = mObservable.getString();
		final char lastChar = str.charAt(str.length()-1);

		if (!isBlock()) {
			if (String.valueOf(lastChar).matches("T")) {
					setBlock(true);
						new Thread(new Runnable() {
							@Override public void run() {
								try {
									mObservable.deleteObserver(mSubscriber);
									Thread.sleep(SLEEP_TIME);
									setBlock(false);
									mObservable.addObserver(mSubscriber);
								} catch (final InterruptedException e) {
									e.printStackTrace();
								}
							}
						}).start();

			} else {
				System.out.printf("%s: new string available: %s%n", this, str);
			} // isT
		}
	}

	public boolean isBlock() {
		return mIsBlock;
	}

	public void setBlock(final boolean isBlock) {
		mIsBlock = isBlock;
	}

//	/**
//	 * prüft ob ein Zeichen "T" ist.
//	 *
//	 * @param inChar - char
//	 * @return true / false
//	 */
//	static boolean isCharEqualsT(final char inChar) {
//		return String.valueOf(inChar).matches("T");
//    }

}