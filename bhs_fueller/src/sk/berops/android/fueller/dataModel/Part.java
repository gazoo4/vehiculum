package sk.berops.android.fueller.dataModel;

import org.simpleframework.xml.Element;

public class Part {
	@Element(name="price", required=false)
	private double price;
	@Element(name="condition", required=false)
	private int condition; //100 = new, 0 = totaled
	@Element(name="performanceAffecting", required=false)
	private boolean performanceAffecting; //or consumption
	@Element(name="oem", required=false)
	private boolean oem; //or aftermarket
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getCondition() {
		return condition;
	}
	public void setCondition(int condition) {
		this.condition = condition;
	}
	public boolean isPerformanceAffecting() {
		return performanceAffecting;
	}
	public void setPerformanceAffecting(boolean performanceAffecting) {
		this.performanceAffecting = performanceAffecting;
	}
	public boolean isOem() {
		return oem;
	}
	public void setOem(boolean oem) {
		this.oem = oem;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	private String name;
	private String manufacturer;
	private String productID;
}
