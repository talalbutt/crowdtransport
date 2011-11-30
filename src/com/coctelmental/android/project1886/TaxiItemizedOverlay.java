package com.coctelmental.android.project1886;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coctelmental.android.project1886.common.TaxiDriver;
import com.coctelmental.android.project1886.common.util.JsonHandler;
import com.coctelmental.android.project1886.logic.ControllerUsers;
import com.coctelmental.android.project1886.model.ResultBundle;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class TaxiItemizedOverlay extends ItemizedOverlay<OverlayItem>{

	private Context mContext;
	private ArrayList<OverlayItem> aOverlays;
	
	private TaxiDriverInfoAsyncTask taxiDriverInfoAsyncTask;
	
	private AlertDialog dialog;
	private View layoutInformationOverlay;
		
	public TaxiItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenter(defaultMarker));
		aOverlays = new ArrayList<OverlayItem>();
		mContext = context;
		
		// get custom overlay layout
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInformationOverlay = (View) inflater.inflate(R.layout.user_taxi_information_overlay, null);;
		Button bSelectTaxiDriver = (Button) layoutInformationOverlay.findViewById(R.id.buttonSelectTaxi);
		
		// setup button
		bSelectTaxiDriver.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		//setup overlay alert dialog
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
		dialogBuilder.setTitle(R.string.taxiDriverInformation);
		dialogBuilder.setView(layoutInformationOverlay);
		dialog = dialogBuilder.create();
	}
	
	public void addOverlay(OverlayItem overlay) {
		aOverlays.add(overlay);
	}

	@Override
	protected OverlayItem createItem(int i) {
		return aOverlays.get(i);
	}

	@Override
	public int size() {
		return aOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem item = aOverlays.get(index);
		// show overlay dialog
		dialog.show();
		
		// cancel previous launched async task if it's running
		if (taxiDriverInfoAsyncTask != null)
			taxiDriverInfoAsyncTask.cancel(true);
		// launch async task adding taxi driver id as param
		taxiDriverInfoAsyncTask = new TaxiDriverInfoAsyncTask();
		taxiDriverInfoAsyncTask.execute(item.getTitle());
		return true;
	}
	
	public void populateNow() {
		populate();
	}
	
	private class TaxiDriverInfoAsyncTask extends AsyncTask<String, Void, ResultBundle> {

		protected void onPreExecute () {
			// hide other layout if they are being shown
			layoutInformationOverlay.findViewById(R.id.labelsContainer).setVisibility(View.GONE);
			layoutInformationOverlay.findViewById(R.id.labelNoInfoTaxi).setVisibility(View.GONE);
			// show progress bar
			layoutInformationOverlay.findViewById(R.id.labelLoadingBar).setVisibility(View.VISIBLE);
		} 
	
	    protected ResultBundle doInBackground(String... params) {
			// send request to server and return response code
	        return getTaxiDriverInfo(params[0]);
	    }	    

	    protected void onPostExecute(ResultBundle rb) {		
			// check result
			if (rb.getResultCode() == HttpURLConnection.HTTP_OK) {
				String jsonUser = rb.getContent();
				TaxiDriver taxiDriver = JsonHandler.fromJson(jsonUser, TaxiDriver.class);
				
				// get overlay textviews
				TextView tvTaxiDriverName = (TextView) layoutInformationOverlay.findViewById(R.id.labelTaxiDriverName);
				TextView tvCarName = (TextView) layoutInformationOverlay.findViewById(R.id.labelTaxiDriverCar);
				// fill textviews
				tvTaxiDriverName.setText(taxiDriver.getFullName());
				tvCarName.setText(taxiDriver.getCarTradeMark() + " " + taxiDriver.getCarModel());
				
				// hide progress bar
				layoutInformationOverlay.findViewById(R.id.labelLoadingBar).setVisibility(View.GONE);
				// show taxi driver info
				layoutInformationOverlay.findViewById(R.id.labelsContainer).setVisibility(View.VISIBLE);
			}
			else {
				// hide progress bar
				layoutInformationOverlay.findViewById(R.id.labelLoadingBar).setVisibility(View.GONE);
				// show error message
				layoutInformationOverlay.findViewById(R.id.labelNoInfoTaxi).setVisibility(View.VISIBLE);
			}
	    }
	}
	
	private ResultBundle getTaxiDriverInfo(String taxiDriverID) {
		ResultBundle rb = null;
		ControllerUsers controllerU = new ControllerUsers();
		rb = controllerU.getTaxiDriver(taxiDriverID);
		return rb;
	}

}
