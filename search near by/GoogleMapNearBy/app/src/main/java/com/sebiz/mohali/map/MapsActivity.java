package com.sebiz.mohali.map;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This is the api which return near by every thig ypu wnat to search and place details
 * https://maps.googleapis.com/maps/api/place/textsearch/json?query=KFC%20int%20mohali&type=restaurant|cafe&key=AIzaSyA6J-gUcxdxCavSrtiNX3Sv9EBdHAR-vPk
 * <p>
 * <p>
 * https://maps.googleapis.com/maps/api/place/textsearch/json?query=ATM%20int%20mohali&type=SBI|PNB&key=AIzaSyA6J-gUcxdxCavSrtiNX3Sv9EBdHAR-vPk
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJe5eEjYjuDzkRx9F8kCXBa7Y&key=AIzaSyA6J-gUcxdxCavSrtiNX3Sv9EBdHAR-vPk
 */
public class MapsActivity extends FragmentActivity {
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    EditText edttype, edtlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        edttype = (EditText) findViewById(R.id.edttype);
        edtlocation = (EditText) findViewById(R.id.edtlocation);

        setUpMapIfNeeded();

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mMap.clear();
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                Marker marker;
                marker = mMap.addMarker(new MarkerOptions().position(loc));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                // new MyLocationDetailsTask().execute(latitude + "", longitude + "");

            }
        });


        findViewById(R.id.btnsearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = edttype.getText().toString();
                String location = edtlocation.getText().toString();

                new SearchTask().execute(type,location);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }


    }


    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

//        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String providername = locationManager.getBestProvider(criteria,true);
//        locationManager.requestLocationUpdates(providername,5000,6,this);

    }


    class MyLocationDetailsTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            double lat = Double.parseDouble(params[0]);
            double lnt = Double.parseDouble(params[1]);
            Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lnt, 1);
                String data = addresses.get(0).getPostalCode() + "-" + addresses.get(0).getCountryCode() + "" + addresses.get(0).getCountryName();
                Log.e("Location Details", data);
            } catch (Exception ex) {
                Log.e("Exception", ex.toString());
            }
            return null;
        }
    }


    class SearchTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
            String query = "query="+params[0]+"%20int%20"+params[1];
            String type = "&type=restaurant|cafe";
            String key = "&key=AIzaSyA6J-gUcxdxCavSrtiNX3Sv9EBdHAR-vPk";
            url = url+query+type+key;
            Log.e("ULR",url);
            try{
                URL url1 = new URL(url);
                URLConnection connection = (URLConnection)url1.openConnection();
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String jsonData = br.readLine();
                while (jsonData!=null){
                    sb.append(jsonData);
                    jsonData = br.readLine();
                }
            }
            catch (Exception ex){
                Log.e("Request response error",ex.toString());
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            LocationBaen bean;
            ArrayList<LocationBaen> list = new ArrayList<LocationBaen>();
            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i =0;i<jsonArray.length();i++){
                    bean = new LocationBaen();
                    JSONObject object = jsonArray.getJSONObject(i);
                    String formatted_address = object.getString("formatted_address");
                    String place_id = object.getString("place_id");
                    Log.e(formatted_address,place_id);
                    bean.setPlace_id(place_id);
                    bean.setFormatted_address(formatted_address);
                    list.add(bean);
                }

            }
            catch (Exception ex){
                Log.e("Parsing Error",ex.toString());
            }

            Intent it = new Intent(MapsActivity.this,SearchDataList.class);
            it.putExtra("listData",list);
            startActivity(it);
        }
    }

}
