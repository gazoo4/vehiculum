package sk.berops.android.fueller.dataModel.expense;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import sk.berops.android.fueller.dataModel.maintenance.ReplacementPart;

public class MaintenanceEntry extends Entry {
	@Element(name="type")
	private Type type;
	@Element(name="laborCost", required=false)
	private double laborCost;
	@ElementList(inline=true, required=false)
	private LinkedList<ReplacementPart> parts;
	public MaintenanceEntry() {
		super();
		this.setExpenseType(Entry.ExpenseType.MAINTENANCE);
	}
	
	public enum Type{
		PLANNED(0, "planned"),
		UNPLANNED(1, "unplanned"),
		ACCIDENT_REPAIR(2, "accident repair");
		private int id;
		private String type;	
		Type(int id, String type) {
			this.setId(id);
			this.setType(type);
		}
		
		private static Map<Integer, Type> idToTypeMapping;

		public static Type getType(int id) {
			if (idToTypeMapping == null) {
				initMapping();
			}
			
			Type result = null;
			result = idToTypeMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToTypeMapping = new HashMap<Integer, MaintenanceEntry.Type>();
			for (Type type : values()) {
				idToTypeMapping.put(type.id, type);
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
	
	public int compareTo(MaintenanceEntry e) {
		return super.compareTo(e);
	}
	
	public double getLaborCost() {
		return laborCost;
	}
	public void setLaborCost(double laborCost) {
		this.laborCost = laborCost;
	}
	public LinkedList<ReplacementPart> getParts() {
		return parts;
	}
	public void setParts(LinkedList<ReplacementPart> parts) {
		this.parts = parts;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
