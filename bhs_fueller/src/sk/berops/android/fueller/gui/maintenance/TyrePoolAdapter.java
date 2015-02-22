package sk.berops.android.fueller.gui.maintenance;

import java.util.LinkedList;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.common.TyreDrawer;
import sk.berops.android.fueller.gui.common.GuiUtils;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class TyrePoolAdapter extends ArrayAdapter<Tyre> {
	
	private Context context;
	private LinkedList<Tyre> tyres;
	
	public TyrePoolAdapter(Context context, LinkedList<Tyre> tyres) {
		super(context,R.layout.list_tyre_change_pool, tyres);
		this.context = context;
		this.tyres = tyres;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tyre tyre = tyres.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.list_tyre_change_pool, parent, false);
		
		View tyreView = new ViewTyreGraphics(getContext(), tyre);
		 
		LinearLayout ll = (LinearLayout) rowView.findViewById(R.id.list_tyre_change_pool_layout);
		ll.addView(tyreView);
		return rowView;
	}
}
