package sk.berops.android.vehiculum.dataModel.expense;

import sk.berops.android.vehiculum.dataModel.charting.BurreaucraticCharter;
import sk.berops.android.vehiculum.dataModel.charting.PieCharter;
import sk.berops.android.vehiculum.engine.calculation.NewGenBureaucraticConsumption;
import sk.berops.android.vehiculum.engine.synchronization.controllers.BureaucraticEntryController;

public class BureaucraticEntry extends Entry {
	
	public BureaucraticEntry() {
		super();
		setExpenseType(ExpenseType.BUREAUCRATIC);
	}

	public NewGenBureaucraticConsumption getBureaucraticConsumption() {
		return (NewGenBureaucraticConsumption) this.getConsumption();
	}

	public NewGenBureaucraticConsumption generateConsumption() {
		return new NewGenBureaucraticConsumption();
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

	/****************************** PieChartable interface methods follow *************************/

	public PieCharter getPieCharter() {
		return (charter == null) ? generatePieCharter() : charter;
	}

	public BurreaucraticCharter generatePieCharter() {
		return new BurreaucraticCharter(getBureaucraticConsumption());
	}
}
