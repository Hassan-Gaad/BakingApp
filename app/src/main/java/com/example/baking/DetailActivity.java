package com.example.baking;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baking.adapters.IngredientsAdapter;
import com.example.baking.adapters.StepsAdapter;
import com.example.baking.fragments.DescriptionFragment;
import com.example.baking.fragments.VideoFragment;
import com.example.baking.models.Cake;
import com.example.baking.models.Ingredients;
import com.example.baking.models.Steps;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements StepsAdapter.stepsItemClickListener {
    private Cake cake;
    private ArrayList<Ingredients>listIngredients;
    private ArrayList<Steps> listSteps;
    private String cakeName;
    @BindView(R.id.rv_ingredientes)
    RecyclerView recyclerView_ingredients;
    @BindView(R.id.rv_steps)
    RecyclerView recyclerView_steps;
    final static String STEPS_LIST_KEY="steps_array_list";
    final static String CLICKED_STEP_KEY="clicked_step";
    private static final String WIDGET_SELECTED_CAKE = "WidgetSelectedCake";
    private boolean mTwoPane;
    DescriptionFragment descriptionFragment;
    VideoFragment videoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        cake=getIntent().getParcelableExtra("Cake");
        listIngredients=new ArrayList<>(cake.getIngredients());
        listSteps=new ArrayList<>(cake.getSteps());
        cakeName=cake.getName();
        setTitle(cakeName);
        populateRecyclerViews();
        if (findViewById(R.id.linearLayout_two_pane_detailActivity)!=null){
            mTwoPane=true;// tablet
            if (savedInstanceState==null){
                populateFragments(listSteps,0);
            }

        }else {
            mTwoPane=false;
        }

    }

    @Override
    public void onItemClick(int itemClicked) {

        if (mTwoPane){
            populateFragments(listSteps,itemClicked);

        }else {
            Intent intent=new Intent(this,VideoDescriptionActivity.class);
            intent.putParcelableArrayListExtra(STEPS_LIST_KEY,listSteps);
            intent.putExtra(CLICKED_STEP_KEY,itemClicked);

            startActivity(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_menus,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.add_to_widget_action){
           Intent intent=new Intent(this,CackIngredientsWidget.class);
           intent.putExtra(WIDGET_SELECTED_CAKE,cake);
           intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
           sendBroadcast(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateRecyclerViews(){
        recyclerView_ingredients.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_ingredients.setAdapter(new IngredientsAdapter(listIngredients));

        recyclerView_steps.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_steps.setAdapter(new StepsAdapter(listSteps,this));

    }

    private void populateFragments(List<Steps> stepsList,int clickedStep){
        videoFragment = new VideoFragment();
        videoFragment.setStepList(stepsList);
        videoFragment.setStepListIndex(clickedStep);
        FragmentManager VfragmentManager = getSupportFragmentManager();
        VfragmentManager.beginTransaction()
                .replace(R.id.video_container, videoFragment)
                .commit();


        descriptionFragment = new DescriptionFragment();
        descriptionFragment.setStepList(stepsList);
        descriptionFragment.setStepListIndex(clickedStep);
        FragmentManager DfragmentManager = getSupportFragmentManager();
        DfragmentManager.beginTransaction()
                .replace(R.id.description_container, descriptionFragment)
                .commit();
    }

}


