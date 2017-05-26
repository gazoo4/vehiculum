package sk.berops.android.vehiculum.dataModel.synchronization;

import java.util.UUID;

/**
 * @author Bernard Halas
 * @date 5/22/17
 */

public class DataDelete extends DatasetChangeItem {

	private UUID uuid;

	public DataDelete() {
		setChangeType(ChangeType.DELETE);
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
}
