package sk.berops.android.vehiculum.engine;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;

/**
 * @author Bernard Halas
 * @date 5/24/17
 */

public interface Updatable {
	boolean createRecord(Record newRecord);
	boolean updateRecord(Record newRecord);
	boolean deleteRecord(UUID uuid);
}
