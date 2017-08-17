package sk.berops.android.vehiculum.dataModel.expense;

import sk.berops.android.vehiculum.engine.calculation.NewGenOtherConsumption;
import sk.berops.android.vehiculum.engine.synchronization.controllers.OtherEntryController;

public class OtherEntry extends Entry {

	public OtherEntry() {
		super();
		setExpenseType(ExpenseType.OTHER);
	}

	public NewGenOtherConsumption getOtherConsumption() {
		return (NewGenOtherConsumption) this.getConsumption();
	}

	public NewGenOtherConsumption generateConsumption() {
		return new NewGenOtherConsumption();
	}

	/****************************** Controller-relevant methods ***********************************/

	/**
	 * This method creates and provides a controller that will do all the synchronization updates on this object
	 * @return controller
	 */
	@Override
	public OtherEntryController getController() {
		return new OtherEntryController(this);
	}
}
