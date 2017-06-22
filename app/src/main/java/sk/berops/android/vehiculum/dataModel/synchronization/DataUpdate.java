package sk.berops.android.vehiculum.dataModel.synchronization;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;

/**
 * @author Bernard Halas
 * @date 5/22/17
 */

public class DataUpdate extends DatasetChangeItem {

	private Record updatedRecord;

	public DataUpdate() {
		setChangeType(ChangeType.UPDATE);
	}

	public Record getUpdatedRecord() {
		return updatedRecord;
	}

	public void setUpdatedRecord(Record updatedRecord) {
		this.updatedRecord = updatedRecord;
	}

	public UUID getBaseRecordUUID() {
		return updatedRecord.getUuid();
	}
}