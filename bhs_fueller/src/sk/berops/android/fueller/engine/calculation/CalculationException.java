package sk.berops.android.fueller.engine.calculation;

public class CalculationException extends Exception {
	String reason;
	
	public CalculationException(String reason) {
		super();
		this.reason = reason;
	}
	
	public String getReason() {
		return this.reason;
	}
}
