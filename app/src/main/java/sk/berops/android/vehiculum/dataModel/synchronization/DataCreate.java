package sk.berops.android.vehiculum.dataModel.synchronization;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;

/**
 * @author Bernard Halas
 * @date 5/22/17
 */

public class DataCreate extends DatasetChangeItem {

	private Record newRecord;
	private UUID parent;

	public DataCreate() {
		setChangeType(ChangeType.CREATE);
	}

	public Record getNewRecord() {
		return newRecord;
	}

	public void setNewRecord(Record newRecord) {
		this.newRecord = newRecord;
	}

	public UUID getParent() {
		return parent;
	}

	public void setParent(UUID parent) {
		this.parent = parent;
	}
}
