package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.maintenance.ReplacementPart;

/**
 * @author Bernard Halas
 * @date 6/26/17
 */

public class ReplacementPartController extends GenericPartController {
	private ReplacementPart replacementPart;

	public ReplacementPartController(ReplacementPart replacementPart) {
		super(replacementPart);
		this.replacementPart = replacementPart;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		// There are no child references possible from ReplacementPart object
		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof ReplacementPart)) {
			logFailure(recordUpdate);
			return updated;
		}

		ReplacementPart replacementPartUpdate = (ReplacementPart) recordUpdate;

		if (! replacementPart.getOriginality().equals(replacementPartUpdate.getOriginality())) {
			replacementPart.setOriginality(replacementPartUpdate.getOriginality());
			logUpdate("originality");
			updated = true;
		}

		if (replacementPart.getQuantity() != replacementPartUpdate.getQuantity()) {
			replacementPart.setQuantity(replacementPartUpdate.getQuantity());
			logUpdate("quantity");
			updated = true;
		}

		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		// There are no child references possible from ReplacementPart object
		return updated;
	}
}
