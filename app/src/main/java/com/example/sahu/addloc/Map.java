package com.example.sahu.addloc;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
public class Map extends Activity {
    private static final float DEFAULT_ZOOM = 15;
    GoogleMap mMap;
    Marker marker;
    double lat,lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        if(initMap()){
            //Toast.makeText(this,"Map Ready",Toast.LENGTH_SHORT).show();

            gotoLocation(12.9539974, 77.6309395, 3);
            mMap.setMyLocationEnabled(true);


        }
    }

    public void geoLocate(View v) throws IOException {

        hideKeyboard(v);
        EditText searchIn = (EditText)findViewById(R.id.searchBox);
        String location = searchIn.getText().toString();
        if(location.isEmpty()){
            Toast.makeText(this,"Enter the address !!",Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location, 1);



        Address add = list.get(0);
         String loc = add.getLocality();


         lat = add.getLatitude();
         lon = add.getLongitude();
        gotoLocation(lat,lon,DEFAULT_ZOOM);
         if (marker != null){
            marker.remove();
        }

       // Toast.makeText(this,lat+" and "+lon,Toast.LENGTH_SHORT).show();

        MarkerOptions options = new MarkerOptions()
                .title(loc)
                .position(new LatLng(lat,lon))
                .draggable(true);
        marker = mMap.addMarker(options);

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Vibrator v1= (Vibrator) Map.this.getSystemService(Context.VIBRATOR_SERVICE);
                v1.vibrate(150);
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }
            @Override
            public void onMarkerDragEnd(Marker marker) {
                Geocoder gc = new Geocoder(Map.this);
                List<Address> list = null;
                LatLng ll = marker.getPosition();
                try{
                    list = gc.getFromLocation(ll.latitude,ll.longitude,1);
                    lat=ll.latitude;
                    lon=ll.longitude;
                }catch (IOException e){
                    e.printStackTrace();
                    return;
                }
                Address add = list.get(0);
                marker.setTitle(add.getLocality());
                marker.setSnippet(add.getCountryName());
                marker.showInfoWindow();
            }
        });


    }

    private void gotoLocation(double lat, double lng, float zoom){
        LatLng ll = new LatLng(lat,lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mMap.moveCamera(update);
    }

    private boolean initMap(){
        if(mMap == null){
            MapFragment mapFrag = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
            mMap = mapFrag.getMap();
        }
        return (mMap != null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id==android.R.id.home) {
            this.finish();
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent=new Intent();
            String lat1= String.valueOf(lat);
            String lon1= String.valueOf(lon);
            // Toast.makeText(this,lat+"and "+lon,Toast.LENGTH_LONG).show();
            if(lat==0.0 && lon==0.0){
                Location myLocation  = mMap.getMyLocation();
                lat=myLocation.getLatitude();
                lon=myLocation.getLongitude();
            }
            intent.putExtra("Lat",lat);
            intent.putExtra("Lon",lon);
            setResult(2,intent);
            finish();

        }


        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }
}
