package sk.berops.android.fueller.dataModel;

import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Element;

import sk.berops.android.fueller.dataModel.Car.VolumeUnit;

public class Axle {

	@Element(name="axleType")
	private AxleType axleType;
	
	public Axle() {
		super();
		setAxleType(AxleType.STANDARD);
	}
	
	public Axle(AxleType type) {
		super();
		setAxleType(type);
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
	public AxleType getAxleType() {
		return axleType;
	}
	public void setAxleType(AxleType axleType) {
		this.axleType = axleType;
	}
}
