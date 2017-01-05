package sk.berops.android.fueller.dataModel;

import android.util.Log;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.History;
import sk.berops.android.fueller.dataModel.expense.TyreChangeEntry;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.dataModel.tags.Tag;
import sk.berops.android.fueller.gui.MainActivity;

@Root
public class Garage {
	
	@ElementList(inline = true, required = false)
	private LinkedList<Car> cars;

	@Element(name = "activeCar", required = false)
	private int activeCarId;

	@Element(name = "rootTag", required = false)
	private Tag rootTag;

	public Garage() {
		super();
		cars = new LinkedList<Car>();
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
		ArrayList<Tyre> tyres = new ArrayList<>();

		for (Car c: getCars()) {
			for (TyreChangeEntry e: c.getHistory().getTyreChangeEntries()) {
				tyres.addAll(e.getBoughtTyres());
			}
		}

		return tyres;
	}

	/**
	 * Based on the supplied identifier, return the matching tyre
	 * @param id identifier
	 * @return Tyre instance of the matching tyre. If tyre not found, returns null.
	 */
	public Tyre getTyreById(UUID id) {
		for (Tyre t : getAllTyres()) {
			if (id == t.getUuid()) {
				return t;
			}
		}

		return null;
	}

	/**
	 * Get a snapshot of the tyre inventory (i.e. available tyres - not installed and not trashed)
	 * from just before a specific date
	 * @param dateTo date to which a snapshot is required
	 * @return ArrayList<Tyre> tyre inventory snapshot
	 */
	public ArrayList<Tyre> getAvailableTyresToDate(Date dateTo) {
		ArrayList<Tyre> availableTyres = new ArrayList<Tyre>();
		ArrayList<UUID> deletedTyre = new ArrayList<>();

		LinkedList<TyreChangeEntry> entries;
		for (Car c: MainActivity.garage.getCars()) {
			// Iterate through all the cars
			entries = c.getHistory().getTyreChangeEntries();
			// Filter the entries to defined date
			History.filterEntriesByDate(entries, null, dateTo);
			for (TyreChangeEntry e: entries) {
				// For entry add the bought tyres
				availableTyres.addAll(e.getBoughtTyres());
				// If the tyre has been trashed by the user, it's not available
				availableTyres.removeAll(e.getDeletedTyres());
				}

			// Also, if the tyres are installed on a car, they are not available
			if (entries.size() > 0) {
				availableTyres.removeAll(entries.getLast().getTyreScheme().getInstalledTyres());
			}
		}

		return availableTyres;
	}

	/**
	 * Get a snapshot of the tyre inventory from just before a specific entry occurred
	 * @param entry determining the event
	 * @return ArrayList<Tyre> tyre inventory snapshot
	 */
	public ArrayList<Tyre> getAvailableTyresToEntry(Entry entry) {
		if (entry == null) {
			return getAvailableTyresToDate(null);
		}
		return getAvailableTyresToDate(entry.getEventDate());
	}

	/**
	 * Method used to get the list of available tyres in the garage at this moment
	 * @return ArrayList<Tyre> of available tyres
	 */
	public ArrayList<Tyre> getAvailableTyres() {
		return getAvailableTyresToDate(null);
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

	/**
	 * Get a list of tyres from the supplied list of tyre UUIDs
	 * @param uuids
	 * @return
	 */
	public ArrayList<Tyre> getTyresByIDs(Collection<UUID> uuids) {
		ArrayList<Tyre> tyres = new ArrayList<>();
		for (UUID u : uuids) {
			if (u == null) {
				tyres.add(null);
				continue;
			}
			tyres.add(getTyreById(u));
		}
		return tyres;
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
