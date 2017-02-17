package sk.berops.android.caramel.io.xml;

import org.simpleframework.xml.transform.Transform;

import java.util.UUID;

/**
 * Created by Bernard Halas on 3/12/16.
 *
 * Adapter for XML persistency of 3rd party objects: UUIDs.
 */
public class UUIDTransform implements Transform<UUID> {
	@Override
	public UUID read(String value) throws Exception {
		return UUID.fromString(value);
	}

	@Override
	public String write(UUID value) throws Exception {
		return value.toString();
	}
}
