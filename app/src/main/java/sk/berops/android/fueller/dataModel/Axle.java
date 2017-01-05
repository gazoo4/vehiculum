package sk.berops.android.fueller.dataModel;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.MainActivity;

public class Axle implements Serializable {
	
	@Element(name="axleType")
	private AxleType axleType;
	@Element(name="drivable")
	private boolean drivable;

	/**
	 * TyreIDs belonging to the tyres from left to right (from the birds perspective view).
	 * Null means there's an empty slot for the tyre.
	 */
	@ElementList(inline=true, required=false)
	private ArrayList<UUID> tyreIDs;

	private Car car;
	
	public Axle() {
		this(AxleType.STANDARD);
	}
	
	public Axle(AxleType type) {
		super();
		setAxleType(type);
		createTyres();
	}
	
	public void initAfterLoad(Car car) {
		Tyre tyre;
		this.car = car;
		for (UUID id : tyreIDs) {
			tyre = MainActivity.garage.getTyreById(id);
			tyre.initAfterLoad(car);
		}
	}
	
	public enum AxleType{
		STANDARD(0, "standard"), 
		TANDEM(1, "tandem"), //tandem axis for trucks
		SINGLE(2, "single"); //for motorbikes or three-wheelers
		private int id;
		private String axleType;	
		AxleType(int id, String axleType) {
			this.setId(id);
			this.setAxleType(axleType);
		}
		
		private static Map<Integer, AxleType> idToAxleTypeMapping;

		public static AxleType getAxleType(int id) {
			if (idToAxleTypeMapping == null) {
				initMapping();
			}
			
			AxleType result = null;
			result = idToAxleTypeMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToAxleTypeMapping = new HashMap<Integer, AxleType>();
			for (AxleType type : values()) {
				idToAxleTypeMapping.put(type.id, type);
			}
		}
	
		public String getAxleType() {
			return axleType;
		}
		public void setAxleType(String axleType) {
			this.axleType = axleType;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}
	
	private void createTyres() {
		tyreIDs = new ArrayList<>();
		switch (getAxleType()) {
		case SINGLE:
			tyreIDs.add(null);
			break;
		case STANDARD:
			tyreIDs.add(null);
			tyreIDs.add(null);
			break;
		case TANDEM:
			tyreIDs.add(null);
			tyreIDs.add(null);
			tyreIDs.add(null);
			tyreIDs.add(null);
			break;
		default:
			break;
		}
	}
	
	public AxleType getAxleType() {
		return axleType;
	}
	public void setAxleType(AxleType axleType) {
		this.axleType = axleType;
	}

	public boolean isDrivable() {
		return drivable;
	}

	public void setDrivable(boolean drivable) {
		this.drivable = drivable;
	}

	public ArrayList<UUID> getTyreIDs() {
		return tyreIDs;
	}

	public void setTyreIDs(ArrayList<UUID> tyreIDs) {
		this.tyreIDs = tyreIDs;
	}
	
	public ArrayList<Tyre> getTyres() {
		return MainActivity.garage.getTyresByIDs(getTyreIDs());
	}
	
	public void installTyre(Tyre tyre, int position) {
		getTyreIDs().add(position, tyre.getUuid());
	}
}
