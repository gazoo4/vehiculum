package sk.berops.android.vehiculum.dataModel.synchronization;

import java.util.LinkedList;

/**
 * @author Bernard Halas
 * @date 5/24/17
 */

public class DataChangeSet {
	private LinkedList<DatasetChangeItem> changes;

	public DataChangeSet() {
		changes = new LinkedList<>();
	}

	public LinkedList<DatasetChangeItem> getChanges() {
		return changes;
	}

	public void setChanges(LinkedList<DatasetChangeItem> changes) {
		this.changes = changes;
	}
}
