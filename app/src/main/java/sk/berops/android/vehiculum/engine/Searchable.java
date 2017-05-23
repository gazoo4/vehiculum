package sk.berops.android.vehiculum.engine;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;

/**
 * @author Bernard Halas
 * @date 5/23/17
 */

public interface Searchable {
	Record getRecordByUUID(UUID uuid);
}
