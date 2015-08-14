package sk.berops.android.fueller.dataModel;

import java.util.ArrayList;
import java.util.Collection;
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
	@ElementList(inline = true, required = false)
	private LinkedList<Car> cars;
	/**
	 * A list of all tyres ever recorded
	 */
	@ElementList(inline = true, required = false)
	private ArrayList<Tyre> allTyres;
	@ElementList(inline = true, required = false)
	private LinkedList<Integer> availableTyresIDs;
	@Element(name = "activeCar", required = false)
	private int activeCarId;

	public Garage() {
		super();
		cars = new LinkedList<Car>();
		allTyres = new ArrayList<Tyre>();
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

	public ArrayList<Tyre> getAllTyres() {
		if (allTyres == null) {
			allTyres = new ArrayList<Tyre>();
		}
		return allTyres;
	}

	public void setAllTyres(ArrayList<Tyre> allTyres) {
		this.allTyres = allTyres;
	}

	public LinkedList<Integer> getAvailableTyresIDs() {
		if (availableTyresIDs == null) {
			availableTyresIDs = new LinkedList<Integer>();
		}
		return availableTyresIDs;
	}

	public void setAvailableTyresIDs(LinkedList<Integer> availableTyresIDs) {
		this.availableTyresIDs = availableTyresIDs;
	}

	public LinkedList<Tyre> getAvailableTyres() {
		LinkedList<Tyre> tyres = new LinkedList<Tyre>();
		for (Integer i : getAvailableTyresIDs()) {
			tyres.add(getAllTyres().get(i));
		}
		return tyres;
	}

	public Car getActiveCar() {
		if (getCars().size() == 0)
			return null;
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
		if ((this.activeCarId < 0) && (this.getCars().size() > 0)) {
			activeCarId = 0;
		}
		return this.activeCarId;
	}

	public void setActiveCarId(int id) {
		this.activeCarId = id;
	}

	private Integer getLatestTyreID() {
		return getAllTyres().size();
	}

	public void initAfterLoad() {
		int dynamicId = 0;

		for (Tyre t : getAllTyres()) {
			// We want to make sure that all tyres are initialized
			// If a tyre is installed on a car, it will get re-initialized with
			// the correct car reference through the below FOR loop
			t.initAfterLoad(null);
		}

		for (Car c : cars) {
			c.initAfterLoad();
			for (Entry e : c.getHistory().getEntries()) {
				e.setDynamicId(dynamicId++);
			}
		}
	}
	
	public LinkedList<Tyre> getTyresByIDs(Collection<Integer> c) {
		LinkedList<Tyre> tyres = new LinkedList<Tyre>();
		for (Integer i : c) {
			if (i == -1) {
				tyres.add(null);
				continue;
			}
			tyres.add(getAllTyres().get(i));
		}
		return tyres;
	}

	public void addNewTyre(Tyre tyre) {
		tyre.setDynamicID(getAllTyres().size());
		getAllTyres().add(tyre);
		getAvailableTyresIDs().add(tyre.getDynamicID());
	}
}
