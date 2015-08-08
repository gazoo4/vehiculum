package sk.berops.android.fueller.gui.report;

import sk.berops.android.fueller.gui.ActivityEntryAdd;
import sk.berops.android.fueller.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActivityReportsNavigate extends Activity {
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats_show);
		
		attachGuiObjects();
	}
	
	public void attachGuiObjects() {
	}
	
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.activity_stats_show_button_fuelling:
			startActivity(new Intent(this, ActivityEntriesShow.class));
			break;
		case R.id.activity_stats_show_button_charts:
			startActivity(new Intent(this, ActivityCharts.class));
			break;
		}
	}
}
