package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.expense.FuellingEntry;

/**
 * @author Bernard Halas
 * @date 6/23/17
 */

public class FuellingEntryController extends EntryController {
	private FuellingEntry fuellingEntry;

	public FuellingEntryController(FuellingEntry fuellingEntry) {
		super(fuellingEntry);
		this.fuellingEntry = fuellingEntry;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		// There are no child references possible from FuellingEntry object
		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof FuellingEntry)) {
			logFailure(recordUpdate);
			return updated;
		}

		FuellingEntry entryUpdate = (FuellingEntry) recordUpdate;

		if (fuellingEntry.getFuelQuantity() != entryUpdate.getFuelQuantity()) {
			fuellingEntry.setFuelQuantity(entryUpdate.getFuelQuantity(), fuellingEntry.getQuantityUnit());
			logUpdate("fuelQuantity");
			updated = true;
		}

		if (! fuellingEntry.getQuantityUnit().equals(entryUpdate.getQuantityUnit())) {
			fuellingEntry.setQuantityUnit(entryUpdate.getQuantityUnit());
			logUpdate("quantityUnit");
			updated = true;
		}

		if (! fuellingEntry.getFuelType().equals(entryUpdate.getFuelType())) {
			fuellingEntry.setFuelType(entryUpdate.getFuelType());
			logUpdate("fuelType");
			updated = true;
		}

		if (fuellingEntry.getFuelPrice() != entryUpdate.getFuelPrice()) {
			fuellingEntry.setFuelPrice(entryUpdate.getFuelPrice());
			logUpdate("fuelPrice");
			updated = true;
		}

		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		// There are no child references possible from FuellingEntry object
		return updated;
	}
}
