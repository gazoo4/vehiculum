package sk.berops.android.fueller.gui.common;

import java.text.DecimalFormat;

public class TextFormatter {

	public static String format(double value, String formattingRule) {
		String result;
		if (formattingRule.contentEquals("")) {
			formattingRule = "###.##";
		}
		DecimalFormat df = new DecimalFormat(formattingRule);
		result = df.format(value);
		return result;
	}
}
