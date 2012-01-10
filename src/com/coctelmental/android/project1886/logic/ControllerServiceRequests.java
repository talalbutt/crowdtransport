package com.coctelmental.android.project1886.logic;


import com.coctelmental.android.project1886.MyApplication;
import com.coctelmental.android.project1886.common.DeviceInfo;
import com.coctelmental.android.project1886.common.util.JsonHandler;
import com.coctelmental.android.project1886.model.ResultBundle;
import com.coctelmental.android.project1886.model.ServiceRequestInfo;
import com.coctelmental.android.project1886.util.ConnectionsHandler;

public class ControllerServiceRequests {
	
	public static final int ERROR_MALFORMED_REQUEST = -1;
	
	private static final String C2DM_REGISTRATION_RESOURCE = "/c2dm-registration";
	private static final String SERVICE_REQUEST_RESOURCE = "/service-request";
	private static final String CONFIRMATION_REQUEST_RESOURCE = "/request-response";
	
	private ControllerUsers controllerU;
	
	public ControllerServiceRequests() {
		controllerU = new ControllerUsers();
	}
	
	public void createServiceRequest() {
		// get installation unique id
		String userUUID = MyApplication.getInstance().id();
		ServiceRequestInfo serviceRequestInfo = new ServiceRequestInfo(userUUID);
		
		// get logged user ID
		String userID;
		if(controllerU.existActiveUser())
			userID = controllerU.getActiveUser().getId();
		else
			userID = userUUID;
		
		serviceRequestInfo.setUserID(userID);
				
		// store info overriding previous data
		MyApplication.getInstance().storeServiceRequestInfo(serviceRequestInfo);
	}
	
	public ServiceRequestInfo getServiceRequest() {
		return MyApplication.getInstance().getServiceRequestInfo();
	}
	
	public static int sendServiceRequest(){
		int result = ERROR_MALFORMED_REQUEST;

		ServiceRequestInfo serviceRequest = MyApplication.getInstance().getServiceRequestInfo();
		if (serviceRequest != null) {
			// convert to json
			String jsonServiceRequest = JsonHandler.toJson(serviceRequest);
			result = ConnectionsHandler.put(SERVICE_REQUEST_RESOURCE, jsonServiceRequest);
		}
		
		return result;		
	}

	public static int acceptServiceRequest(String requestID){
		int result = -1;
	
		if (requestID != null) {
		    String taxiUUID = MyApplication.getInstance().id();
		    // build data
		    String[] request = {"accept", taxiUUID};
		    String jsonRequest = JsonHandler.toJson(request);
			String targetURL = CONFIRMATION_REQUEST_RESOURCE + "/" + requestID;
			result = ConnectionsHandler.post(targetURL, jsonRequest);	
		}
		
		return result;		
	}
	
	public static int cancelSentServiceRequest(){
		int result = ERROR_MALFORMED_REQUEST;
		
		ServiceRequestInfo serviceRequest = MyApplication.getInstance().getServiceRequestInfo();
		if (serviceRequest != null) {
			String taxiUUID = serviceRequest.getTaxiDriverUUID();
			String requestID = serviceRequest.getUserUUID();
			
			if (taxiUUID != null && requestID != null) {
				String targetURL = SERVICE_REQUEST_RESOURCE + "/" + taxiUUID + "/request/" + requestID;
				result = ConnectionsHandler.delete(targetURL);
			}
		}
		
		return result;		
	}
	
	public static int rejectAllServiceRequest(){
		String taxiUUID = MyApplication.getInstance().id();
		String targetURL = SERVICE_REQUEST_RESOURCE + "/" + taxiUUID;
		return ConnectionsHandler.delete(targetURL);		
	}
	
	public static ResultBundle getNewServiceRequest(String requestID){
		String taxiUUID = MyApplication.getInstance().id();
		String targetURL = SERVICE_REQUEST_RESOURCE + "/" + taxiUUID + "/request/" + requestID;
		return ConnectionsHandler.get(targetURL);	
	}
	
	public static ResultBundle getAllServiceRequest(){
		String taxiUUID = MyApplication.getInstance().id();
		String targetURL = SERVICE_REQUEST_RESOURCE + "/" + taxiUUID;
		return ConnectionsHandler.get(targetURL);	
	}
	
	public static int sendRegistrationIdToServer(String registrationID) {
		int result = -1;
		if (registrationID != null && registrationID != "") {
			// get installation UUID		
			String UUID = MyApplication.getInstance().id();
			
			// setup device info
			DeviceInfo deviceInfo = new DeviceInfo(UUID);
			deviceInfo.setRegistrationID(registrationID);
			
			// send to webservice
			String jsonDeviceInfo = JsonHandler.toJson(deviceInfo);
			result = ConnectionsHandler.post(C2DM_REGISTRATION_RESOURCE, jsonDeviceInfo);
		}
		return result;
	}

}
