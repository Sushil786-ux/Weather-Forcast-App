package com.example.weatherforecastapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY ="https://api.openweathermap.org/data/2.5/weather?q=" ;
    private EditText cityEditText;
    private Button searchButton;
    private TextView cityNameTextView, tempTextView, conditionTextView, humidityTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        cityEditText = findViewById(R.id.cityEditText);
        searchButton = findViewById(R.id.searchButton);
        cityNameTextView = findViewById(R.id.cityNameTextView);
        tempTextView = findViewById(R.id.tempTextView);
        conditionTextView = findViewById(R.id.conditionTextView);
        humidityTextView = findViewById(R.id.humidityTextView);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = cityEditText.getText().toString().trim();
                if (!cityName.isEmpty()) {
                    getWeatherData(cityName);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a city name", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getWeatherData(String cityName) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=" + API_KEY;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Extract City Name
                        String name = response.getString("name");
                        cityNameTextView.setText(name);

                        // Extract Main Object (Temperature & Humidity)
                        JSONObject mainObj = response.getJSONObject("main");
                        double temp = mainObj.getDouble("temp");
                        int humidity = mainObj.getInt("humidity");

                        tempTextView.setText(String.format("%.1f °C", temp));
                        humidityTextView.setText("Humidity: " + humidity + "%");

                        // Extract Weather Description Array
                        JSONArray weatherArray = response.getJSONArray("weather");
                        JSONObject weatherObj = weatherArray.getJSONObject(0);
                        String description = weatherObj.getString("description");
                        conditionTextView.setText(description.substring(0, 1).toUpperCase() + description.substring(1));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Parsing error!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "City not found or network error", Toast.LENGTH_SHORT).show());

        // Add request to Volley queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}