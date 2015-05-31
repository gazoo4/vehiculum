package sk.berops.android.fueller.gui.maintenance;

import android.os.Bundle;
import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.expense.Entry;
import sk.berops.android.fueller.dataModel.expense.TyreChangeEntry;
import sk.berops.android.fueller.gui.common.ActivityAddEventGeneric;

public class ActivityTyreAdd extends ActivityAddEventGeneric {
	
	protected TyreChangeEntry tyreChangeEntry;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_tyre_add);
		if (tyreChangeEntry == null) {
			tyreChangeEntry = new TyreChangeEntry();
		}
		tyreChangeEntry.setExpenseType(Entry.ExpenseType.TYRES);
		super.entry = (Entry) this.tyreChangeEntry;
		super.editMode = editMode;
		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void attachGuiObjects() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void styleGuiObjects() {
		// TODO Auto-generated method stub

	}

}
