package com.example.komakrasan.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.komakrasan.R;
import com.example.komakrasan.adaptrs.GetProvinceRecyclerViewAdapter;
import com.example.komakrasan.adaptrs.GetResultRecyclerViewAdapter;
import com.example.komakrasan.model.ClassProvince;
import com.example.komakrasan.model.ClassResult;
import com.example.komakrasan.network.ApiClient;
import com.example.komakrasan.network.ApiInterface;
import com.example.komakrasan.util.ViewAnimation;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private GetResultRecyclerViewAdapter adapter;
    private List<ClassResult> results;
    private ApiInterface apiInterface;
    private final static int LOADING_DURATION = 2000;
    private View parent_view;
    private int id;
    private LinearLayoutManager layoutManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //intent for get county id
        Intent intent= getIntent();
        id = intent.getIntExtra("id",-1);
//        Log.i("kkk", "onCreate: kkk" + id);
//        Toast.makeText(this, ""+ id, Toast.LENGTH_SHORT).show();

        //init toolbar
        toolbar = findViewById(R.id.toolbar_result);
        toolbar.setTitle("نتایج");
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.Result_recyclerView);


        initprogress();
    }

    // Initialize activity center progress handler
    private void initprogress(){

        final LinearLayout lyt_progress = findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);
        recyclerView.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewAnimation.fadeOut(lyt_progress);
            }
        }, LOADING_DURATION);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                apiMethod();
            }
        }, LOADING_DURATION + 400);

    }

    private void apiMethod(){

        //init recyclerView
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        //Api method
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<List<ClassResult>> getReportCall = apiInterface.getData(id);
        getReportCall.enqueue(new Callback<List<ClassResult>>() {
            @Override
            public void onResponse(Call<List<ClassResult>> call, Response<List<ClassResult>> response) {

                if (!response.isSuccessful()){

                    Toasty.warning(ResultActivity.this, R.string.warning_toast, Toast.LENGTH_SHORT, true).show();

//                    Toast.makeText(ResultActivity.this, "the error cod is"+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                results =  response.body();
                adapter = new GetResultRecyclerViewAdapter(results,ResultActivity.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ClassResult>> call, Throwable t) {

                Toasty.error(ResultActivity.this, R.string.error_toast, Toast.LENGTH_SHORT, true).show();

//                Toast.makeText(getApplicationContext(),"onFailure"+t.getMessage(),Toast.LENGTH_LONG).show();
//                Log.i("TAGS", "onFailure: " + t);

            }
        });

    }
}
