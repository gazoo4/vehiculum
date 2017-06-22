package sk.berops.android.vehiculum.dataModel.synchronization;

import java.util.UUID;

/**
 * @author Bernard Halas
 * @date 5/22/17
 */

public class DataDelete extends DatasetChangeItem {

	private UUID uuid;
	private UUID parent;

	public DataDelete() {
		setChangeType(ChangeType.DELETE);
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getParent() {
		return parent;
	}

	public void setParent(UUID parent) {
		this.parent = parent;
	}

	public UUID getBaseRecordUUID() {
		return parent;
	}

	public String toString() {
		return super.toString() + " Child: " + uuid.toString();
	}
}
