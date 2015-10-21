package sk.berops.android.fueller.dataModel;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.MainActivity;

public class Axle {
	
	@Element(name="axleType")
	private AxleType axleType;
	@Element(name="drivable")
	private boolean drivable;
	/**
	 * TyreIDs belonging to the tyres from left to right (from the birds perspective view)
	 */
	@ElementList(inline=true, required=false)
	private ArrayList<Integer> tyreIDs;

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
		for (Integer i : tyreIDs) {
			tyre = MainActivity.garage.getAllTyres().get(i);
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
		tyreIDs = new ArrayList<Integer>();
		switch (getAxleType()) {
		case SINGLE:
			tyreIDs.add(-1);
			break;
		case STANDARD:
			tyreIDs.add(-1);
			tyreIDs.add(-1);
			break;
		case TANDEM:
			tyreIDs.add(-1);
			tyreIDs.add(-1);
			tyreIDs.add(-1);
			tyreIDs.add(-1);
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

	public ArrayList<Integer> getTyreIDs() {
		return tyreIDs;
	}

	public void setTyreIDs(ArrayList<Integer> tyreIDs) {
		this.tyreIDs = tyreIDs;
	}
	
	public ArrayList<Tyre> getTyres() {
		return MainActivity.garage.getTyresByIDs(getTyreIDs());
	}
	
	public void installTyre(Tyre tyre, int position) {
		getTyreIDs().add(position, tyre.getDynamicID());
	}
}
