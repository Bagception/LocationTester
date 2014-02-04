package de.uniulm.bagception.location;

import de.uniulm.bagception.intentservicecommunication.MyResultReceiver;
import de.uniulm.bagception.intentservicecommunication.MyResultReceiver.Receiver;
import de.uniulm.bagception.services.ServiceNames;
import de.uniulm.bagception.services.attributes.Calendar;
import de.uniulm.bagception.services.attributes.OurLocation;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements Receiver{

	
	private MyResultReceiver mResultreceiver;
	private TextView providerTV;
	private TextView accuracyTV;
	private TextView longitudeTV;
	private TextView latitudeTV;
	private Button requestButton;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		providerTV = (TextView) findViewById(R.id.providerTV);
		accuracyTV = (TextView) findViewById(R.id.accuracyTV);
		longitudeTV = (TextView) findViewById(R.id.longitudeTV);
		latitudeTV = (TextView) findViewById(R.id.latitudeTV);
		
		requestButton = (Button) findViewById(R.id.getLocationButton);
		requestButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestLocation();
			}
		});
		
		mResultreceiver = new MyResultReceiver(new Handler());
		mResultreceiver.setReceiver(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		log("answer received!");
		if(resultData.containsKey(OurLocation.RESPONSE_TYPE)){
			if(resultData.getString(OurLocation.RESPONSE_TYPE).equals(OurLocation.LOCATION)){
				providerTV.setText(resultData.getString(OurLocation.PROVIDER, ""));
				accuracyTV.setText(""+resultData.getFloat(OurLocation.ACCURACY, 0));
				latitudeTV.setText(""+resultData.getDouble(OurLocation.LATITUDE, 0));
				longitudeTV.setText(""+resultData.getDouble(OurLocation.LONGITUDE, 0));
			}
		}
		
		
		
	}
	
	private void requestLocation(){
		String serviceString = ServiceNames.LOCATION_SERVICE;
		Intent i = new Intent(serviceString);
		i.putExtra(OurLocation.REQUEST_TYPE, OurLocation.GETLOCATION);
		i.putExtra("receiverTag", mResultreceiver);
		log("sending request...");
		startService(i);
	}
	
	@Override
	protected void onDestroy() {
		String serviceString = ServiceNames.LOCATION_SERVICE;
		Intent i = new Intent(serviceString);
		stopService(i);
		super.onDestroy();
	}

	
	private void log(String s){
		Log.d("LocationTester", s);
	}
}
