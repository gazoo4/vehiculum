package sk.berops.android.vehiculum.dataModel;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.maintenance.Tyre;
import sk.berops.android.vehiculum.gui.MainActivity;

public class Axle implements Serializable {
	
	@Element(name="type")
	private Type type;
	@Element(name="drivable")
	private boolean drivable;

	/**
	 * TyreIDs belonging to the tyres from left to right (from the birds perspective view).
	 * Null means there's an empty slot for the tyre.
	 */
	@ElementMap(required=false, inline=true)
	private TreeMap<Integer, UUID> tyreIDs;

	private Car car;

	/**
	 * Empty constructor for serialization/de-serialization purposes
	 */
	public Axle() {
		super();
		setType(Type.STANDARD);
		setTyreIDs(new TreeMap<Integer, UUID>());
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
		STANDARD(0, "standard", 2),
		TANDEM(1, "tandem", 4), //tandem axis for trucks
		SINGLE(2, "single", 1); //for motorbikes or three-wheelers
		private int id;
		private String type;
		private int tyreCount;
		Type(int id, String type, int tyreCount) {
			this.setId(id);
			this.setType(type);
			this.setTyreCount(tyreCount);
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
		private void setType(String type) {
			this.type = type;
		}
		public int getId() {
			return id;
		}
		private void setId(int id) {
			this.id = id;
		}
		public int getTyreCount() {
			return tyreCount;
		}
		private void setTyreCount(int tyreCount) {
			this.tyreCount = tyreCount;
		}
	}
	
	private void createTyres() {
		tyreIDs = new TreeMap<>();

		for (int i = 0; i < getType().getTyreCount(); i++) {
			tyreIDs.put(i, null);
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

	public TreeMap<Integer, UUID> getTyreIDs() {
		return tyreIDs;
	}

	public void setTyreIDs(TreeMap<Integer, UUID> tyreIDs) {
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
		getTyreIDs().put(position, tyre.getUuid());
	}
}
