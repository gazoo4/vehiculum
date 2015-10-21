package sk.berops.android.fueller.gui.maintenance;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import sk.berops.android.fueller.dataModel.maintenance.Tyre;
import sk.berops.android.fueller.gui.common.TyreDrawer;

public class ViewTyreGraphics extends View implements Runnable {
	private Tyre tyre;
	private TyreDrawer tyreDrawer;
	private TyreSchemeHelper helper;
	private TyreGUIContainer tyreGUIContainer;
	private Context context;
	
	public ViewTyreGraphics(Context context, Tyre tyre) {
		super(context);
		this.context = context;
		this.tyre = tyre;
		tyreDrawer = TyreDrawer.getInstance();
		helper = TyreSchemeHelper.getInstance();
		tyreGUIContainer = new TyreGUIContainer(context, tyre, 0, 0, 0, 0, null, 0);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int x = getWidth();
		int y = getHeight();
		tyreGUIContainer.setWidth((int) (y * 0.5));
		tyreGUIContainer.setHeight(y);
		tyreDrawer.drawTyre(canvas, tyreGUIContainer);
		postDelayed(this, 10);
	}

	@Override
	public void run() {
		invalidate();
	}
}
