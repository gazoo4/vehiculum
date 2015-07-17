package sk.berops.android.fueller.dataModel.maintenance;

import org.simpleframework.xml.Element;

import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.Currency;
import sk.berops.android.fueller.dataModel.Currency.Unit;
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
	@Element(name="priceSI", required=false)
	private double priceSI;
	@Element(name="currency", required=false)
	private Currency.Unit currency;
	//@Element(category="category")
	//private ??? category 
	
	Car car;
	
	public void initAfterLoad(Car car) {
		setCar(car);
		if (getCurrency() == null) {
			setCurrency(Currency.Unit.EURO);
		}
		
		generateSI();
	}
	
	private void generateSI() {
		setPrice(getPrice(), getCurrency());
	}
	
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
	public void setPrice(double price, Unit currency) {
		this.price = price;
		this.currency = currency;
		setPriceSI(Currency.convertToSI(getPrice(), getCurrency(), getCreationDate()));
	}
	public double getPriceSI() {
		return priceSI;
	}
	public void setPriceSI(double priceSI) {
		this.priceSI = priceSI;
	}
	public Currency.Unit getCurrency() {
		return currency;
	}
	public void setCurrency(Currency.Unit currency) {
		this.currency = currency;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}
}
