package com.example.baking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baking.R;
import com.example.baking.models.Cake;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CakeListAdapter extends RecyclerView.Adapter<CakeListAdapter.CakeListViewHolder>{

    private List<Cake> cakeList;
    final private ItemClickListener mItemClickListener;

    public CakeListAdapter(List<Cake> cakeList, ItemClickListener mItemClickListener) {
        this.cakeList = cakeList;
        this.mItemClickListener = mItemClickListener;
    }


    @NonNull
    @Override
    public CakeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.cake_list_item,parent,false);
        return new CakeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CakeListViewHolder holder, int position) {
        holder.cakeName.setText(cakeList.get(position).getName());
        switch (cakeList.get(position).getName()){

            case "Brownies":
                holder.cakeCard.setImageResource(R.drawable.brownies);
                break;
            case "Nutella Pie":
                holder.cakeCard.setImageResource(R.drawable.nutella_pie);
                break;
            case "Yellow Cake":
                holder.cakeCard.setImageResource(R.drawable.yellow_cake);
                break;
            default:
                holder.cakeCard.setImageResource(R.drawable.cheesecake);
        }

    }

    @Override
    public int getItemCount() {
        if (cakeList==null)return 0;
        return cakeList.size();
    }

    class CakeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_cake_card)
        ImageView cakeCard;
        @BindView(R.id.tv_cake_name)
        TextView cakeName;
        public CakeListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface ItemClickListener{
        void onItemClick(int clickedItemIndex);
    }
}
