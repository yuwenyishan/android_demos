package com.jax.reactivex;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jax.reactivex.model.FunItemInfo;

import java.util.ArrayList;

/**
 * Created on 2017/2/8.
 */

public class FunAdapter extends RecyclerView.Adapter<FunItemHolder> {

    private ArrayList<FunItemInfo> itemInfos;
    private LayoutInflater inflater;
    public ItemClickListener clickListener;

    public FunAdapter(Context context, ArrayList<FunItemInfo> itemInfos) {
        this.itemInfos = itemInfos;
        inflater = LayoutInflater.from(context);
    }

    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public FunItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fun_item_layout, parent, false);
        return new FunItemHolder(view);
    }

    @Override
    public void onBindViewHolder(FunItemHolder holder, int position) {
        holder.tv_Title.setText(itemInfos.get(position).getItemTitle());
        holder.tv_demo.setText(itemInfos.get(position).getItemDes());
        holder.cardView.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.itemClick(position);
            }
        });
        holder.btn_more.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.itemMore(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemInfos.size();
    }

    public interface ItemClickListener {

        void itemClick(int position);

        void itemMore(int position);
    }
}
