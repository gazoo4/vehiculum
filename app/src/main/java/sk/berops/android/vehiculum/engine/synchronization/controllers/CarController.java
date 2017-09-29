package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.maintenance.TyreConfigurationScheme;


/**
 * @author Bernard Halas
 * @date 5/30/17
 */

public class CarController extends RecordController {
	public static String LOG_TAG = "CarController";

	private Car car;

	public CarController(Car car) {
		super(car);
		this.car = car;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		// Technically the only record we can possibly add to the Car is the TyreConfigurationScheme
		if (!(child instanceof TyreConfigurationScheme)) {
			logFailure(child);
			return updated;
		}

		if (car.getInitialTyreScheme() == null) {
			car.setInitialTyreScheme((TyreConfigurationScheme) child);
			logUpdate("tyreConfigurationScheme");
			updated = true;
		} else {
			logFailure(child);
		}

		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof Car)) {
			logFailure(recordUpdate);
			return updated;
		}

		Car carUpdate = (Car) recordUpdate;
		if (! car.getNickname().equals(carUpdate.getNickname())) {
			car.setNickname(carUpdate.getNickname());
			logUpdate("nickname");
			updated = true;
		}

		if (! car.getBrand().equals(carUpdate.getBrand())) {
			car.setBrand(carUpdate.getBrand());
			logUpdate("brand");
			updated = true;
		}

		if (! car.getModel().equals(carUpdate.getModel())) {
			car.setModel(carUpdate.getModel());
			logUpdate("model");
			updated = true;
		}

		if (car.getModelYear() != carUpdate.getModelYear()) {
			car.setModelYear(carUpdate.getModelYear());
			logUpdate("modelYear");
			updated = true;
		}

		if (! car.getLicensePlate().equals(carUpdate.getLicensePlate())) {
			car.setLicensePlate(carUpdate.getLicensePlate());
			logUpdate("licensePlate");
			updated = true;
		}

		if (car.getInitialMileage() != carUpdate.getInitialMileage()) {
			car.setInitialMileage(carUpdate.getInitialMileage());
			logUpdate("initialMileage");
			updated = true;
		}

		if (car.getCurrentMileage() != carUpdate.getCurrentMileage()) {
			car.setCurrentMileage(carUpdate.getCurrentMileage());
			logUpdate("currentMileage");
			updated = true;
		}

		if (! car.getDistanceUnit().equals(carUpdate.getDistanceUnit())) {
			car.setDistanceUnit(carUpdate.getDistanceUnit());
			logUpdate("distanceUnit");
			updated = true;
		}

		if (! car.getType().equals(carUpdate.getType())) {
			car.setType(carUpdate.getType());
			logUpdate("carType");
			updated = true;
		}
		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) {
		boolean updated = false;

		if (car.getRecordByUUID(deleteUUID) == car.getInitialTyreScheme()) {
			car.setInitialTyreScheme(null);
			logUpdate("tyreConfigurationScheme");
			updated = true;
		} else {
			logFailure(deleteUUID);
		}

		return updated;
	}
}
