package sk.berops.android.vehiculum.dataModel.expense;

import sk.berops.android.vehiculum.engine.synchronization.controllers.BureaucraticEntryController;

public class BureaucraticEntry extends Entry {
	
	public BureaucraticEntry() {
		super();
		setExpenseType(ExpenseType.BUREAUCRATIC);
	}

	/****************************** Controller-relevant methods ***********************************/

	/**
	 * This method creates and provides a controller that will do all the synchronization updates on this object
	 * @return controller
	 */
	@Override
	public BureaucraticEntryController getController() {
		return new BureaucraticEntryController(this);
	}
}
