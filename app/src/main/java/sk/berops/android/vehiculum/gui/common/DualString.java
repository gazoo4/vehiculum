package sk.berops.android.vehiculum.gui.common;

import android.util.Log;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bernard Halas
 * @date 10/5/17
 */

public class DualString {
	private final CharSequence mCollapsed;
	private final CharSequence mExpanded;

	public static List<DualString> of(List<CharSequence> shorts, List<CharSequence> longs) {
		List<DualString> result = new LinkedList<>();
		Iterator<CharSequence> is = shorts.iterator();
		Iterator<CharSequence> il = longs.iterator();
		while (is.hasNext() && il.hasNext()) {
			result.add(new DualString(is.next(), il.next()));
		}

		if (is.hasNext() || il.hasNext()) {
			Log.d(DualString.class.toString(), "Uneven lengths of short and long lists");
		}

		return result;
	}

	public static List<CharSequence> collapsed(List<DualString> duals) {
		return duals.stream()
				.map(dual -> dual.getCollapsed())
				.collect(Collectors.toList());
	}

	public static List<CharSequence> expanded(List<DualString> duals) {
		return duals.stream()
				.map(dual -> dual.getExpanded())
				.collect(Collectors.toList());
	}

	public DualString(CharSequence collapsed, CharSequence expanded) {
		mCollapsed = collapsed;
		mExpanded = expanded;
	}

	public CharSequence getCollapsed() {
		return mCollapsed;
	}

	public CharSequence getExpanded() {
		return mExpanded;
	}
}
