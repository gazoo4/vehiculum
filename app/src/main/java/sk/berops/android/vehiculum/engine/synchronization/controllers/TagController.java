package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.tags.Tag;

/**
 * @author Bernard Halas
 * @date 6/27/17
 */

public class TagController extends RecordController {
	private Tag tag;

	public TagController(Tag tag) {
		super(tag);
		this.tag = tag;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		if (!(child instanceof Tag)) {
			logFailure(child);
			return updated;
		}

		tag.getChildren().add((Tag) child);

		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof Tag)) {
			logFailure(recordUpdate);
			return updated;
		}

		Tag tagUpdate = (Tag) recordUpdate;

		if (tag.getName().equals(tagUpdate.getName())) {
			tag.setName(tagUpdate.getName());
			logUpdate("name");
			updated = true;
		}

		if (tag.getColor() != tagUpdate.getColor()) {
			tag.setColor(tagUpdate.getColor());
			logUpdate("color");
			updated = true;
		}

		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		Record forDeletion = null;
		for (Record r: tag.getChildren()) {
			if (r.getUuid().equals(deleteUUID)) {
				forDeletion = r;
				break;
			}
		}

		if (forDeletion != null) {
			tag.getChildren().remove(forDeletion);
			logUpdate("deleted entry");
			updated = true;
		} else {
			logFailure(deleteUUID);
		}

		return updated;
	}
}
