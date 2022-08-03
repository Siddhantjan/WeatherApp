package com.personal.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    TextView tempTextView;
    TextView dateTextView;
    TextView weatherDescTextView;
    TextView cityTextView;
    ImageView weatherImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tempTextView = (TextView) findViewById(R.id.tempTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        weatherDescTextView = (TextView) findViewById(R.id.weatherDesctextView);
        cityTextView = (TextView) findViewById(R.id.cityTextView);

        weatherImageView = (ImageView) findViewById(R.id.weatherImageView);

        dateTextView.setText(getCurrentDate());

        String url = "https://api.openweathermap.org/data/2.5/weather?q=Kota,IN&appid=483d7db4f462ab6c41911debf233cc57&units=Imperial";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, responseObject -> {
                    Log.v("WEATHER", "Response: " + responseObject.toString());

                    try
                    {
                        JSONObject mainJSONObject = responseObject.getJSONObject("main");
                        JSONArray weatherArray = responseObject.getJSONArray("weather");
                        JSONObject firstWeatherObject = weatherArray.getJSONObject(0);

                        String temp = Integer.toString((int) Math.round(mainJSONObject.getDouble("temp")));
                        String weatherDescription = firstWeatherObject.getString("description");
                        String city = responseObject.getString("name");

                        tempTextView.setText(temp);
                        weatherDescTextView.setText(weatherDescription);
                        cityTextView.setText(city);

                        int iconResourceId = getResources().getIdentifier("icon_" + weatherDescription.replace(" ", ""), "drawable", getPackageName());
                        weatherImageView.setImageResource(iconResourceId);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(this, ""+error.getMessage(), Toast.LENGTH_SHORT).show());

        // Access the RequestQueue through your singleton class.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }

    private String getCurrentDate ()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");

        return dateFormat.format(calendar.getTime());
    }
}
