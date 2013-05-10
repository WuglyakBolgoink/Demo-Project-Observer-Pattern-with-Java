import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * Observerklasse PortObserver,
 * die Netzwerkverbindungen annimmt und den Text
 *
 * PortObserver@…: new string available: …
 *
 * an den betreffenden Client schickt.
 * Der erste Portobserver öffnet Port 2000, der nächste Port 2001 und so weiter.
 *
 * Ein Portobserver erwartet keine Daten vom Client,
 * sondern schickt nur Änderungsmeldungen.
 * Wenn ein Client die Verbindung beendet,
 * wartet der Portobserver auf den nächsten Client.
 * Bis dahin verwirft der Portobserver Meldungen.
 *
 * @author "Elderov Ali, IF4B"
 */

public class PortObserver implements Observer{
	/** default server port. */
	public static final int PORT = 2000;
	/**	Default Encoding for Input/OutputStreamReader. */
	public static final String DEFAULT_ENCODING = "UTF8";
	/** ServerPort. */
	private static int sServerPort = PORT;

	/** ServerSocket - clientSocket. */
	private ServerSocket mServerSocket;
	/** Unveränderliches Observable. */
	private final StringObservable mObservable;
	/** PrintWriter. */
	private PrintWriter mPrintWriter;
	/** clientSocket. */
	private Socket mClientSocket;
	/** current serverPort. */
	private final int mCurrentPort = sServerPort;

	/**
	 * PortObserver(StringObservable) - erwartet ein StringObservable.<br>
	 * Startet ein Thread mit ServerSocket mit currentPort,
	 * wartet auf Client, wenn client disconnected erzetzt clientSocket auf null.
	 *
	 * @param observable - StringObservable
	 */
	public PortObserver(final StringObservable observable) {

		try {
		 	mServerSocket =  new ServerSocket(mCurrentPort);
		 	System.out.println("======# Start Server with port: "+mCurrentPort+" #======");
		 	setServerPort();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
		mObservable = observable;
		observable.addObserver(this);
		startThread();
	}

	/** setter. */
	public static void setServerPort() {
		sServerPort++;
	}
//	/**
//	 * getter mCurrentPort.
//	 * @return int - current port number
//	 */
//	public int getCurrentPort() {return mCurrentPort;}

//	/**
//	 * setter mCurrentPort.
//	 */
//	public void setCurrentPort() {
//		mCurrentPort = sServerPort;
//		sServerPort++;
//	}


	/**
	 * start Thread.
	 */
	private void startThread() {
		new Thread(new Runnable() {
			@Override public void run() {
				try {
					while (true) {
						mClientSocket = mServerSocket.accept();
						System.out.println(">>> new Client: " + getClientSocketInfo());

						final BufferedReader mBufferedReader = new BufferedReader(
								new InputStreamReader(mClientSocket.getInputStream(), DEFAULT_ENCODING));

						mPrintWriter = new PrintWriter(
										new OutputStreamWriter(mClientSocket.getOutputStream(), DEFAULT_ENCODING));
						mBufferedReader.readLine();
						System.out.println("test-> "+new InputStreamReader(mClientSocket.getInputStream(), DEFAULT_ENCODING).read());
						if (new InputStreamReader(mClientSocket.getInputStream(), DEFAULT_ENCODING).read() == -1) {
							System.out.println(getClientSocketInfo() + " disconnected");
							mClientSocket = null;
						}
					}
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Gibt hilfsinformation ob server hat eine verbindung mit Client oder nein.
	 * Falls ja, zeigt meldung von observable.
	 *
	 * @param notused Ein Stringobservable.
	 * @param ignored Unbenutztes Objekt.
	 */
	@Override public void update(final Observable notused, final Object ignored) {
		System.out.println("[Server:" + mCurrentPort + "]: "
				+ (mClientSocket == null ? "OFF" : " ON <===> " + getClientSocketInfo()));
		if (mClientSocket != null) {
			mPrintWriter.write(this + ": new string available: " + mObservable.getString() + "\n");
			mPrintWriter.flush();
		}
		mObservable.notifyObservers();
	}

	/**
	 * Gibt String "Client [IP:PORT]" zurück.
	 *
	 * @return String
	 */
	private String getClientSocketInfo() {
		return "Client ["
				+ mClientSocket.getInetAddress().getHostAddress()+":"+mClientSocket.getPort() + "]";
	}
}