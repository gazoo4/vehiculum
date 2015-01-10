package sk.berops.android.fueller.dataModel;

import java.util.Iterator;
import java.util.LinkedList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.MainActivity;

@Root
public class Garage {
	@ElementList(inline=true, required=false)
	private LinkedList<Car> cars;
	@ElementList(inline=true, required=false)
	private LinkedList<Tyre> tyres;
	@Element(name="activeCar", required=false)
	private int activeCarId;
	
	public Garage() {
		super();
		cars = new LinkedList<Car>();
		tyres = new LinkedList<Tyre>();
		activeCarId = -1;
	}
	
	public LinkedList<Car> getCars() {
		return cars;
	}

	public void setCars(LinkedList<Car> cars) {
		this.cars = cars;
	}
	
	public void addCar(Car car) {
		cars.add(car);
		setActiveCarId(getCars().size() - 1);
	}
	
	public LinkedList<Tyre> getTyres() {
		return tyres;
	}
	
	public void addTyre(Tyre tyre) {
		tyres.add(tyre);
	}
	
	public Car getActiveCar() {
		if (getCars().size() == 0) return null;
		if (activeCarId < 0) {
			activeCarId = 0;
		}
		try {
			return cars.get(activeCarId);
		} catch (IndexOutOfBoundsException ex) {
			return cars.getFirst();
		}
	}
	
	public int getActiveCarId() {
		if ((this.activeCarId < 0) && (this.getCars().size() > 0)) { activeCarId = 0; }
		return this.activeCarId;
	}
	
	public void setActiveCarId(int id) {
		this.activeCarId = id;
	}
	
	public void initAfterLoad() {
		Car car;
		Entry entry;
		int dynamicId = 0;
		
		for (Iterator<Car> c = this.cars.iterator(); c.hasNext(); ) {
			car = c.next();
			car.initAfterLoad();
			for (Iterator<Entry> e = car.getHistory().getEntries().iterator(); e.hasNext(); ) {
				entry = e.next();
				entry.setDynamicId(dynamicId++);
			}
		}
	}
}
