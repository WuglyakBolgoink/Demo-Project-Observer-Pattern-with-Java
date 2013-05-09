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

	/** Unveränderliches Observable. */
	private final StringObservable mObservable;
	/** LOCK-Object. */
	private boolean mIsBlock = false;
	/** Unveränderliches Observer. */
	private final TemporaryObserver mSubscriber;

	/**
	 * Konstruktor.
	 *
	 * @param observable - StringObservable
	 */
	public TemporaryObserver(final StringObservable observable) {
		this.mObservable = observable;
		mSubscriber = this;
		observable.addObserver(mSubscriber);
	}


	@Override
	public void update(final Observable o, final Object arg) {
		final String str = mObservable.getString();
		final char lastChar = str.charAt(str.length()-1);

		if (!isBlock()) {
			if (isCharEqualsT(lastChar)) {
					setBlock(true);
						new Thread(new Runnable() {
							@Override public void run() {
								try {
									mObservable.deleteObserver(mSubscriber);
									Thread.sleep(5000);
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

	/**
	 * @return the isBlock
	 */
	public boolean isBlock() {
		return mIsBlock;
	}


	/**
	 * @param isBlock the isBlock to set
	 */
	public void setBlock(final boolean isBlock) {
		this.mIsBlock = isBlock;
	}

	static boolean isCharEqualsT(final char inChar) {
		return String.valueOf(inChar).matches("T");
    }

}