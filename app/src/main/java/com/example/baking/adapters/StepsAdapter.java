package com.example.baking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baking.R;
import com.example.baking.models.Steps;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {
    private List<Steps> stepsList;
    final private stepsItemClickListener itemClickListener;

    public StepsAdapter(List<Steps> stepsList, stepsItemClickListener itemClickListener) {
        this.stepsList = stepsList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_item,parent,false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        holder.stepID.setText(String.valueOf(stepsList.get(position).getId()));
        holder.stepShortDescription.setText(stepsList.get(position).getShortDescription());
        if (stepsList.get(position).getVideoUrl().equals("")){
            holder.playImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (stepsList==null)return 0;
        return stepsList.size();
    }

    public  class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    @BindView(R.id.tv_step_id)
    TextView stepID;
    @BindView(R.id.tv_step_short_discribtion)
    TextView stepShortDescription;
    @BindView(R.id.iv_play)
    ImageView playImage;

    public StepsViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(this);
    }

        @Override
        public void onClick(View v) {
           itemClickListener.onItemClick(getAdapterPosition());
        }
    }

public interface stepsItemClickListener{
        void onItemClick(int itemClicked);
}
}
