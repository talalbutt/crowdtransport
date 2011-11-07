package com.coctelmental.android.project1886;

import com.coctelmental.android.project1886.logic.ControllerUsers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity{   

    private TextView tvProfileName;
    
    private ControllerUsers controllerU;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        // get a instance of our controller
        controllerU = new ControllerUsers();
        
        tvProfileName = (TextView) findViewById(R.id.profileName);
        
        Button bBusLocation = (Button) findViewById(R.id.buttonBus);
        bBusLocation.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent intent;
				intent = new Intent(getApplicationContext(), BusLineSelection.class);
				startActivity(intent);				
			}
		});
        
        Button bCollaboration = (Button) findViewById(R.id.buttonCollaborate);
        bCollaboration.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Intent intent;
				// if collaborationTrackingService is running
				if (MyApplication.getInstance().isServiceRunning(CollaborationTrackingService.class.getName())) {
					//intent = CollaborationTrackingService.getNotificationIntent();
					intent = new Intent(getApplicationContext(), CollaborationInformationPanel.class);
				}
				else
					intent = new Intent(getApplicationContext(), CollaborationLineSelection.class);
					
				startActivity(intent);
			}
		});
        
        Button bTaxiService = (Button) findViewById(R.id.buttonTaxi);
        bTaxiService.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent;
				intent = new Intent(getApplicationContext(), TaxiRouteSpecification.class);
				startActivity(intent);								
			}
		});
    }
      
	@Override
	public void onResume() {
		super.onResume();		
		// check if exist a user logged into the application 
        if(controllerU.existActiveUser())
        {
        	String userName = controllerU.getActiveUser().getId();
        	// show user's name
        	tvProfileName.setText(getString(R.string.profile_welcome)+" "+userName);
        }
        else
        	// remove profile information from main panel
            tvProfileName.setText("");
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// clear previous menu configuration
		menu.clear();

		MenuInflater inflater = getMenuInflater();
		if(!controllerU.existActiveUser())		
			inflater.inflate(R.menu.main_activity_menu, menu);
		else
			inflater.inflate(R.menu.main_activity_logged_menu, menu);
		
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {		
		Intent intent;
		
		switch(item.getItemId()) {
			case R.id.menuLogin:
				// Login panel launcher
				intent = new Intent(this, Authentication.class);
				startActivity(intent);
				break;
			case R.id.menuRegistration:
				// Registration panel launcher
				intent = new Intent(this, Registration.class);
				startActivity(intent);
				break;
			case R.id.menuExit:
				// logout and exit
				controllerU.logOut();
				moveTaskToBack(true);
				break;
			}
		return super.onMenuItemSelected(featureId, item);
	}

	
	
	
}