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
/*
$ java ObserverMain StringObserver TemporaryObserver
a
TemporaryObserver@dc5733: new string available: aA
StringObserver@1e0f790: new string available: aA
t
StringObserver@1e0f790: new string available: aAtT
cd
StringObserver@1e0f790: new string available: aAtTcC
StringObserver@1e0f790: new string available: aAtTcCdD
e                                                   # 10 Sekunden später
TemporaryObserver@dc5733: new string available: aAtTcCdDeE
StringObserver@1e0f790: new string available: aAtTcCdDeE
 */
public class TemporaryObserver implements Observer {

	private final StringObservable observable;
	private boolean isBlock = false;
	private final TemporaryObserver subscriber;

	/**
	 * @param stringObservable
	 */
	public TemporaryObserver(final StringObservable observable) {
		this.observable = observable;
		subscriber = this;
		observable.addObserver(subscriber);
	}


	@Override
	public void update(final Observable o, final Object arg) {
		final String str = observable.getString();
		final char lastChar = str.charAt(str.length()-1);

		if (!isBlock()) {
			if (isT(lastChar)) {
					setBlock(true);
						new Thread(new Runnable() {
							@Override public void run() {
								try {
									observable.deleteObserver(subscriber);
									Thread.sleep(5000);
									setBlock(false);
									observable.addObserver(subscriber);
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
		return isBlock;
	}


	/**
	 * @param isBlock the isBlock to set
	 */
	public void setBlock(final boolean isBlock) {
		this.isBlock = isBlock;
	}

	static boolean isT(final char inChar) {
		return String.valueOf(inChar).matches("T");
    }

}