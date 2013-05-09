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

public class PortObserver implements Observer{
	private final StringObservable observable;
	public static int PORT = 2000;
	private PrintWriter bos;
	private BufferedReader br;
	private ServerSocket serverSocket = null;
	private Socket socket;
	private int curPort;
	private String tmp;

	/**
	 * @param observable
	 */
	public PortObserver(final StringObservable observable) {
		try {
			curPort = PORT++;
		 	serverSocket =  new ServerSocket(curPort);
		 	System.out.println("======# Start Server with port: "+curPort+" #======");
		} catch (final IOException e) {
			e.printStackTrace();
		}
		this.observable = observable;
		observable.addObserver(this);

		new Thread(new Runnable() {
			@Override public void run() {
				try {
					while (true) {
						socket = serverSocket.accept();
						System.out.println("======> new Client: "+socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + " <======");
						br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						bos = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
						tmp = br.readLine();
						if (new InputStreamReader(socket.getInputStream()).read() == -1) {
							System.out.println("======< Client [" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "] disconnected >======");
							socket = null;
						}
					}

				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override public void update(final Observable o, final Object arg) {
		System.out.println("======# [Server:" + curPort + "]: " + (socket == null ? "OFF" : " ON <===> Client [" + socket.getInetAddress().getHostName() + ":" +socket.getPort()+"]") + " #======");
		if (socket != null) {
			bos.write(this + ": new string available: " + observable.getString() + "\n");
			bos.flush();
		}
		observable.notifyObservers();
	}

}
