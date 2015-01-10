package sk.berops.android.fueller.dataModel.maintenance;

import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Element;

public class Tyre extends GenericPart {

	@Element(name="model", required=false)
	private String model;
	@Element(name="width", required=false)
	private int width;
	@Element(name="height", required=false)
	private int height;
	@Element(name="diameter", required=false)
	private double diameter;
	@Element(name="weightIndex", required=false)
	private int weightIndex;
	@Element(name="speedIndex", required=false)
	private int speedIndex;
	@Element(name="dot", required=false)
	private String dot;
	@Element(name="season", required=false)
	private Season season;
	@Element(name="wearLevel", required=false)
	private int wearLevel; //in percentages
	
	public Tyre() {
		this(0);
	}
	
	public Tyre(int wearLevel) {
		super();
		this.wearLevel = wearLevel;
		if (wearLevel == 0) {
			this.setUsed(false);
		} else {
			this.setUsed(true);
		}
	}
	
	public enum Season{
		SUMMER(0, "summer"),
		ALL_SEASON(1, "all season"),
		WINTER(2, "winter"),
		SPIKES(3, "winter with spikes");
		private int id;
		private String season;	
		Season(int id, String season) {
			this.setId(id);
			this.setSeason(season);
		}
		
		private static Map<Integer, Season> idToSeasonMapping;

		public static Season getSeason(int id) {
			if (idToSeasonMapping == null) {
				initMapping();
			}
			
			Season result = null;
			result = idToSeasonMapping.get(id);
			return result;
		}
		
		private static void initMapping() {
			idToSeasonMapping = new HashMap<Integer, Tyre.Season>();
			for (Season season : values()) {
				idToSeasonMapping.put(season.id, season);
			}
		}
	
		public String getSeason() {
			return season;
		}
		public void setSeason(String season) {
			this.season = season;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getDiameter() {
		return diameter;
	}

	public void setDiameter(double diameter) {
		this.diameter = diameter;
	}

	public int getWeightIndex() {
		return weightIndex;
	}

	public void setWeightIndex(int weightIndex) {
		this.weightIndex = weightIndex;
	}

	public int getSpeedIndex() {
		return speedIndex;
	}

	public void setSpeedIndex(int speedIndex) {
		this.speedIndex = speedIndex;
	}

	public String getDot() {
		return dot;
	}

	public void setDot(String dot) {
		this.dot = dot;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}

	public int getWearLevel() {
		return wearLevel;
	}

	public void setWearLevel(int wearLevel) {
		this.wearLevel = wearLevel;
	}
}