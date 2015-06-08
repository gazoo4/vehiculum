package sk.berops.android.fueller.dataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import sk.berops.android.fueller.dataModel.maintenance.Tyre;

public class Axle {

	@Element(name="axleType")
	private AxleType axleType;
	@Element(name="drivable")
	private boolean drivable;
	@ElementList(inline=true, required=false)
	private ArrayList<Tyre> tyres; //tyres from left to right (from the birds perspective view)
	
	public Axle() {
		this(AxleType.STANDARD);
	}
	
	public Axle(AxleType type) {
		super();
		setAxleType(type);
		createTyres();
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
		tyres = new ArrayList<Tyre>();
		switch (getAxleType()) {
		case SINGLE:
			tyres.add(new Tyre());
			break;
		case STANDARD:
			tyres.add(new Tyre());
			tyres.add(new Tyre());
			break;
		case TANDEM:
			tyres.add(new Tyre());
			tyres.add(new Tyre());
			tyres.add(new Tyre());
			tyres.add(new Tyre());
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

	public ArrayList<Tyre> getTyres() {
		return tyres;
	}

	public void setTyres(ArrayList<Tyre> tyres) {
		this.tyres = tyres;
	}
}
