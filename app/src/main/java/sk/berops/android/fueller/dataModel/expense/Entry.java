package sk.berops.android.fueller.dataModel.expense;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.calculation.Consumption;
import sk.berops.android.fueller.dataModel.tags.Tag;
import sk.berops.android.fueller.dataModel.tags.Taggable;

public abstract class Entry extends Expense implements Comparable<Entry>, Taggable {
    private int dynamicId;
    private Consumption consumption;

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
        super.generateSI();
        setMileage(getMileage());
        setCost(getCost(), getCurrency());
    }

    public int compareTo(Entry e) {
        return Double.valueOf(this.getMileage()).compareTo(Double.valueOf(e.getMileage()));
    }

    public int getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(int dynamicId) {
        this.dynamicId = dynamicId;
    }

    public Consumption getConsumption() {
        return consumption;
    }

    public void setConsumption(Consumption consumption) {
        this.consumption = consumption;
    }

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
}