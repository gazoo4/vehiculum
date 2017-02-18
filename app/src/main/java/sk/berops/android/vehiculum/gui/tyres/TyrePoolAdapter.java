package sk.berops.android.vehiculum.gui.tyres;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.LinkedList;

import sk.berops.android.vehiculum.R;
import sk.berops.android.vehiculum.dataModel.maintenance.Tyre;

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
