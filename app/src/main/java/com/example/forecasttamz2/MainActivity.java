package com.example.forecasttamz2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

    private TextView tvTemperature, tvDescription;
    private ImageView ivIcon;
    private EditText etCityName;
    private Button btnOk, btnForecast, btnHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        tvTemperature = findViewById(R.id.tv_temperature);

        tvDescription = findViewById(R.id.tv_description);

        ivIcon = findViewById(R.id.iv_icon);

        etCityName = findViewById(R.id.et_city_name);
        btnOk = findViewById(R.id.btn_ok);
        btnHistory = findViewById(R.id.btn_history);
        btnForecast = findViewById(R.id.btn_forecast);

        btnOk.setOnClickListener(v -> fetchWeatherData(etCityName.getText().toString().trim()));

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ForecastHistoryActivity.class);
            startActivity(intent);
        });

        btnForecast.setOnClickListener(v -> {
            String cityName = etCityName.getText().toString().trim();
            if (!cityName.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, ForecastActivity.class);
                intent.putExtra("city_name", cityName);
                startActivity(intent);
            } else {
                Log.e("MainActivity", "City name is empty.");
            }
        });


    }

    private String getCurrentDateTime() {
        LocalDateTime now = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now = LocalDateTime.now(ZoneId.of("Europe/Prague"));
        }

        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return now.format(formatter);
        }

        return "";
    }

    private void saveForecastToHistory(String dateTime, String temperature, String description, String iconUrl) {
        SharedPreferences sharedPreferences = getSharedPreferences("ForecastHistory", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String existingHistory = sharedPreferences.getString("history", "[]");

        try {
            JSONArray historyArray = new JSONArray(existingHistory);

            JSONObject newEntry = new JSONObject();
            newEntry.put("cityName", etCityName.getText().toString().trim());
            newEntry.put("dateTime", dateTime);
            newEntry.put("temperature", temperature);
            newEntry.put("description", description);
            newEntry.put("iconUrl", iconUrl);

            historyArray.put(newEntry);

            editor.putString("history", historyArray.toString());
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void fetchWeatherData(String cityName) {
        String apiKey = "2e9971ba6eb19b1c59c5679c737ee1d4";
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=" + apiKey;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject main = response.getJSONObject("main");

                            double temperature = main.getDouble("temp");

                            String description = response.getJSONArray("weather")
                                    .getJSONObject(0)
                                    .getString("description");

                            String icon = response.getJSONArray("weather")
                                    .getJSONObject(0)
                                    .getString("icon");

                            tvTemperature.setText(String.format("%.2f°C", temperature));
                            tvDescription.setText(description);

                            String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";
                            Glide.with(MainActivity.this).load(iconUrl).into(ivIcon);

                            String dateTime = getCurrentDateTime();
                            saveForecastToHistory(dateTime, String.format("%.2f°C", temperature), description, iconUrl);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("WeatherApp", "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("WeatherApp", "API request error: " + error.getMessage());
                    }
                }
        );

        queue.add(jsonObjectRequest);
    }
}
