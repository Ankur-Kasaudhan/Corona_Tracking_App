 package com.example.coronatracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coronatracker.api.ApiUtilities;
import com.example.coronatracker.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalConfirm,todayConfirm,totalActive,totalRecovered,
            todayRecovered,totalDeath,todayDeath, totalTests,date;

    private List<CountryData> list;
    private PieChart pieChart;

    String country = "India";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();

        if (getIntent().getStringExtra("country")!=null)
        {
            country = getIntent().getStringExtra("country");
        }

        init();

        TextView cname = findViewById(R.id.cname);

        cname.setText(country);
        cname.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, CountryActivity.class));
        });
        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());
                        for (int i=0;i<list.size();i++)
                        {
                            if (list.get(i).getCountry().equals(country)){
                                int confirm = Integer.parseInt(list.get(i).getCases());
                                int active = Integer.parseInt(list.get(i).getActive());
                                int recovered = Integer.parseInt(list.get(i).getRecovered());
                                int death = Integer.parseInt(list.get(i).getDeaths());

                                totalActive.setText(NumberFormat.getInstance().format(active));
                                totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                                totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                                totalDeath.setText(NumberFormat.getInstance().format(death));

                                todayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                                todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                                todayRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                                totalTests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                                setText(list.get(i).getUpdated());

                                pieChart.addPieSlice(new PieModel("Confirm",confirm,getResources().getColor(R.color.yellow)));
                                pieChart.addPieSlice(new PieModel("Active",active,getResources().getColor(R.color.blue)));
                                pieChart.addPieSlice(new PieModel("Recovered",recovered,getResources().getColor(R.color.green_pie)));
                                pieChart.addPieSlice(new PieModel("Deaths",death,getResources().getColor(R.color.red_pie)));

                                pieChart.startAnimation();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error :"+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setText(String updated) {
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        long milliseconds = Long.parseLong(updated);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        date.setText("Updated at "+format.format(calendar.getTime()));
    }

    private void init()
    {
        totalConfirm = findViewById(R.id.totalConfirm);
        todayConfirm = findViewById(R.id.todayConfirm);
        totalActive = findViewById(R.id.totalActive);
        totalRecovered = findViewById(R.id.totalRecovered);
        todayRecovered = findViewById(R.id.todayRecovered);
        totalDeath = findViewById(R.id.totalDeath);
        todayDeath = findViewById(R.id.todayDeath);
        totalTests = findViewById(R.id.totalTests);
        pieChart = findViewById(R.id.piechart);
        totalTests = findViewById(R.id.totalTests);
        date = findViewById(R.id.date);

    }
}