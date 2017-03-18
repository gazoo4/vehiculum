package sk.berops.android.vehiculum.engine.charts;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

/**
 * Created by bernard.halas on 12/11/2015.
 * <p/>
 * This interface ensures that we're able to extract the data values and the legend information from the class for a use in charts
 */
public interface IntoPieChartExtractable {

    /**
     * @return DataSet containing the values from the child consumption instances
     */
    ArrayList<PieEntry> getPieChartVals();

	/**
	 * Setter for pieChartVals
	 * @param pieChartVals
	 */
	void setPieChartVals(ArrayList<PieEntry> pieChartVals);

    /**
     * @return ArrayList of colors for the displayable data
     */
    ArrayList<Integer> getPieChartColors();

	/**
	 * @return the label for the legend (i.e. current focus + cost, e.g. fuel: 3500,-)
	 */
	String getPieChartLabel(int depth);
}