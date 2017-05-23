package sk.berops.android.vehiculum.dataModel.synchronization;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;

/**
 * @author Bernard Halas
 * @date 5/22/17
 */

public class DataCreate extends DatasetChange {

	private Record newRecord;
	private UUID parent;
	private Class recordType;

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

	public Class getRecordType() {
		return recordType;
	}

	public void setRecordType(Class recordType) {
		this.recordType = recordType;
	}
}
