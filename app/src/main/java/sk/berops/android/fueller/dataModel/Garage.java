package sk.berops.android.fueller.dataModel;

import android.util.Log;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.TyreChangeEntry;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.dataModel.tags.Tag;

@Root
public class Garage {
	
	@ElementList(inline = true, required = false)
	private LinkedList<Car> cars;

	/**
	 * The list of tyres bought, used and trashed for the entire history of the garage
	 */
	@ElementList(inline = true, required = false)
	private ArrayList<Tyre> tyreInventory;

	@Element(name = "activeCar", required = false)
	private int activeCarId;

	@Element(name = "rootTag", required = false)
	private Tag rootTag;

	public Garage() {
		super();
		cars = new LinkedList<Car>();
		tyreInventory = new ArrayList<Tyre>();
		activeCarId = -1;

		Tag root = getRootTag();
		if (root == null) {
			root = new Tag();
			root.setName("rootTag");
			root.setDepth(0);
			root.setParent(null);
			setRootTag(root);
		}
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

	/**
	 * Provide the full tyre inventory, collecting all the tyres from all the entries
	 * @return ArrayList<Tyre> collection of tyres
	 */
	public ArrayList<Tyre> getAllTyres() {
		if (tyreInventory == null) {
			tyreInventory = new ArrayList<Tyre>();
		}
		return tyreInventory;
	}

	/**
	 * Based on the supplied identifier, return the matching tyre
	 * @param id identifier
	 * @return Tyre instance of the matching tyre. If tyre not found, returns null.
	 */
	public Tyre getTyreById(long id) {
		for (Tyre t : getAllTyres()) {
			if (id == t.getId()) {
				return t;
			}
		}

		return null;
	}

	/**
	 * Get a snapshot of the tyre inventory from just before a specific date
	 * @param date from which a snapshot is required
	 * @return ArrayList<Tyre> tyre inventory snapshot
	 */
	public ArrayList<Tyre> getTyreInventoryToDate(Date date) {
		ArrayList<Tyre> inventory = new ArrayList<Tyre>();
		Tyre t;

		if (date == null) {
			Log.d("DEBUG", "Called getTyreInventoryToDate(null). Returning null.");
			return null;
		}

		TyreChangeEntry last = null;
		// Need to scan all tyre change entries for added/removed tyres
		for (TyreChangeEntry e : getActiveCar().getHistory().getTyreChangeEntries()) {
			last = e;
			if (e.getEventDate().compareTo(date) < 0) {

				// Add bought tyres to the list of available tyres
				for (Long id : e.getBoughtTyresIDs()) {
					t = getTyreById(id);
					if (t != null) {
						inventory.add(t);
					}
				}

				// Remove trashed tyres from the list of available tyres
				for (Long id : e.getDeletedTyreIDs()) {
					t = getTyreById(id);
					if (t != null) {
						inventory.remove(t);
					}
				}
			}

			// Events are sorted chronologically. Once we get over the event date, we don't need to consider the newer events
			break;
		}

		// Need to remove the tyres from this list, which are installed on the car now
		if (last != null) {
			for (Axle a : last.getTyreScheme().getAxles()) {
				for (Long id : a.getTyreIDs()) {
					if (id != null) {
						t = getTyreById(id);
						inventory.remove(t);
					}
				}
			}
		}

		return inventory;
	}

	/**
	 * Get a snapshot of the tyre inventory from just before a specific entry occurred
	 * @param entry determining the event
	 * @return ArrayList<Tyre> tyre inventory snapshot
	 */
	public ArrayList<Tyre> getTyreInventoryToEntry(Entry entry) {
		return getTyreInventoryToDate(entry.getEventDate());
	}

	/**
	 * Method used to get the list of available tyres in the garage at this moment
	 * @return ArrayList<Tyre> of available tyres
	 */
	public ArrayList<Tyre> getAvailableTyres() {
		return getTyreInventoryToDate(new Date());
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

		// Init tyres
		for (Tyre t : getAllTyres()) {
			// We want to make sure that all tyres are initialized
			// If a tyre is installed on a car, it will get re-initialized with
			// the correct car reference through the below FOR loop
			t.initAfterLoad(null);
		}

		// Init cars
		for (Car c : cars) {
			c.initAfterLoad();
			for (Entry e : c.getHistory().getEntries()) {
				e.setDynamicId(dynamicId++);
			}
		}

		//Init tag tree
		this.rootTag.initAfterLoad();
	}

	public ArrayList<Tyre> getTyresByIDs(Collection<Long> c) {
		ArrayList<Tyre> tyres = new ArrayList<Tyre>();
		for (Long l : c) {
			if (l == null) {
				tyres.add(null);
				continue;
			}
			tyres.add(getTyreById(l));
		}
		return tyres;
	}

	public void addNewTyre(Tyre tyre) {
		tyreInventory.add(tyre);
	}

	/**
	 * The root tag for the tag tree structure relevant to this garage
	 */
	public Tag getRootTag() {
		return rootTag;
	}

	public void setRootTag(Tag rootTag) {
		this.rootTag = rootTag;
	}
}
