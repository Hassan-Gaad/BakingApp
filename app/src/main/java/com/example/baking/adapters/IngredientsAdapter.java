package com.example.baking.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baking.R;
import com.example.baking.models.Ingredients;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {


    private List<Ingredients> ingredients;

    public IngredientsAdapter(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_item,parent,false);
        Log.d("ingredients",ingredients.get(0).getIngredient());
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        holder.textViewIngredientsQuantity.setText(ingredients.get(position).getQuantity());
        holder.textViewIngredientsMeasure.setText(ingredients.get(position).getMeasure());
        holder.textViewIngredients.setText(ingredients.get(position).getIngredient());
    }

    @Override
    public int getItemCount() {
       if (ingredients==null)return 0;
       return ingredients.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_ingredients_quantity)
        TextView textViewIngredientsQuantity;
        @BindView(R.id.tv_ingredients_measure)
        TextView textViewIngredientsMeasure;
        @BindView(R.id.tv_ingredients)
        TextView textViewIngredients;

        public IngredientsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
