/**
 * 
 */
package sk.berops.android.fueller.dataModel.maintenance;

import java.io.Serializable;
import java.util.LinkedList;

import org.simpleframework.xml.ElementList;

import android.util.Log;
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
	 * Axles in this tyre configuration
	 */
	@ElementList(inline=true, required=false)
	private LinkedList<Axle> axles;
	
	private Car car;
	
	public TyreConfigurationScheme(Car car) {
		this.car = car;
	}
	
	public void initAfterLoad(Car car) {
		this.car = car;
		for (Axle a : axles) {
			a.initAfterLoad(car);
		}
	}
	
	public LinkedList<Axle> createAxles(CarType type) {
		LinkedList<Axle> axles = new LinkedList<Axle>();
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
			axles.add(new Axle(Axle.AxleType.STANDARD));
			axles.add(new Axle(Axle.AxleType.STANDARD));
			break;
		case MOTORBIKE:
			axles.add(new Axle(Axle.AxleType.SINGLE));
			axles.add(new Axle(Axle.AxleType.SINGLE));
			break;
		case SEMI_TRAILER:
			axles.add(new Axle(Axle.AxleType.STANDARD));
			axles.add(new Axle(Axle.AxleType.STANDARD));
			axles.add(new Axle(Axle.AxleType.STANDARD));
			break;
		case THREE_WHEELER:
			axles.add(new Axle(Axle.AxleType.SINGLE));
			axles.add(new Axle(Axle.AxleType.STANDARD));
			break;
		case TRUCK:
		case TRUCK_TRACTOR:
			axles.add(new Axle(Axle.AxleType.STANDARD));
			axles.add(new Axle(Axle.AxleType.TANDEM));
			axles.add(new Axle(Axle.AxleType.TANDEM));
			break;
		default: 
			Log.d("WARN", "Wrong CarType specified");
			break;
		}
		
		return axles;
	}
	
	public LinkedList<Axle> getAxles() {
		if (axles == null) {
			return createAxles(car.getType());
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
