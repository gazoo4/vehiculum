package sk.berops.android.vehiculum.dataModel.expense;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Car;
import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.calculation.Consumption;
import sk.berops.android.vehiculum.dataModel.tags.Tag;
import sk.berops.android.vehiculum.dataModel.tags.Taggable;
import sk.berops.android.vehiculum.engine.calculation.NewGenConsumption;
import sk.berops.android.vehiculum.engine.synchronization.controllers.EntryController;

public abstract class Entry extends Expense implements Comparable<Entry>, Taggable {
    private int dynamicId;
    private NewGenConsumption consumption;

    /**
     * Reference to the Car to which this Entry is attached to.
     */
    protected Car car;

    /**
     * Structure to hold the tags assigned to this entry.
     */
    @ElementList(name = "tagUUIDs", required = false)
    private ArrayList<UUID> tagUuids;

    @Element(name = "mileage")
    private double mileage;
    private double mileageSI;
    @Element(name = "expenseType")
    private ExpenseType expenseType;

    public Entry() {
        clearTags();
    }

    @Override
    public void generateSI() {
        setMileage(getMileage());
    }

	/**
	 * Overriding the compareTo method from Comparable interface in order to correctly compare Entries
	 * @param e entry to be compared
	 * @return -1 if this Entry is older than the one from the argument
	 * 1 if this Entry is newer than the one from the argument
	 * 0 if both are equally chronologically old
	 */
	@Override
    public int compareTo(Entry e) {
	    // Mileage is the main comparison variable
	    int result = Double.valueOf(this.getMileage()).compareTo(Double.valueOf(e.getMileage()));
	    if (result == 0) {
		    // EventDate is the secondary comparison variable
		    result = this.getEventDate().compareTo(e.getEventDate());
	    }
	    if (result == 0) {
		    // CreationDate is the tertiary comparison variable
		    result = this.getCreationDate().compareTo(getCreationDate());
	    }
        return result;
    }

    public int getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(int dynamicId) {
        this.dynamicId = dynamicId;
    }

    public NewGenConsumption getConsumption() {
	    if (consumption == null) {
		    consumption = generateConsumption();
	    }
	    return consumption;
    }

    public void setConsumption(NewGenConsumption consumption) {
        this.consumption = consumption;
    }

    public abstract NewGenConsumption generateConsumption();

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
        setMileageSI(mileage * car.getDistanceUnit().getCoef());
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(ExpenseType expenseType) {
        this.expenseType = expenseType;
    }

    public double getMileageSI() {
        return mileageSI;
    }

    public void setMileageSI(double mileageSI) {
        this.mileageSI = mileageSI;
    }



    public enum ExpenseType {
        FUEL(0, "fuel", 0xFFDA3BB0), //fossil fuels, electricity
        MAINTENANCE(1, "maintenance", 0xFF5046C4), //any maintenance action
        SERVICE(2, "service", 0xFFBCCF30), //tow service, replacement car service
        TYRES(3, "tyres", 0xFF3A3A3A),
        TOLL(4, "toll", 0xFFCF9730), //ferry fee, highway vignette...
        INSURANCE(5, "insurance", 0xFF30CFBC), //basic insurance or extended insurance
        BUREAUCRATIC(6, "bureaucratic", 0xFFCF8030), //technical compliancy check fee (TUEV), import tax, eco class tax,...
        OTHER(Integer.MAX_VALUE, "other", 0xFFB168A9);

        private static Map<Integer, ExpenseType> idToExpenseTypeMapping;
        private int id;
        private String expenseType;
        private int color;

        ExpenseType(int id, String expenseType) {
            this(id, expenseType, ((int) (Math.random() * Integer.MAX_VALUE) | 0xFF000000));
        }

        ExpenseType(int id, String expenseType, int color) {
            this.setId(id);
            this.setExpenseType(expenseType);
            this.setColor(color);
        }

        public static ExpenseType getExpenseType(int id) {
            if (idToExpenseTypeMapping == null) {
                initMapping();
            }

            ExpenseType result = null;
            result = idToExpenseTypeMapping.get(id);
            return result;
        }

        private static void initMapping() {
            idToExpenseTypeMapping = new HashMap<Integer, ExpenseType>();
            for (ExpenseType expenseType : values()) {
                idToExpenseTypeMapping.put(expenseType.id, expenseType);
            }
        }

        public String getExpenseType() {
            return expenseType;
        }

        public void setExpenseType(String expenseType) {
            this.expenseType = expenseType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

    /**
     * Add a new tag to the list of tags for this Entry
     * @param tag to be added
     */
    public void addTag(Tag tag) {
        //tagIds.add(tag.getId());
	    tagUuids.add(tag.getUuid());

    }

    /**
     * Remove a tag from the list of tags attached to this Entry
     * @param tag to be removed
     */
    public void removeTag(Tag tag) {
	    tagUuids.remove(tag.getUuid());
    }

    /**
     * Clear the list of tags IDs attached to this Entry
     */
    public void clearTags() {
	    tagUuids = new ArrayList<>();
    }

    /**
     * Get the list of tags IDs for the relevant tags attached to this Entry
     * @return list of tags
     */
	public ArrayList<UUID> getTagUuids() {
		return tagUuids;
	}

    /**
     * Set the list of tags relevant to this Entry
     * @param tagUuids list of tags
     */
	public void setTagUuids(ArrayList<UUID> tagUuids) {
		this.tagUuids = tagUuids;
	}

    /**
     * Get the tags associated with this entry based on the known tag IDs
     * @return ArrayList of the tags
     */
    public ArrayList<Tag> getTags() {
	    ArrayList<Tag> tags = new ArrayList<>();
	    for (UUID id : getTagUuids()) {
		    tags.add(Tag.getTag(id));
	    }
	    return tags;
    }
    /**
     * Set the tag associated to this entry based on the provided list of tags
     * @param tags
     */
    public void setTags(ArrayList<Tag> tags) {
	    ArrayList<UUID> tagUuids = new ArrayList<>();
	    for (Tag tag : tags) {
		    tagUuids.add(tag.getUuid());
	    }
	    this.tagUuids = tagUuids;
    }

	/****************************** Controller-relevant methods ***********************************/

	/**
	 * This method creates and provides a controller that will do all the synchronization updates on this object
	 * @return controller
	 */
	@Override
	public EntryController getController() {
		return new EntryController(this);
	}

	/****************************** Searchable interface methods follow ***************************/

	/**
	 * Method used to search for an object by its UUID within the Object tree of this Object.
	 * @param uuid of the searched object
	 * @return Record that matches the searched UUID
	 */
	public Record getRecordByUUID(UUID uuid) {
		// Are they looking for me? Delegate task to Record.getRecordByUUID to find out.
		Record result = super.getRecordByUUID(uuid);

		// There's nothing more in this to search for UUIDs. The tagUUIDs Linked-List has been
		// parsed directly from the Garage object, no need to parse the same tags again.
		return result;
	}
}