package sk.berops.android.fueller.gui.maintenance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

import sk.berops.android.fueller.R;
import sk.berops.android.fueller.dataModel.maintenance.Tyre;

public class TyrePoolAdapter extends ArrayAdapter<Tyre> {
	
	private Context context;
	private ArrayList<Tyre> tyres;
	
	public TyrePoolAdapter(Context context, ArrayList<Tyre> tyres) {
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
