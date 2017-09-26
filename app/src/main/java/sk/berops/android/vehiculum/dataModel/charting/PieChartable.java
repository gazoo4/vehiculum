package sk.berops.android.vehiculum.dataModel.charting;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

/**
 * @author Bernard Halas
 * @date 9/20/17
 *
 * Interfaced used for channeling the data into PieCharts
 */

public interface PieChartable {
	ArrayList<PieEntry> getPieChartVals();
	ArrayList<Integer> getPieChartColors();
	String getPieChartLabel();
	boolean invokeSelection(Integer selection);
}
