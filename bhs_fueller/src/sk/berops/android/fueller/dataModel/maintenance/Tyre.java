package sk.berops.android.fueller.dataModel.maintenance;

import java.util.Date;
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
	@Element(name="threadLevel", required=false)
	private double threadLevel; //in mm
	@Element(name="threadMin", required=false)
	private int threadMin; //in mm
	@Element(name="threadMax", required=false)
	private int threadMax; //in mm
	@Element(name="mileageDriveAxle", required=false)
	private double mileageDriveAxle;
	private double mileageDriveAxleSI;
	@Element(name="mileageNonDriveAxle", required=false)
	private double mileageNonDriveAxle;
	private double mileageNonDriveAxleSI;
	
	private static final int TEMP_DEFAULT_THREAD_LEVEL = 9; //in mm
	
	public Tyre() {
		this(TEMP_DEFAULT_THREAD_LEVEL);
		if (getCreationDate() == null) {
			setCreationDate(new Date());
		}
	}
	
	public Tyre(int threadLevel) {
		super();
		this.threadLevel = threadLevel;
		if (threadLevel == TEMP_DEFAULT_THREAD_LEVEL) {
			this.setCondition(Condition.NEW);
		} else {
			this.setCondition(Condition.USED);
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
	
	public int getYear() {
		String string = getDot().substring(2, 4);
		int year = Integer.valueOf(string);
		if (year > 50) {
			year +=1900;
		} else {
			year +=2000;
		}
		return year;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}

	public int getWearLevel() {
		/* returns wear level in percentages; new tyres = 100%; used tyres < 100% */
		return (int) ((getThreadMax() - getThreadLevel()) / (getThreadMax() - getThreadMin()) * 100);
	}

	public double getMileageDriveAxle() {
		return mileageDriveAxle;
	}

	public void setMileageDriveAxle(double mileageDriveAxle) {
		this.mileageDriveAxle = mileageDriveAxle;
	}

	public double getMileageDriveAxleSI() {
		return mileageDriveAxleSI;
	}

	public void setMileageDriveAxleSI(double mileageDriveAxleSI) {
		this.mileageDriveAxleSI = mileageDriveAxleSI;
	}

	public double getMileageNonDriveAxle() {
		return mileageNonDriveAxle;
	}

	public void setMileageNonDriveAxle(double mileageNonDriveAxle) {
		this.mileageNonDriveAxle = mileageNonDriveAxle;
	}

	public double getMileageNonDriveAxleSI() {
		return mileageNonDriveAxleSI;
	}

	public void setMileageNonDriveAxleSI(double mileageNonDriveAxleSI) {
		this.mileageNonDriveAxleSI = mileageNonDriveAxleSI;
	}

	public double getThreadLevel() {
		return threadLevel;
	}

	public void setThreadLevel(double threadLevel) {
		this.threadLevel = threadLevel;
	}

	public int getThreadMin() {
		return threadMin;
	}

	public void setThreadMin(int threadMin) {
		this.threadMin = threadMin;
	}

	public int getThreadMax() {
		return threadMax;
	}

	public void setThreadMax(int threadMax) {
		this.threadMax = threadMax;
	}
}