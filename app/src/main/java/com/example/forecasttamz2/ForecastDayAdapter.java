package com.example.forecasttamz2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ForecastDayAdapter extends RecyclerView.Adapter<ForecastDayAdapter.ForecastViewHolder> {

    private List<ForecastItem> forecastList;
    private Context context;

    public ForecastDayAdapter(Context context, List<ForecastItem> forecastList) {
        this.context = context;
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ForecastItem item = forecastList.get(position);

        holder.tvCityName.setText(item.getCityName());
        holder.tvDateTime.setText(item.getDateTime());
        holder.tvTemperatureDescription.setText(item.getTemperature() + ": " + item.getDescription());
        Glide.with(context).load(item.getIconUrl()).into(holder.ivWeatherIcon);
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView tvCityName, tvDateTime, tvTemperatureDescription;
        ImageView ivWeatherIcon;
        Button btnDelete;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCityName = itemView.findViewById(R.id.tv_city_name);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tvTemperatureDescription = itemView.findViewById(R.id.tv_temperature_description);
            ivWeatherIcon = itemView.findViewById(R.id.iv_weather_icon);
            btnDelete = itemView.findViewById(R.id.btn_delete_item);

            btnDelete.setVisibility(View.GONE);
        }
    }
}
