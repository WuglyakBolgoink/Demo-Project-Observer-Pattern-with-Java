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
 * Schreiben Sie eine neue Observerklasse PortObserver,
 * die Netzwerkverbindungen annimmt und den Text
 *
 * PortObserver@É: new string available: É
 *
 * an den betreffenden Client schickt.
 * Der erste Portobserver šffnet Port 2000, der nŠchste Port 2001 und so weiter.
 *
 * Ein Portobserver erwartet keine Daten vom Client,
 * sondern schickt nur €nderungsmeldungen.
 * Wenn ein Client die Verbindung beendet,
 * wartet der Portobserver auf den nŠchsten Client.
 * Bis dahin verwirft der Portobserver Meldungen.
 *
 * Zum Testen eignet sich beispielweise der Telnet-Client.
 *
 *
 * @author "Elderov Ali, IF4B"
 */
/*
 	Ein Beispiel: Auf einem Rechner namens ãsystem1Ò:
	$ java ObserverMain StringObserver PortObserver PortObserver
	a
	StringObserver@8a88a9: new string available: aA
	b
	StringObserver@8a88a9: new string available: aAbB
	c
	StringObserver@8a88a9: new string available: aAbBcC
	d
	StringObserver@8a88a9: new string available: aAbBcCdD
	e
	StringObserver@8a88a9: new string available: aAbBcCdDeE
 */
/*
	Auf einem anderen Rechner:
	(Die Bedienung des Telnet-Client hŠngt vom System ab.)
	$ telnet system1 2000     # nach Eingabe von a auf system1
	Connected to system1.
	PortObserver@fbb5f5: new string available: aAbB
	^]                        # nach Eingabe von b auf system1
	telnet> quit
	Connection closed.
	$ telnet system1 2001     # nach Eingabe von c auf system1
	Connected to system1.
	PortObserver@898b41: new string available: aAbBcCdD
	^]                        # nach Eingabe von d auf system1
	telnet> quit
	Connection closed.
 */
public class PortObserver implements Observer{
	private final StringObservable observable;
	public static int PORT = 2000;
	private PrintWriter bos;
	private BufferedReader br;
	private ServerSocket serverSocket = null;
	private Socket socket;


	/**
	 * @param observable
	 */
	public PortObserver(final StringObservable observable) {
		try {
			final int curPort = PORT++;
		 	serverSocket =  new ServerSocket(curPort);
		 	System.out.println("start Server with port"+curPort);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		this.observable = observable;
		observable.addObserver(this);
	}


	@Override
	public void update(final Observable o, final Object arg) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					socket = serverSocket.accept();
					observable.notifyObservers();
					System.out.println("socket: "+socket);
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					bos = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
					while (true) {
						final String theString = observable.getString();
						String tmp;
						tmp = br.readLine();
						bos.write(this+": new string available: "+tmp+"   >>>"+theString+"<<<\n");
						bos.flush();
						observable.addChar(tmp.charAt(0));
					}
				} catch (final IOException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}


}
