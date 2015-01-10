package sk.berops.android.fueller.gui.maintenance;

import java.util.ArrayList;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.Axle;
import sk.berops.android.fueller.dataModel.Car;
import sk.berops.android.fueller.dataModel.expense.TyreChangeEntry;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.MainActivity;
import sk.berops.android.fueller.gui.common.GuiUtils;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ActivityTyreChangeScheme extends Activity {
	
	private Car car;
	private TyreChangeEntry tyreChangeEntry;
	
	private LinearLayout tyreChangeScheme;
	private ListView listView;
	TyrePoolAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_tyre_change_scheme);
		car = MainActivity.garage.getActiveCar();
		tyreChangeEntry = ActivityTyreChange.tyreChangeEntryStatic;
		super.onCreate(savedInstanceState);
		
		buildDynamicLayout();
	}
	
	private void buildDynamicLayout() {
		View graphics = new ViewTyreChangeGraphics(this, car);
		
		tyreChangeScheme = (LinearLayout) findViewById(R.id.activity_tyre_change_graphical_layout);
		tyreChangeScheme.removeAllViews();
		tyreChangeScheme.addView(graphics);
		
		listView = (ListView) findViewById(R.id.activity_tyre_change_tyre_pool_list_view);
		for (int i = 0; i < 10; i++) {
			Tyre tyre = new Tyre();
			tyre.setWearLevel((int) (100 * Math.random())); 
			MainActivity.garage.addTyre(tyre);
		}
		adapter = new TyrePoolAdapter(this, MainActivity.garage.getTyres());
		listView.setAdapter(adapter);
	}
}