package sk.berops.android.fueller.dataModel.maintenance;

import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Element;

public class Tyre extends GenericPart {

	@Element(name="model")
	private String model;
	@Element(name="width")
	private int width;
	@Element(name="height")
	private int height;
	@Element(name="diameter")
	private double diameter;
	@Element(name="weightIndex")
	private int weightIndex;
	@Element(name="speedIndex")
	private int speedIndex;
	@Element(name="dot")
	private String dot;
	@Element(name="season")
	private Season season;
	
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
}