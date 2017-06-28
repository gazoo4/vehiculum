package sk.berops.android.vehiculum.engine.synchronization.controllers;

import java.util.UUID;

import sk.berops.android.vehiculum.dataModel.Record;
import sk.berops.android.vehiculum.dataModel.expense.Expense;

/**
 * @author Bernard Halas
 * @date 6/22/17
 */

public class ExpenseController extends RecordController {
	private Expense expense;

	public ExpenseController(Expense expense) {
		super(expense);
		this.expense = expense;
	}

	@Override
	public boolean createRecord(Record child) throws SynchronizationException {
		boolean updated = super.createRecord(child);

		// There are no child references possible from Expense objects
		return updated;
	}

	@Override
	public boolean updateRecord(Record recordUpdate) throws SynchronizationException {
		boolean updated = super.updateRecord(recordUpdate);

		if (!(recordUpdate instanceof  Expense)) {
			logFailure(recordUpdate);
			return updated;
		}

		Expense expenseUpdate = (Expense) recordUpdate;
		if (! expense.getEventDate().equals(expenseUpdate.getEventDate())) {
			expense.setEventDate(expenseUpdate.getEventDate());
			logUpdate("eventDate");
			updated = true;
		}

		if (expense.getCost() != expenseUpdate.getCost()) {
			expense.setCost(expenseUpdate.getCost());
			logUpdate("cost");
			updated = true;
		}

		return updated;
	}

	@Override
	public boolean deleteRecord(UUID deleteUUID) throws SynchronizationException {
		boolean updated = super.deleteRecord(deleteUUID);

		// There are no child objects referenced in Expense, so nothing to delete in this scope
		return updated;
	}
}
