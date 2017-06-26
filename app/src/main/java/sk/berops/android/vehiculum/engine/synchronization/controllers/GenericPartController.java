package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.expense.Expense;
import sk.berops.android.vehiculum.dataModel.maintenance.GenericPart;

/**
 * @author Bernard Halas
 * @date 6/26/17
 */

public class GenericPartController extends ExpenseController {
	private GenericPart part;

	public GenericPartController(GenericPart genericPart) {
		super(genericPart);
		this.part = genericPart;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		// There are no child references possible from GenericPart object
		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof GenericPart)) {
			logFailure(recordUpdate);
			return updated;
		}

		GenericPart partUpdate = (GenericPart) recordUpdate;

		if (! part.getCondition().equals(partUpdate.getCondition())) {
			part.setCondition(partUpdate.getCondition());
			logUpdate("condition");
			updated = true;
		}

		if (! part.getBrand().equals(partUpdate.getBrand())) {
			part.setBrand(partUpdate.getBrand());
			logUpdate("brand");
			updated = true;
		}

		if (! part.getProducerPartID().equals(partUpdate.getProducerPartID())) {
			part.setProducerPartID(partUpdate.getProducerPartID());
			logUpdate("producerPartID");
			updated = true;
		}

		if (! part.getCarmakerPartID().equals(partUpdate.getCarmakerPartID())) {
			part.setCarmakerPartID(partUpdate.getCarmakerPartID());
			logUpdate("carmakerPartID");
			updated = true;
		}

		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		// There are no child references possible from GenericPart object
		return updated;
	}
}
