package sk.berops.android.fueller.dataModel;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.MainActivity;

public class Axle implements Serializable {
	
	@Element(name="type")
	private Type type;
	@Element(name="drivable")
	private boolean drivable;

	/**
	 * TyreIDs belonging to the tyres from left to right (from the birds perspective view).
	 * Null means there's an empty slot for the tyre.
	 */
	@ElementList(required=false, inline=true, empty=false)
	private ArrayList<UUID> tyreIDs;

	private Car car;

	/**
	 * Empty constructor for serialization/de-serialization purposes
	 */
	public Axle() {
		super();
		setType(Type.STANDARD);
		setTyreIDs(new ArrayList<UUID>());
	}
	
	public Axle(Type type) {
		super();
		setType(type);
		createTyres();
	}
	
	public void initAfterLoad(Car car) {
		this.car = car;
		for (Tyre tyre: MainActivity.garage.getTyresByIDs(tyreIDs)) {
			if (tyre != null) {
				tyre.initAfterLoad(car);
			}
		}
	}
	
	public enum Type {
		STANDARD(0, "standard"), 
		TANDEM(1, "tandem"), //tandem axis for trucks
		SINGLE(2, "single"); //for motorbikes or three-wheelers
		private int id;
		private String type;
		Type(int id, String type) {
			this.setId(id);
			this.setType(type);
		}
		
		private static Map<Integer, Type> idToAxleTypeMapping;

		public static Type getType(int id) {
			if (idToAxleTypeMapping == null) {
				initMapping();
			}
			
			Type result = null;
			result = idToAxleTypeMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToAxleTypeMapping = new HashMap<Integer, Type>();
			for (Axle.Type type : values()) {
				idToAxleTypeMapping.put(type.id, type);
			}
		}
	
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
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
		switch (getType()) {
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
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
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

	/**
	 * Method to return Tyres installed on this axle.
	 * @return tyres installed on the axle
	 */
	public ArrayList<Tyre> getTyres() {
		return MainActivity.garage.getTyresByIDs(getTyreIDs());
	}
	
	public void installTyre(Tyre tyre, int position) {
		getTyreIDs().set(position, tyre.getUuid());
	}
}
