package com.example.forecasttamz2;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForecastActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ForecastDayAdapter adapter;
    private List<ForecastItem> forecastList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        recyclerView = findViewById(R.id.recycler_forecast);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        forecastList = new ArrayList<>();
        adapter = new ForecastDayAdapter(this, forecastList);
        recyclerView.setAdapter(adapter);

        String cityName = getIntent().getStringExtra("city_name");
        if (cityName != null && !cityName.isEmpty()) {
            fetchForecast(cityName);
        } else {
            Log.e("ForecastActivity", "City name is null or empty.");
        }
    }

    private void fetchForecast(String cityName) {
        String apiKey = "2e9971ba6eb19b1c59c5679c737ee1d4";
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&units=metric&appid=" + apiKey;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray listArray = response.getJSONArray("list");
                            for (int i = 0; i < listArray.length(); i++) {
                                JSONObject entry = listArray.getJSONObject(i);

                                String dateTime = entry.getString("dt_txt");
                                String temperature = entry.getJSONObject("main").getString("temp") + "°C";
                                String description = entry.getJSONArray("weather").getJSONObject(0).getString("description");
                                String icon = entry.getJSONArray("weather").getJSONObject(0).getString("icon");
                                String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

                                forecastList.add(new ForecastItem(cityName, dateTime, temperature, description, iconUrl));
                            }

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ForecastActivity", "API Error: " + error.getMessage());
                    }
                }
        );

        queue.add(request);
    }

}
