package com.example.pankaj.andjun2015googlemap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

//https://developers.google.com/places/webservice/search
//https://developers.google.com/places/webservice/intro
//https://developers.google.com/maps/documentation/webservices/
//https://developers.google.com/maps/documentation/geocoding/intro
//http://www.truiton.com/2015/04/using-new-google-places-api-android/
public class MainActivity extends FragmentActivity {
    GoogleMap map;
    EditText edtaddres;
    Button btnsearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtaddres = (EditText) findViewById(R.id.edtaddress);
        btnsearch = (Button) findViewById(R.id.brnsearch);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mymap);
        map = mapFragment.getMap();
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = edtaddres.getText().toString().trim();
                new MyAddressSearchTask().execute(address);
            }
        });
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMyLocationEnabled(true);
//        GoogleApiClient mGoogleApiClient= new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();

    }

    class MyAddressSearchTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Task");
            progressDialog.setMessage("data loading");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            String address = "address=" + params[0];
            String sensor = "&" + "sensor=false";
            String URL1 = "https://maps.googleapis.com/maps/api/geocode/json?";
            String mainURl = URL1 + address + sensor;

            try {

                URL uri = new URL(mainURl);
                URLConnection connection = (URLConnection) uri.openConnection();
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String data = br.readLine();
                while (data != null) {
                    sb.append(data);
                    data = br.readLine();
                }
            } catch (Exception ex) {
                Log.e("Connection Error", ex.toString());
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            PolygonOptions liOptions = new PolygonOptions();
            PolylineOptions polylineOptions = new PolylineOptions();
            ArrayList<String> latlong = getJsonData(s);
            for (int i = 0; i < latlong.size(); i++) {
                String latlong1 = latlong.get(i);
                String ltot[] = latlong1.split(",");
                double lat = Double.valueOf(ltot[0]);
                double lot = Double.valueOf(ltot[1]);
                if(i<=1){
                    if(i==0) {
                        liOptions.add(new LatLng(lat, lot));
                        polylineOptions.add(new LatLng(lat, lot));
                    }
                    else if (i==1){
                        liOptions.add(new LatLng(lat, lot));
                        polylineOptions.add(new LatLng(lat, lot));
                    }
                    else if (i==2){
                        liOptions.add(new LatLng(lat, lot));
                        polylineOptions.add(new LatLng(lat, lot));
                    }
                }
                map.addMarker(new MarkerOptions().position(new LatLng(lat, lot)));
                //map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lot)));
                //map.moveCamera(CameraUpdateFactory.zoomBy(5));

            }
            //map.addPolygon(liOptions);
            map.addPolyline(polylineOptions);

            progressDialog.dismiss();
        }
    }


    public ArrayList<String> getJsonData(String data) {
        ArrayList<String> jsonlist = new ArrayList<String>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                //JSONObject obj = object.getJSONObject("geometry");
                JSONObject geometry = object.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                String lat = location.getString("lat");
                String lng = location.getString("lng");
                jsonlist.add(lat + "," + lng);
            }
        } catch (Exception ex) {
            Log.e("Parsing Error", ex.toString());
        }


        return jsonlist;
    }
}
