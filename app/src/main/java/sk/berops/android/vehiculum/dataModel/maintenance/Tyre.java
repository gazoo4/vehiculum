package sk.berops.android.vehiculum.dataModel.maintenance;

import org.simpleframework.xml.Element;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Tyre extends GenericPart {

	@Element(name = "model", required = false)
	private String model;
	@Element(name = "width", required = false)
	private int width;
	@Element(name = "height", required = false)
	private int height;
	@Element(name = "diameter", required = false)
	private double diameter;
	@Element(name = "weightIndex", required = false)
	private int weightIndex;
	@Element(name = "speedIndex", required = false)
	private String speedIndex;
	@Element(name = "dot", required = false)
	private String dot;
	@Element(name = "season", required = false)
	private Season season;
	@Element(name = "threadLevel", required = false)
	private double threadLevel; // in mm
	@Element(name = "threadMin", required = false)
	private double threadMin; // in mm
	@Element(name = "threadMax", required = false)
	private int threadMax; // in mm
	@Element(name = "mileageDriveAxle", required = false)
	private double mileageDriveAxle;
	private double mileageDriveAxleSI;
	@Element(name = "mileageNonDriveAxle", required = false)
	private double mileageNonDriveAxle;
	private double mileageNonDriveAxleSI;

	public static final int NEW_TYRE_THREAD_LEVEL = 9; // in mm

	/**
	 * Constructor
	 */
	public Tyre() {
		this(NEW_TYRE_THREAD_LEVEL);
		if (getCreationDate() == null) {
			setCreationDate(new Date());
		}
	}

	/**
	 * Customized constructor to create a tyre with a defined thread level
	 * @param threadLevel
	 */
	public Tyre(int threadLevel) {
		this.threadLevel = threadLevel;
		if (threadLevel == NEW_TYRE_THREAD_LEVEL) {
			this.setCondition(Condition.NEW);
		} else {
			this.setCondition(Condition.USED);
		}
	}

	/**
	 * Copy constructor
	 * @param tyre
	 */
	public Tyre(Tyre tyre) {
		super(tyre);
		this.model = tyre.model;
		this.width = tyre.width;
		this.height = tyre.height;
		this.diameter = tyre.diameter;
		this.weightIndex = tyre.weightIndex;
		this.speedIndex = tyre.speedIndex;
		this.dot = tyre.dot;
		this.season = tyre.season;
		this.threadLevel = tyre.threadLevel;
		this.threadMin = tyre.threadMin;
		this.threadMax = tyre.threadMax;
		this.mileageDriveAxle = tyre.mileageDriveAxle;
		this.mileageNonDriveAxle = tyre.mileageNonDriveAxle;
	}

	public enum Season {
		SUMMER(0, "summer", 1.6), ALL_SEASON(1, "all season", 4.0), WINTER(2, "winter", 4.0), SPIKES(3,
				"winter with spikes", 4.0);
		private int id;
		private String season;
		private double minThreadLevel;

		Season(int id, String season, double minThreadLevel) {
			this.setId(id);
			this.setSeason(season);
			this.setMinThreadLevel(minThreadLevel);
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
		
		public double getMinThreadLevel() {
			return minThreadLevel;
		}
		
		public void setMinThreadLevel(double minThreadLevel) {
			this.minThreadLevel = minThreadLevel;
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

	public String getSpeedIndex() {
		return speedIndex;
	}

	public void setSpeedIndex(String speedIndex) {
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
			year += 1900;
		} else {
			year += 2000;
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
		// returns wear level in percentages; new tyres = 100%; used tyres <
		// 100%
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

	public double getThreadMin() {
		if (threadMin == 0.0) {
			setThreadMin(getSeason().getMinThreadLevel());
		}
		return threadMin;
	}

	public void setThreadMin(double threadMin) {
		this.threadMin = threadMin;
	}

	public int getThreadMax() {
		if (threadMax == 0) {
			setThreadMax(NEW_TYRE_THREAD_LEVEL);
		}
		return threadMax;
	}

	public void setThreadMax(int threadMax) {
		this.threadMax = threadMax;
	}
}