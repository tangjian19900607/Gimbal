package com.tibco.gimbal;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.gimbal.android.BeaconEventListener;
import com.gimbal.android.BeaconManager;
import com.gimbal.android.BeaconSighting;
import com.gimbal.android.Communication;
import com.gimbal.android.CommunicationListener;
import com.gimbal.android.CommunicationManager;
import com.gimbal.android.Gimbal;
import com.gimbal.android.PlaceEventListener;
import com.gimbal.android.PlaceManager;
import com.gimbal.android.Push;
import com.gimbal.android.Visit;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private PlaceEventListener placeEventListener;
    private CommunicationListener communicationListener;
    private BeaconEventListener beaconSightingListener;
    private BeaconManager beaconManager;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Gimbal.setApiKey(this.getApplication(), "df0083dc-472e-4da7-89e2-fa19c8d5c3f8");
        placeEventListener = new PlaceEventListener() {
            @Override
            public void onVisitStart(Visit visit) {
                // This will be invoked when a place is entered. Example below shows a simple log upon enter
                Log.i("Info:", "Enter: " + visit.getPlace().getName() + ", at: " + new Date(visit.getArrivalTimeInMillis()));
                Toast.makeText(MainActivity.this, "onVisitStart", Toast.LENGTH_SHORT).show();
                mTextView.append("Enter " + visit.getPlace().getName());
            }

            @Override
            public void onVisitEnd(Visit visit) {
                // This will be invoked when a place is exited. Example below shows a simple log upon exit
                Log.i("Info:", "Exit: " + visit.getPlace().getName() + ", at: " + new Date(visit.getDepartureTimeInMillis()));
                Toast.makeText(MainActivity.this, "onVisitEnd", Toast.LENGTH_SHORT).show();
                mTextView.append("Exit " + visit.getPlace().getName());
            }
        };
        PlaceManager.getInstance().addListener(placeEventListener);
        communicationListener = new CommunicationListener() {
            @Override
            public Collection<Communication> presentNotificationForCommunications(Collection<Communication> communications, Visit visit) {
                for (Communication comm : communications) {
                    Log.i("INFO", "Place Communication: " + visit.getPlace().getName() + ", message: " + comm.getTitle());
                    Toast.makeText(MainActivity.this, "presentNotificationForCommunications", Toast.LENGTH_SHORT).show();
                }
                //allow Gimbal to show the notification for all communications
                return communications;
            }

            @Override
            public Collection<Communication> presentNotificationForCommunications(Collection<Communication> communications, Push push) {
                for (Communication comm : communications) {
                    Log.i("INFO", "Received a Push Communication with message: " + comm.getTitle());
                    Toast.makeText(MainActivity.this, "presentNotificationForCommunications", Toast.LENGTH_SHORT).show();
                }
                //allow Gimbal to show the notification for all communications
                return communications;
            }

            @Override
            public void onNotificationClicked(List communications) {
                Log.i("INFO", "Notification was clicked on");
                Toast.makeText(MainActivity.this, "onNotificationClicked", Toast.LENGTH_SHORT).show();
            }
        };
        CommunicationManager.getInstance().addListener(communicationListener);
        beaconSightingListener = new BeaconEventListener() {
            @Override
            public void onBeaconSighting(BeaconSighting sighting) {
                Log.i("INFO", sighting.toString());
                mTextView.setText("OnBeaconSighting " + sighting.getBeacon().getTemperature());
            }
        };
        beaconManager = new BeaconManager();
        beaconManager.addListener(beaconSightingListener);
        PlaceManager.getInstance().startMonitoring();
        beaconManager.startListening();
        mTextView = (TextView) this.findViewById(R.id.gimbal);
    }
}
