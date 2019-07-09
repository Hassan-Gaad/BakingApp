package com.example.baking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baking.R;
import com.example.baking.adapters.IngredientsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsStepsFragments extends Fragment {

    public IngredientsStepsFragments(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         return inflater.inflate(R.layout.fragment_ingredients_steps, container, false);


    }


}
