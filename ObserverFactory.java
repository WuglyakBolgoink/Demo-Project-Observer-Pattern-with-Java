import java.lang.reflect.Constructor;
import java.util.Observer;

/**
 * Definieren Sie eine statische Observerfactory ObserverFactory.
 * Sie kšnnen mit fest vorgegebenen Typen arbeiten oder Reflection verwenden.
 * Passen Sie ObserverMain so an,
 * dass Typen von der Kommandozeile an die Factory Ÿbergeben werden.
 *
 * @author "Elderov Ali, IF4B"
 */
public class ObserverFactory {

	@SuppressWarnings("unchecked")
	public static Observer make(final String args, final StringObservable observable) {
		try {
			final Class<?> observerClass = Class.forName(args);
			final Constructor<Observer>[] constructors = (Constructor<Observer>[]) observerClass.getConstructors();
			final Constructor<Observer> firstConstuctor = constructors[0];
			return firstConstuctor.newInstance(observable);
		} catch (final ReflectiveOperationException roe) {
			roe.printStackTrace();
		}
		return null;
	}

}
