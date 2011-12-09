package com.coctelmental.android.project1886.c2dm;

import java.net.HttpURLConnection;

import com.coctelmental.android.project1886.logic.ControllerServiceRequests;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


public class C2DMRegistrationReceiver extends BroadcastReceiver{
	
	public static final String C2DM_REGISTRATION_ACTION = "com.google.android.c2dm.intent.REGISTRATION";
	public static final String C2DM_REGISTRATION_INTENT = "com.google.android.c2dm.intent.REGISTER";
	public static final String C2DM_UNREGISTRATION_INTENT = "com.google.android.c2dm.intent.UNREGISTER";
	
    // extras in the registration callback intents.
    public static final String EXTRA_REGISTRATION_ID = "registration_id";
    public static final String EXTRA_UNREGISTERED = "unregistered";
    public static final String EXTRA_ERROR = "error";
    
    // extras in registration intents
    public static final String EXTRA_SENDER = "sender";
    public static final String EXTRA_APPLICATION_PENDING_INTENT = "app";
    public static final String SENDER_ID = "project1886@gmail.com";

	@Override
	public void onReceive(Context context, Intent intent) {
	    if (intent.getAction().equals(C2DM_REGISTRATION_ACTION)) {
	    	handleRegistration(context, intent);
	    }
	}
	
	protected void handleRegistration(Context context, Intent intent) {
		 String registrationID = intent.getStringExtra(EXTRA_REGISTRATION_ID);
		 
		 if (intent.getStringExtra(EXTRA_ERROR) != null)
			// Registration failed, should try again later.
			 Log.e("C2DM", "Error in C2DM registration process: " + intent.getStringExtra(EXTRA_ERROR));
		 else if (intent.getStringExtra(EXTRA_UNREGISTERED) != null)
			 // unregistration done, new messages from the authorized sender will be rejected
			 Log.d("C2DM", "Device succesful unregistered");
		 else if (registrationID != null) {
			 Log.d("C2DM", "Device succesful registered");
			 // register device in webservice
			 new SendRegistrationIDTask().execute(registrationID);
		 }

	}

    public static void register(Context context) {
        Intent registrationIntent = new Intent(C2DM_REGISTRATION_INTENT);
        registrationIntent.putExtra(EXTRA_APPLICATION_PENDING_INTENT,
        		 PendingIntent.getBroadcast(context, 0, new Intent(), 0));
        registrationIntent.putExtra(EXTRA_SENDER, SENDER_ID);
        // Initiate c2d messaging registration for the current application
        context.startService(registrationIntent);
    }

    public static void unregister(Context context) {
        Intent unRegistrationIntent = new Intent(C2DM_UNREGISTRATION_INTENT);
        unRegistrationIntent.putExtra(EXTRA_APPLICATION_PENDING_INTENT, PendingIntent.getBroadcast(context,
                0, new Intent(), 0));
        // unregister the application. New messages will be blocked by server.
        context.startService(unRegistrationIntent);
    }
    
	private class SendRegistrationIDTask extends AsyncTask<String, Void, Integer> {
	
	    protected Integer doInBackground(String... params) {
	    	// send device info to server
	        return ControllerServiceRequests.sendRegistrationIdToServer(params[0]);
	    }

	    protected void onPostExecute(Integer result) {
	        // check result
	        if(result == HttpURLConnection.HTTP_OK) {
	        	Log.w("C2DM", "Succesful device registration in webservice.");
	        }				
	        else {
	        	Log.e("C2DM", "Error trying device registration in webservice." +
	        			"Error code -> (" + result +")");
	        }
	    }
	}
	
}
