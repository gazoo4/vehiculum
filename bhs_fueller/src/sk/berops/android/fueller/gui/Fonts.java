package sk.berops.android.fueller.gui;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

public class Fonts {

	public static Typeface getFont(Context c) {
		return Typeface.createFromAsset(c.getAssets(), "fonts/Action_Man.ttf");
	}
	
	public static Typeface getFontBook(Context c) {
		return Typeface.createFromAsset(c.getAssets(), "fonts/Aquifer.ttf");
	}
	
	public static Typeface getFontPort(Context c) {
		return Typeface.createFromAsset(c.getAssets(), "fonts/Portmanteau.ttf");
	}
	
	public static Typeface getFontBold(Context c) {
		return Typeface.createFromAsset(c.getAssets(), "fonts/Action_Man_Bold.ttf");
	}
	
	public static Typeface getOldFont(Context c) {
		return Typeface.createFromAsset(c.getAssets(), "fonts/ChopinScript.ttf");
	}
	
	public static Typeface getGomariceFont(Context c) {
		return Typeface.createFromAsset(c.getAssets(), "fonts/gomarice_old_book.ttf");
	}
}
