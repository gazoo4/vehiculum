package sk.berops.android.caramel.io.xml;

import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

import java.util.UUID;

/**
 * Created by Bernard Halas on 3/12/16.
 *
 * Class used for handling 3rd party XML persistency within SimpleXML.
 * In the current scope concerns:
 * - UUID objects
 */
public class CaramelCustomMatcher implements Matcher {
	@Override
	public Transform match(Class type) throws Exception {
		if (type.equals(UUID.class)) {
			return new UUIDTransform();
		}
		return null;
	}
}
