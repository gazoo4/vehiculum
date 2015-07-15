package sk.berops.android.fueller.dataModel.maintenance;

import org.simpleframework.xml.Element;

import sk.berops.android.fueller.dataModel.Currency;

public abstract class GenericItem {
	
	@Element(name="producer", required=false)
	private String producer;
	@Element(name="partID", required=false)
	private String partID;
	@Element(name="price", required=false)
	private double price;
	@Element(name="currency", required=false)
	private Currency.Unit currency;
	@Element(name="comment", required=false)
	private String comment;
	//@Element(category="category")
	//private ??? category 
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public String getPartID() {
		return partID;
	}
	public void setPartID(String partID) {
		this.partID = partID;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Currency.Unit getCurrency() {
		return currency;
	}
	public void setCurrency(Currency.Unit currency) {
		this.currency = currency;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
