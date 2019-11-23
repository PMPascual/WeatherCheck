package phillippascual.weathercheck;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //Globals
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    final String APP_ID = "f188c3a9be76b9f75ca96a9a5ae8a099";

    //Declare variables for views in this activity
    TextView _cityName;
    TextView _tempDisplay;
    ImageView _weatherImage;
    Button _changeCityButton;

    //This activity uses gps, instantiating location manager and listener.
    LocationManager _locationManager;
    LocationListener _locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _cityName = findViewById(R.id.cityName);
        _tempDisplay = findViewById(R.id.tempDisplay);
        _weatherImage = findViewById(R.id.weatherSymbol);
        _changeCityButton = findViewById(R.id.changeCityButton);

        //Setting an onclick listener on change city button
        _changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Move to new activity when made
                Intent jumpPage = new Intent(MainActivity.this, ChangeCity.class);

                startActivity(jumpPage);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent myIntent = getIntent();

        String city = myIntent.getStringExtra("City");

        if (city != null) {
            callWeatherApiForNewCity(city);
        } else {
            getWeather();
        }
    }


    private void getWeather() {
        _locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        _locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", APP_ID);
                callWeatherApi(params);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("WeatherCheck", "Provider disabled!");
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            return;
        }
        _locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                5000,
                1000,
                _locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 123) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Log.d("WeatherCheck", "Permission granted, getting the weather");
                getWeather();
            } else {
                Log.d("WeatherCheck", "Permission denied!");
            }
        }
    }

    private void callWeatherApi(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                WeatherData weatherData = WeatherData.fromJson(response);

                updateUI(weatherData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("WeatherCheck", "API call failed!");
                Log.d("WeatherCheck","Failure code: " + statusCode);
            }


        });
    }

    private void callWeatherApiForNewCity(String city) {
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", APP_ID);
        callWeatherApi(params);
    }

    private void updateUI(WeatherData weatherData) {
        _tempDisplay.setText(weatherData.getTemperature());
        _cityName.setText(weatherData.getCity());

        int resourceID = getResources().getIdentifier(weatherData.getIconName(), "drawable",
                getPackageName());
        _weatherImage.setImageResource(resourceID);

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (_locationManager != null) {
            _locationManager.removeUpdates(_locationListener);
        }

    }
}
