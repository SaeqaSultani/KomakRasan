package com.example.komakrasan.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.komakrasan.R;
import com.example.komakrasan.adaptrs.GetCountyRecyclerViewAdapter;
import com.example.komakrasan.model.ClassCounty;
import com.example.komakrasan.network.ApiClient;
import com.example.komakrasan.network.ApiInterface;
import com.example.komakrasan.util.MySharedPreferences;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountyActivity extends AppCompatActivity {

    private int id;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private GetCountyRecyclerViewAdapter adapter;
    private ApiInterface apiInterface;
    private List<ClassCounty> counties;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_county);

        //intent for get province id
        Intent intent= getIntent();
        id = intent.getIntExtra("id",-1);
//        Toast.makeText(this, ""+ id, Toast.LENGTH_SHORT).show();


        //init toolbar
        toolbar = findViewById(R.id.toolbar_county);
        toolbar.setTitle("ولسوالی/ناحیه");
        setSupportActionBar(toolbar);


        //init recyclerView
        recyclerView = findViewById(R.id.County_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        //api method
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<List<ClassCounty>> getReportCall = apiInterface.getCounty(id);
        getReportCall.enqueue(new Callback<List<ClassCounty>>() {
            @Override
            public void onResponse(Call<List<ClassCounty>> call, Response<List<ClassCounty>> response) {

                if (!response.isSuccessful()){

                    Toasty.warning(CountyActivity.this, R.string.warning_toast, Toast.LENGTH_SHORT, true).show();

//                    Toast.makeText(MainActivity.this, "the error cod is"+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                counties =  response.body();
                adapter = new GetCountyRecyclerViewAdapter(counties,CountyActivity.this);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<ClassCounty>> call, Throwable t) {

                Toasty.error(CountyActivity.this,R.string.error_toast, Toast.LENGTH_SHORT, true).show();

//                Toast.makeText(getApplicationContext(),"onFailure"+t.getMessage(),Toast.LENGTH_LONG).show();
//                Log.i("TAGS", "onFailure: " + t);

            }
        });
    }
}
