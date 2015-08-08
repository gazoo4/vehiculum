package sk.berops.android.fueller.dataModel.expense;

import sk.berops.android.fueller.dataModel.expense.Entry.ExpenseType;

public class OtherEntry extends Entry {

	public OtherEntry() {
		super();
		setExpenseType(ExpenseType.OTHER);
	}
}
