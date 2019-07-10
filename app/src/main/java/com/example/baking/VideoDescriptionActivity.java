package com.example.baking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.baking.fragments.DescriptionFragment;
import com.example.baking.fragments.VideoFragment;
import com.example.baking.models.Steps;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoDescriptionActivity extends AppCompatActivity {
    final static String STEPS_LIST_KEY="steps_array_list";
    final static String CLICKED_STEP_KEY="clicked_step";
    @BindView(R.id.iv_forward)
    ImageView forward;
    @BindView(R.id.iv_back)
    ImageView back;
    @BindView(R.id.scrollView3)
    ScrollView descriptionContainer;
    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.video_container)
    FrameLayout videoContainer;
    List<Steps> stepsList;
    int clickedStep;



    DescriptionFragment descriptionFragment;
    VideoFragment videoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_description);
        ButterKnife.bind(this);
        ActionBar actionBar=this.getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        stepsList = getIntent().getParcelableArrayListExtra(STEPS_LIST_KEY);
        clickedStep = getIntent().getIntExtra(CLICKED_STEP_KEY, 0);

        if (savedInstanceState!=null){
            stepsList=savedInstanceState.getParcelableArrayList(STEPS_LIST_KEY);
            clickedStep=savedInstanceState.getInt(CLICKED_STEP_KEY);
        }
            populateFragments();


        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickedStep < stepsList.size()-1){
                    clickedStep++;
                }else {
                    clickedStep=0;
                }
              populateFragments();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickedStep >0){
                    clickedStep--;
                }else {
                    clickedStep=stepsList.size()-1;
                }
            populateFragments();
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggleVideoFullScreen(newConfig);
    }

    private void toggleVideoFullScreen(Configuration newConfig) {
        // This method is used to show video in fullscreen when device is rotated
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) videoContainer.getLayoutParams();
        int visibility = View.VISIBLE;

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            visibility = View.GONE;

            if(getSupportFragmentManager().findFragmentById(R.id.description_container) != null) {
                getSupportFragmentManager()
                        .beginTransaction().
                        remove(getSupportFragmentManager().findFragmentById(R.id.description_container)).commit();
            }
            params.width= ViewGroup.LayoutParams.MATCH_PARENT;
            params.height= ViewGroup.LayoutParams.MATCH_PARENT;
            videoContainer.setLayoutParams(params);
            if(getSupportActionBar()!=null) {
                getSupportActionBar().hide();
            }
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //must add after remove
            descriptionFragment = new DescriptionFragment();
            descriptionFragment.setStepList(stepsList);
            descriptionFragment.setStepListIndex(clickedStep);
            FragmentManager DfragmentManager = getSupportFragmentManager();
            DfragmentManager.beginTransaction()
                    .add(R.id.description_container, descriptionFragment)
                    .commit();

            params.width= ViewGroup.LayoutParams.MATCH_PARENT;
            params.height=ViewGroup.LayoutParams.WRAP_CONTENT;
            videoContainer.setLayoutParams(params);
           //populateFragments();
            if(getSupportActionBar()!=null) {
                getSupportActionBar().hide();
            }
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        constraintLayout.setVisibility(visibility);
        descriptionContainer.setVisibility(visibility);
    }




    private void populateFragments(){
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



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEPS_LIST_KEY,(ArrayList<? extends Parcelable>) stepsList);
        outState.putInt(CLICKED_STEP_KEY,clickedStep);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

}
