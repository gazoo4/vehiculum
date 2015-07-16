package sk.berops.android.fueller.dataModel.maintenance;

import org.simpleframework.xml.Element;

import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.Record;

public abstract class GenericItem extends Record {
	
	@Element(name="producer", required=false)
	private String producer;
	@Element(name="producerPartID", required=false)
	private String producerPartID;
	@Element(name="carmakerPartID", required=false)
	private String carmakerPartID;
	@Element(name="price", required=false)
	private double price;
	@Element(name="currency", required=false)
	private Currency.Unit currency;
	//@Element(category="category")
	//private ??? category 
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public String getProducerPartID() {
		return producerPartID;
	}
	public void setProducerPartID(String producerPartID) {
		this.producerPartID = producerPartID;
	}
	public String getCarmakerPartID() {
		return carmakerPartID;
	}
	public void setCarmakerPartID(String carmakerPartID) {
		this.carmakerPartID = carmakerPartID;
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
}
