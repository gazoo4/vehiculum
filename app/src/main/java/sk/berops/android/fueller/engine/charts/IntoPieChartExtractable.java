package sk.berops.android.fueller.engine.charts;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * Created by bernard.halas on 12/11/2015.
 * <p/>
 * This interface ensures that we're able to extract the data values and the legend information from the class for a use in charts
 */
public interface IntoPieChartExtractable {

    /**
     * @return ArrayList<String> consisting of the legends of the recorded child consumption instances
     */
    public ArrayList<String> getPieChartLegend();

    /**
     * @return DataSet containing the values from the child consumption instances
     */
    public ArrayList<Entry> getPieChartVals();

	/**
	 * Setter for pieChartVals
	 * @param pieChartVals
	 */
	public void setPieChartVals(ArrayList<Entry> pieChartVals);

    /**
     * @return ArrayList of colors for the displayable data
     */
    public ArrayList<Integer> getPieChartColors();
}