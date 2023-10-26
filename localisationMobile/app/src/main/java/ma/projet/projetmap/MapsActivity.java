package ma.projet.projetmap;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private RequestQueue requestQueue;

    private String insertUrl = "http://192.168.0.134/localisation/showPosition.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestQueue = Volley.newRequestQueue(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("bouchra", "test");

        mMap = googleMap;

        JSONObject jsonRequest = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, insertUrl, jsonRequest,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("positions");
                        Log.d("Response", jsonArray.toString());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject position = jsonArray.getJSONObject(i);
                            String latitudeStr = position.getString("latitude");
                            String longitudeStr = position.getString("longitude");

                            double latitude = Double.parseDouble(latitudeStr);
                            double longitude = Double.parseDouble(longitudeStr);

                            LatLng location = new LatLng(latitude, longitude);
                            Log.d("Latitude", String.valueOf(latitude));
                            Log.d("Longitude", String.valueOf(longitude));

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitude, longitude))
                                    .title("Marker"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                        }

                    } catch (Exception e) {

                    }
                },
                error -> {
                    Log.d("ErrorMap", error.toString() + error.getMessage());
                }
        );
        requestQueue.add(jsonObjectRequest);


    }
}