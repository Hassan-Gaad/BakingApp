package com.example.baking.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.baking.R;
import com.example.baking.models.Steps;

import java.util.ArrayList;
import java.util.List;

public class DescriptionFragment extends Fragment {
    private static String TAG =DescriptionFragment.class.getSimpleName();
    private List<Steps> mStepList;
    private int stepListIndex;

    private static String STEPS_LIST_KEY="stepsList";
    private static String STEP_LIST_INDEX="stepListIndex";

    public DescriptionFragment(){}
    private TextView tvDescription;
    private TextView textViewShortDescription;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState!=null){
            mStepList=savedInstanceState.getParcelableArrayList(STEPS_LIST_KEY);
            stepListIndex=savedInstanceState.getInt(STEP_LIST_INDEX);
        }

        View view=inflater.inflate(R.layout.description_fragment,container,false);
        tvDescription=view.findViewById(R.id.tv_stepDescription);
        textViewShortDescription=view.findViewById(R.id.tv_step_video_Short_Description);




        if (mStepList !=null){
            populateTextViews();


        }else {
            Log.d(TAG,"stepList is null");
        }

        return view;
    }

    private void populateTextViews(){
        tvDescription.setText(mStepList.get(stepListIndex).getDescription());
        textViewShortDescription.setText(mStepList.get(stepListIndex).getShortDescription());

    }

    public void setStepList(List<Steps> stepList){
        mStepList =stepList;
    }

    public void setStepListIndex(int stepListIndex) {
        this.stepListIndex = stepListIndex;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(STEPS_LIST_KEY, (ArrayList<? extends Parcelable>) mStepList);
        outState.putInt(STEP_LIST_INDEX,stepListIndex);
    }
}
