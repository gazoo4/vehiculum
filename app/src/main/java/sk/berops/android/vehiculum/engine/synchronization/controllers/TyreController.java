package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.maintenance.GenericPart;
import sk.berops.android.vehiculum.dataModel.maintenance.Tyre;

/**
 * @author Bernard Halas
 * @date 6/27/17
 */

public class TyreController extends GenericPartController {
	private Tyre tyre;

	public TyreController(GenericPart genericPart) {
		super(genericPart);
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		// There are no child references possible from Tyre object
		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof Tyre)) {
			logFailure(recordUpdate);
			return updated;
		}

		Tyre tyreUpdate = (Tyre) recordUpdate;

		if (! tyre.getModel().equals(tyreUpdate.getModel())) {
			tyre.setModel(tyreUpdate.getModel());
			logUpdate("model");
			updated = true;
		}

		if (tyre.getWidth() != tyreUpdate.getWidth()) {
			tyre.setWidth(tyreUpdate.getWidth());
			logUpdate("width");
			updated = true;
		}

		if (tyre.getHeight() != tyreUpdate.getHeight()) {
			tyre.setHeight(tyreUpdate.getHeight());
			logUpdate("height");
			updated = true;
		}

		if (tyre.getDiameter() != tyreUpdate.getDiameter()) {
			tyre.setDiameter(tyreUpdate.getDiameter());
			logUpdate("diameter");
			updated = true;
		}

		if (tyre.getWeightIndex() != tyreUpdate.getWeightIndex()) {
			tyre.setWeightIndex(tyreUpdate.getWeightIndex());
			logUpdate("weightIndex");
			updated = true;
		}

		if (! tyre.getSpeedIndex().equals(tyreUpdate.getSpeedIndex())) {
			tyre.setSpeedIndex(tyreUpdate.getSpeedIndex());
			logUpdate("speedIndex");
			updated = true;
		}

		if (! tyre.getDot().equals(tyreUpdate.getDot())) {
			tyre.setDot(tyreUpdate.getDot());
			logUpdate("dot");
			updated = true;
		}

		if (! tyre.getSeason().equals(tyreUpdate.getSeason())) {
			tyre.setSeason(tyreUpdate.getSeason());
			logUpdate("season");
			updated = true;
		}

		if (tyre.getThreadLevel() != tyreUpdate.getThreadLevel()) {
			tyre.setThreadLevel(tyreUpdate.getThreadLevel());
			logUpdate("threadLevel");
			updated = true;
		}

		if (tyre.getThreadMin() != tyreUpdate.getThreadMin()) {
			tyre.setThreadMin(tyreUpdate.getThreadMin());
			logUpdate("threadMin");
			updated = true;
		}

		if (tyre.getThreadMax() != tyreUpdate.getThreadMax()) {
			tyre.setThreadMax(tyreUpdate.getThreadMax());
			logUpdate("threadMax");
			updated = true;
		}

		if (tyre.getMileageDriveAxle() != tyreUpdate.getMileageDriveAxle()) {
			tyre.setMileageDriveAxle(tyreUpdate.getMileageDriveAxle());
			logUpdate("mileageDriveAxle");
			updated = true;
		}

		if (tyre.getMileageNonDriveAxle() != tyreUpdate.getMileageNonDriveAxle()) {
			tyre.setMileageNonDriveAxle(tyreUpdate.getMileageNonDriveAxle());
			logUpdate("mileageNonDriveAxle");
			updated = true;
		}
		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		// There are no child references possible from Tyre object
		return updated;
	}
}
