package com.jax.reactivex;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created on 2017/2/8.
 */

public class FunItemHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public TextView tv_Title;
    public TextView tv_demo;
    public ImageButton btn_more;

    public FunItemHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.FunItemCardView);
        tv_Title = (TextView) itemView.findViewById(R.id.FunItemTitle);
        tv_demo = (TextView) itemView.findViewById(R.id.FunItemIntroduce);
        btn_more = (ImageButton) itemView.findViewById(R.id.FunItemMore);
    }
}
