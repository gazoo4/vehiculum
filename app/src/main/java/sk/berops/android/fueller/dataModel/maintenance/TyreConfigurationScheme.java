/**
 * 
 */
package sk.berops.android.fueller.dataModel.maintenance;

import android.util.Log;

import org.simpleframework.xml.ElementList;

import java.io.Serializable;
import java.util.LinkedList;

import sk.berops.android.fueller.dataModel.Axle;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Car.CarType;

/**
 * This object holds the configuration of the tyres on the car
 * (current configuration or a configuration after a certain TyreChangeEntry)
 * 
 * @author bernard
 *
 */
public class TyreConfigurationScheme implements Serializable, Cloneable {

	/**
	 * Axles in this tyre configuration, linked from front to back
	 */
	@ElementList(inline=true, required=false)
	private LinkedList<Axle> axles;
	
	private Car car;

	public TyreConfigurationScheme() {

	}

	/**
	 * Constructor
	 * @param car
	 */
	public TyreConfigurationScheme(Car car) {
		this.car = car;
		createAxles(car);
	}

	/**
	 * Method that's called after the objects are loaded
	 * @param car
	 */
	public void initAfterLoad(Car car) {
		this.car = car;
		for (Axle a : axles) {
			a.initAfterLoad(car);
		}
	}

	/**
	 * Method to generate the axle structures based on the car
	 * @param car to generate the Axles for
	 */
	public void createAxles(Car car) {
		LinkedList<Axle> axles = new LinkedList<>();
		CarType type = car.getType();
		switch (type) {
		case CITY_CAR:
		case COUPE:
		case HATCHBACK:
		case PICKUP:
		case ROADSTER:
		case SEDAN:
		case STATION_WAGON:
		case SUV:
		case TRACTOR:
		case VAN:
			axles.add(new Axle(Axle.Type.STANDARD));
			axles.add(new Axle(Axle.Type.STANDARD));
			break;
		case MOTORBIKE:
			axles.add(new Axle(Axle.Type.SINGLE));
			axles.add(new Axle(Axle.Type.SINGLE));
			break;
		case SEMI_TRAILER:
			axles.add(new Axle(Axle.Type.STANDARD));
			axles.add(new Axle(Axle.Type.STANDARD));
			axles.add(new Axle(Axle.Type.STANDARD));
			break;
		case THREE_WHEELER:
			axles.add(new Axle(Axle.Type.SINGLE));
			axles.add(new Axle(Axle.Type.STANDARD));
			break;
		case TRUCK:
			axles.add(new Axle(Axle.Type.STANDARD));
			axles.add(new Axle(Axle.Type.TANDEM));
			axles.add(new Axle(Axle.Type.TANDEM));
		case TRUCK_TRACTOR:
			axles.add(new Axle(Axle.Type.STANDARD));
			axles.add(new Axle(Axle.Type.TANDEM));
			break;
		default: 
			Log.d("WARN", "Wrong CarType specified");
			break;
		}
		
		setAxles(axles);
	}
	
	public LinkedList<Axle> getAxles() {
		if (axles == null) {
			createAxles(car);
		}
		return axles;
	}

	public void setAxles(LinkedList<Axle> axles) {
		this.axles = axles;
	}
	
	@Override
	public TyreConfigurationScheme clone() {
		TyreConfigurationScheme scheme = new TyreConfigurationScheme(car);
		scheme.setAxles(new LinkedList<Axle>(getAxles()));
		return scheme;
	}


}
