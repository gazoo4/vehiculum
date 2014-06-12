package sk.berops.android.fueller.engine.calculation;

import sk.berops.android.fueller.engine.charts.HistoryViewData;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

public class ChartInterpolator {
	public static HistoryViewData[] applySpline(HistoryViewData[] data, int resolution) {
		HistoryViewData[] dataInterpolated = new HistoryViewData[resolution];
		
		double[] x = new double[data.length];
		double[] y = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			x[i] = data[i].getX();
			y[i] = data[i].getY();
		}
		
		SplineInterpolator interpolator = new SplineInterpolator();
		PolynomialSplineFunction f = interpolator.interpolate(x, y);
		
		double initX = data[0].getX();
		double rangeX = data[data.length - 1].getX() - data[0].getX();
		
		// when mapping 5-item long field to 8-item long field, the indexes need to be stretched by a factor of 7/4
		double multiplicator = (resolution - 1) / (data.length - 1);
		for (int i = 0; i < data.length; i++) {
			dataInterpolated[(int) Math.round(multiplicator * i)] = data[i];
		}
		
		// interpolate the empty fields
		for (int i = 0; i < dataInterpolated.length; i++) {
			if (dataInterpolated[i] == null) {
				double newX = initX + (rangeX/dataInterpolated.length * i); 
				dataInterpolated[i] = new HistoryViewData(newX, f.value(newX));
			}
		}
		
		double previous = dataInterpolated[0].getX();
		for (int i = 1; i < dataInterpolated.length; i++) {
			if (previous > dataInterpolated[i].getX()) {
				// fix would be to sort by X, but why not doing that properly from beginning?
				System.out.println("TROUBLE");
			}
			previous = dataInterpolated[i].getX();
		}
		
		// return the interpolated data
		return dataInterpolated;
	}
}
