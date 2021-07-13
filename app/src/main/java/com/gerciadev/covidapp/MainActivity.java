package com.gerciadev.covidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.slice.Slice;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import api.ApiUtilities;
import api.CountryData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalConfirm,totalActive,totalDeath,totalRecovered,totalTests, dateTV;
    private TextView todayConfirm,todayRecovered,todayDeath;
    private PieChart piechart;
    private List<CountryData> list;
    String country ="Angola";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        if (getIntent().getStringExtra("country") != null)
            country = getIntent().getStringExtra("country");


        init();

        TextView cname = findViewById(R.id.cname);
        cname.setText(country);
        cname.setOnClickListener(v ->
               startActivity(new Intent(MainActivity.this,CountryActivity.class)));

        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());

                        for ( int i = 0; i<list.size();i++){
                            if (list.get(i).getCountry().equals(country)){
                              int confirm = Integer.parseInt(list.get(i).getCases());
                              int active = Integer.parseInt(list.get(i).getActive());
                              int recovered = Integer.parseInt(list.get(i).getRecovered());
                              int deaths  = Integer.parseInt(list.get(i).getDeaths());


                              totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                              totalDeath.setText(NumberFormat.getInstance().format(deaths));
                              totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                              totalActive.setText(NumberFormat.getInstance().format(active));

                              todayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                              todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                              todayRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                              totalTests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                              setText(list.get(i).getUpdated());

                              piechart.addPieSlice(new PieModel("Confirm",confirm,getResources().getColor(R.color.yellow)));
                              piechart.addPieSlice(new PieModel("Recovered",recovered,getResources().getColor(R.color.green_pie)));
                              piechart.addPieSlice(new PieModel("Death",deaths,getResources().getColor(R.color.red_pie)));
                              piechart.addPieSlice(new PieModel("Active",active,getResources().getColor(R.color.blue_pie)));

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                       Toast.makeText(MainActivity.this,"Error:"+t.getMessage(),Toast.LENGTH_SHORT);

                    }
                });

    }
    private  void  setText(String Update){
        DateFormat format = new SimpleDateFormat("dd MM, yyyy");
        long milliseconds = Long.parseLong(Update);
        Calendar calendar =Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        dateTV.setText("Updated at" +format.format(calendar.getTime()));
    }
    private void init() {
        totalConfirm = findViewById(R.id.totalConfirm);
        totalActive = findViewById(R.id.totalActive);
        totalDeath = findViewById(R.id.totalDeath);
        totalRecovered = findViewById(R.id.totalRecovered);
        totalTests = findViewById(R.id.totalTests);
        piechart = findViewById(R.id.piechart);
        dateTV = findViewById(R.id.dateTv);
        todayConfirm = findViewById(R.id.todayConfirm);
        todayRecovered = findViewById(R.id.totalRecovered);
        todayDeath = findViewById(R.id.todayDeath);



    }

}