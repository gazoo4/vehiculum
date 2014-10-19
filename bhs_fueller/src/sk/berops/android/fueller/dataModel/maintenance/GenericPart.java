package sk.berops.android.fueller.dataModel.maintenance;

import org.simpleframework.xml.Element;

public abstract class GenericPart extends GenericItem {
	@Element(name="used")
	private boolean used;

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
}
