package gps.lab.kmh.mygps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.BreakIterator;


public class zoomActivity extends Activity {



    TextView logView;

    static final LatLng SEOUL = new LatLng( 37.56, 126.97);

    private GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        //Marker seoul = map.addMarker(new MarkerOptions().position(SEOUL).title("Seoul"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        Intent intent = getIntent(); //전달된 인텐트
        TextView logView = (TextView) findViewById(R.id.textViewLog);
        logView.setText(intent.getStringExtra("TextIn"));

    }

    public void findLocate(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                    double lat = location.getLatitude();
                    double lng = location.getLongitude();

                    LatLng CURRENT_LOCATION = new LatLng(lat, lng);
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT_LOCATION, 16));
                    Marker seoul = map.addMarker(new MarkerOptions().position(CURRENT_LOCATION).title("CURRNT_LOCATION"));

                    logView.setText("(" + lat + " , " + lng + ")");

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                logView.setText("onStatusChanged");
            }

            public void onProviderEnabled(String provider) {
                logView.setText("잠시만 기다려주세요");
            }

            public void onProviderDisabled(String provider) {
                logView.setText("GPS기능을 켜주세요");
            }
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


    }
}
