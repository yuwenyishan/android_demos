package com.jax.reactivex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.jax.basedepend.BaseActivity;
import com.jax.reactivex.bean.FunItemType;
import com.jax.reactivex.detail.SingleDemoActivity;
import com.jax.reactivex.detail.SubjectDemoActivity;
import com.jax.reactivex.fun.SingleDemo;
import com.jax.reactivex.fun.SubjectDemo;
import com.jax.reactivex.model.FunItemInfo;
import com.jax.reactivex.util.ToastUtil;

import java.util.ArrayList;

import rx.Single;
import rx.SingleSubscriber;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements FunAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private ArrayList<FunItemInfo> infos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFunItem();
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewRxFunList);
        FunAdapter adapter = new FunAdapter(this, infos);
        adapter.setClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initFunItem() {
        infos.add(new FunItemInfo(FunItemType.TYPE_INTRODUCE, "Rx介绍", "https://mcxiaoke.gitbooks.io/rxdocs/content/Intro.html"));
        infos.add(new FunItemInfo(FunItemType.TYPE_INTRODUCE, "Observable", "https://mcxiaoke.gitbooks.io/rxdocs/content/Observables.html"));
        infos.add(new FunItemInfo(FunItemType.TYPE_DEMO, "Single", "https://mcxiaoke.gitbooks.io/rxdocs/content/Single.html").setTarget(SingleDemoActivity.class));
        infos.add(new FunItemInfo(FunItemType.TYPE_DEMO, "Subject", "https://mcxiaoke.gitbooks.io/rxdocs/content/Subject.html").setTarget(SubjectDemoActivity.class));
    }

    @Override
    public void itemClick(int position) {
        demos(position);
    }

    @Override
    public void itemMore(int position) {
        ToastUtil.showToast("Nothing to do!");
    }

    private void demos(int position) {
        FunItemInfo itemInfo = infos.get(position);
        if (itemInfo.getItemType() == FunItemType.TYPE_DEMO) {
            if (infos.get(position).getTarget() != null) {
                startActivity(new Intent(context, infos.get(position).getTarget()));
            }
        }
    }
}