package com.example.baking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.baking.adapters.CakeListAdapter;
import com.example.baking.models.Cake;
import com.example.baking.network.GetDataService;
import com.example.baking.network.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CakeListAdapter.ItemClickListener{

    @BindView(R.id.rv_cakeList)
    RecyclerView recyclerView;
    List<Cake> cakeArrayList;
    private ProgressDialog progressDialog;

    @Nullable
    private SimpleIdlingResource mSimpleIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mSimpleIdlingResource == null) {
            mSimpleIdlingResource = new SimpleIdlingResource();
        }
        return mSimpleIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initializeDialog();
        getCakeData();

        getIdlingResource();

    }

    private void initializeDialog() {

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
    }

    private void getCakeData(){
        //Espresso
        if (mSimpleIdlingResource != null) {
            mSimpleIdlingResource.setIdleState(false);
        }
        GetDataService dataService= RetrofitFactory.getRetrofit().create(GetDataService.class);
        Call<List<Cake>> listCall=dataService.getAllCake();
        listCall.enqueue(new Callback<List<Cake>>() {
            @Override
            public void onResponse(Call<List<Cake>> call, Response<List<Cake>> response) {
                if (mSimpleIdlingResource != null) {
                    mSimpleIdlingResource.setIdleState(true);
                }

                progressDialog.dismiss();
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, calculateNoOfColumns(MainActivity.this)));
                recyclerView.setAdapter(new CakeListAdapter(response.body(),MainActivity.this));


                if (response.body()!=null){
                    cakeArrayList=new ArrayList<>(response.body());
                    Log.d("MainActivity ",response.body().get(0).getName());
                }
            }

            @Override
            public void onFailure(Call<List<Cake>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error... Please try again !", Toast.LENGTH_SHORT).show();

            }
        });

    }


    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    @Override
    public void onItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("Cake", cakeArrayList.get(clickedItemIndex));

        startActivity(intent);
    }
}
