package sk.berops.android.fueller.configuration;

import java.io.Serializable;

import org.simpleframework.xml.Element;

public class UserSettings implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -686042542962253237L;

	private static UserSettings instance = null;
	
	@Element(name="unitSettings", required=false)
	private UnitSettings unitSettings;
	
	private UserSettings() {
		unitSettings = UnitSettings.getInstance();
	}
	
	public static UserSettings getInstance() {
		if (instance == null) {
			instance = new UserSettings();
		}
		return instance;
	}

	public UnitSettings getUnitSettings() {
		return unitSettings;
	}

	public void setUnitSettings(UnitSettings unitSettings) {
		this.unitSettings = unitSettings;
	}
}
