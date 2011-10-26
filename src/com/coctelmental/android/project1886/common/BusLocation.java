package com.coctelmental.android.project1886.common;

import com.coctelmental.android.project1886.common.util.JsonHandler;



public class BusLocation {

	private String busLocationID;
	private int latitude;
	private int longitude;
	private long when;
	
	public BusLocation (String id) {
		this.busLocationID = id;
	}

	public String getBusLocationID() {
		return busLocationID;
	}

	public void setBusLocationID(String busLocationID) {
		this.busLocationID = busLocationID;
	}


	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}
	
	public long getWhen() {
		return when;
	}

	public void setWhen(long when) {
		this.when = when;
	}
	
	public String toString() {
		return String.format("locationID: %s\n" +
							 "latitude: %s\n" +
							 "longitude: %s" +
							 "when: %s", busLocationID, latitude, longitude, when );
	}

	public String toJson() {
		return JsonHandler.toJson(this);	
	}

	
}
