package com.coctelmental.android.project1886;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coctelmental.android.project1886.common.GeoPointInfo;
import com.coctelmental.android.project1886.common.util.JsonHandler;
import com.coctelmental.android.project1886.model.ServiceRequestInfo;
import com.coctelmental.android.project1886.util.Tools;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;

public class TaxiDriverRouteView extends MapActivity {

	public static final String SERVICE_REQUEST = "SERVICE_REQUEST";
	private static final int INIT_ZOOM_LEVEL = 17;

	private MyLocationOverlay myLocationOverlay;
	
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.taxi_driver_route_view);
		
        // setup map configuration
	    MapView mapView = (MapView) findViewById(R.id.mapTaxiRoute);	    
	    mapView.setBuiltInZoomControls(true);
        mapView.setEnabled(true);
        mapView.setSatellite(false);
        mapView.setTraffic(false);	
        mapView.setStreetView(false);
        
        // get serviceRequestInfo
        Bundle extras = getIntent().getExtras();
        String jsonServiceRequest = null;
        if (extras != null)
        	jsonServiceRequest = extras.getString(SERVICE_REQUEST);
        
        ServiceRequestInfo serviceRequest = null;
        if (jsonServiceRequest == null) {
        	Tools.buildToast(this, getString(R.string.errorGettingServiceRequestInfo), Gravity.CENTER, Toast.LENGTH_SHORT).show();
        	finish();
        }
        else {
        	Log.e("re", jsonServiceRequest);
	        serviceRequest = JsonHandler.fromJson(jsonServiceRequest, ServiceRequestInfo.class);
	
	        GeoPointInfo gpOri = serviceRequest.getGpOrigin();
	        GeoPointInfo gpDest = serviceRequest.getGpDestination();
	        
	        // setup overlays
	        OverlayItem overlayOri = new OverlayItem(new GeoPoint(gpOri.getLatitudeE6(), gpOri.getLongitudeE6()),
	        			"", "");       
	        OverlayItem overlayDest = new OverlayItem(new GeoPoint(gpDest.getLatitudeE6(), gpDest.getLongitudeE6()),
	    				"", "");
        	
			// calculating distance
			Double distance = Tools.calculateDistanceInMeters(overlayOri.getPoint(), overlayDest.getPoint());
			// update distance label
			TextView tvDistance = (TextView) findViewById(R.id.distance);
			DecimalFormat df = new DecimalFormat("#######0.0#");
			tvDistance.setText(" "+df.format(distance)+"m");
	        
	        // add overlays
	        MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay(getResources().getDrawable(R.drawable.marker_ori));
	        itemizedOverlay.addOverlay(overlayOri);
	        itemizedOverlay.addOverlay(overlayDest, getResources().getDrawable(R.drawable.marker_dest));
	        itemizedOverlay.populateNow();
	    
	        mapView.getOverlays().add(itemizedOverlay);
	        
	        // setup zoom
	        mapView.getController().setCenter(overlayOri.getPoint());
	        mapView.getController().setZoom(INIT_ZOOM_LEVEL);
	        
	        // redraw map
	        mapView.invalidate();
	    
	        // show taxi driver location
	        myLocationOverlay = new MyLocationOverlay(this, mapView);
	        mapView.getOverlays().add(myLocationOverlay);
	        myLocationOverlay.enableMyLocation(); 
	        
	        Button bAcceptServiceRequest = (Button) findViewById(R.id.bAcceptRequest);
	        bAcceptServiceRequest.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					// TO-DO
					Tools.buildToast(getApplicationContext(), "TO-DO", Gravity.CENTER, Toast.LENGTH_SHORT).show();
				}
			});
	        
        }
	}

	@Override
	protected void onResume() {
		myLocationOverlay.enableMyLocation();
		super.onResume();
	}

	@Override
	protected void onPause() {
		myLocationOverlay.disableMyLocation();
		super.onPause();
	}
	
	private class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
		private ArrayList<OverlayItem> overlays;
		
		public MyItemizedOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
			overlays = new ArrayList<OverlayItem>();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return overlays.get(i);
		}

		@Override
		public int size() {
			return overlays.size();
		}
		
		public void addOverlay(OverlayItem overlay) {
			overlays.add(overlay);
		}
		
		public void addOverlay(OverlayItem overlay, Drawable  marker) {
			marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
			overlay.setMarker(boundCenterBottom(marker));
			overlays.add(overlay);
		}
		
		public void populateNow() {
			populate();
		}
	}

}
