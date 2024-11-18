package com.example.forecasttamz2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForecastHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ForecastAdapter adapter;
    private List<ForecastItem> forecastList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_history);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        forecastList = loadForecastHistory();

        Log.i("kkt", "forecastList: " + forecastList);

        adapter = new ForecastAdapter(this, forecastList);
        recyclerView.setAdapter(adapter);
    }

    private List<ForecastItem> loadForecastHistory() {
        List<ForecastItem> list = new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("ForecastHistory", MODE_PRIVATE);
        String historyJson = sharedPreferences.getString("history", "[]");

        Log.i("kkt", "Načtená historie (JSON): " + historyJson);

        try {
            JSONArray historyArray = new JSONArray(historyJson);
            for (int i = 0; i < historyArray.length(); i++) {
                JSONObject entry = historyArray.getJSONObject(i);

                String cityName = entry.getString("cityName");
                String dateTime = entry.getString("dateTime");
                String temperature = entry.getString("temperature");
                String description = entry.getString("description");
                String iconUrl = entry.getString("iconUrl");

                ForecastItem item = new ForecastItem(cityName, dateTime, temperature, description, iconUrl);
                list.add(item);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    
}
