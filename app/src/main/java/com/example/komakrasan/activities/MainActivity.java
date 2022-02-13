package com.example.komakrasan.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.komakrasan.BuildConfig;
import com.example.komakrasan.R;
import com.example.komakrasan.adaptrs.GetProvinceRecyclerViewAdapter;
import com.example.komakrasan.model.ClassProvince;
import com.example.komakrasan.network.ApiClient;
import com.example.komakrasan.network.ApiInterface;
import com.example.komakrasan.util.MySharedPreferences;
import com.example.komakrasan.util.ViewAnimation;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ApiInterface apiInterface;
    private GetProvinceRecyclerViewAdapter adapter;
    private List<ClassProvince> provinces;
    private final static int LOADING_DURATION = 2000;
    private View parent_view;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Context context;
    ImageButton btn;
    Toolbar toolbar;
     long aLong;
    Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        initComponent();

        //change the language of App white isRtl method
        String languageToLoad = "fa";
        Locale locale = new Locale(languageToLoad);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());


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

        Call<List<ClassProvince>> getReportCall = apiInterface.getProvince();
        getReportCall.enqueue(new Callback<List<ClassProvince>>() {
            @Override
            public void onResponse(Call<List<ClassProvince>> call, Response<List<ClassProvince>> response) {

                if (!response.isSuccessful()){

                    Toasty.warning(MainActivity.this, R.string.warning_toast, Toast.LENGTH_SHORT, true).show();

//                    Toast.makeText(MainActivity.this, "the error cod is"+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                provinces =  response.body();
                adapter = new GetProvinceRecyclerViewAdapter(provinces,MainActivity.this);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<ClassProvince>> call, Throwable t) {

                Toasty.error(MainActivity.this, R.string.error_toast, Toast.LENGTH_SHORT, true).show();

//                Toast.makeText(getApplicationContext(),"onFailure"+t.getMessage(),Toast.LENGTH_LONG).show();
//                Log.i("TAGS", "onFailure: " + t);

            }
        });

    }


    private void initComponent() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.Province_recyclerView);
        btn = findViewById(R.id.item_maine_TextView_more_option);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onBackPressed() {

        if (aLong + 2000 > System.currentTimeMillis()){
            toast.cancel();
            super.onBackPressed();
            return;
        }else {

            toast = Toast.makeText(this, R.string.exist, Toast.LENGTH_SHORT);
            toast.show();
        }
        aLong = System.currentTimeMillis();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int id=item.getItemId();
        Intent intent;

        switch (id){
            case R.id.home:
                intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.registeration:
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.abuot:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.share:
                ShareApp();
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        Intent intent;
        switch (id) {

            case R.id.action_about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ShareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage = "\nLet me recommend you application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.choose_one)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
