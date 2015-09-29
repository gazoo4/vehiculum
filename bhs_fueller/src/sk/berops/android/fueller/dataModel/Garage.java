package sk.berops.android.fueller.dataModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.TyreChangeEntry;
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
	private HashMap<Integer, Tyre> allTyres;
	@Element(name = "activeCar", required = false)
	private int activeCarId;

	public Garage() {
		super();
		cars = new LinkedList<Car>();
		allTyres = new HashMap<Integer, Tyre>();
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

	public HashMap<Integer, Tyre> getAllTyres() {
		if (allTyres == null) {
			allTyres = new HashMap<Integer, Tyre>();
		}
		return allTyres;
	}

	public void setAllTyres(HashMap<Integer, Tyre> allTyres) {
		this.allTyres = allTyres;
	}

	public ArrayList<Tyre> getAvailableTyres() {
		HashMap<Integer, Tyre> allTyres = new HashMap<Integer, Tyre>(getAllTyres());
		for (Car c: getCars()) {
			for (TyreChangeEntry e: c.getHistory().getTyreChangeEntries()) {
				for (Integer i: e.getDeletedTyreIDs()) {
					allTyres.remove(i);
				}
				for (Axle a: e.getTyreScheme().getAxles()) {
					for (Integer i: a.getTyreIDs()) {
						allTyres.remove(i);
					}
				}
			}
		}
		ArrayList<Tyre> allTyreList = new ArrayList<Tyre>(allTyres.values());
		return allTyreList;
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

		for (Tyre t : getAllTyres().values()) {
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
	
	public ArrayList<Tyre> getTyresByIDs(Collection<Integer> c) {
		ArrayList<Tyre> tyres = new ArrayList<Tyre>();
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
		getAllTyres().put(tyre.getDynamicID(), tyre);
	}
}
