package sk.berops.android.vehiculum.dataModel.expense;

import sk.berops.android.vehiculum.engine.synchronization.controllers.OtherEntryController;

public class OtherEntry extends Entry {

	public OtherEntry() {
		super();
		setExpenseType(ExpenseType.OTHER);
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
