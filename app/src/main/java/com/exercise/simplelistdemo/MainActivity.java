package com.exercise.simplelistdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.exercise.simplelistdemo.mvp.model.Fact;
import com.exercise.simplelistdemo.mvp.presenters.FactPresenter;
import com.exercise.simplelistdemo.mvp.views.FactListView;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements FactListView, SwipeRefreshLayout.OnRefreshListener {

    private FactPresenter presenter;
    private ProgressWheel progressWheel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FactAdapter adapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get action bar
        actionBar = getSupportActionBar();

        // load & parse json progress view
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);

        // refresh to load layout
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // listView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FactAdapter();
        recyclerView.setAdapter(adapter);

        // MVP: presenter
        presenter = new FactPresenter();
        presenter.attachView(this); // important, must attachView before use presenter
        presenter.startLoadFacts();
    }

    @Override
    public void showLoading() {
        progressWheel.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        // use objectAnimator let progress from InVisible to Visible
        ValueAnimator progressFadeInAnim = ObjectAnimator.ofFloat(progressWheel, "alpha", 0, 1, 1);
        progressFadeInAnim.start();
    }

    @Override
    public void hideLoading() {
        progressWheel.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false); // close refresh animator

        ValueAnimator progressFadeInAnim = ObjectAnimator.ofFloat(progressWheel, "alpha", 1, 0, 0);
        progressFadeInAnim.start();
    }

    @Override
    public void showError(String msg) {
        // use snackbar to replace Toast to show Error message
        Snackbar.make(swipeRefreshLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showResult(List<Fact> list) {
        // upDate listview adapter
        adapter.setFacts(list);
        String msg = String.format(getString(R.string.update_data_hint), list.size());
        Snackbar.make(swipeRefreshLayout, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTitle(String title) {
        // update actionbar title with "title" from json
        actionBar.setTitle(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(false); // release presenter
    }

    /**
     * 下拉刷新的回调函数
     */
    @Override
    public void onRefresh() {
        /**
         * 关于下拉更新这块是有歧义的，是只更新变的地方？还是清除原来的，直接更新网络上的呢？
         * 目前我选择后一种简单的做法
         */
        presenter.startLoadFacts();
    }

    /**
     * Adapter for Listview
     */
    private static class FactAdapter extends RecyclerView.Adapter<FactViewholder> {

        List<Fact> facts;

        public FactAdapter() {
            facts = new ArrayList<Fact>();
        }

        public void setFacts(List<Fact> list) {
            facts.clear();
            facts.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public FactViewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fact_item, viewGroup, false);
            FactViewholder viewholder = new FactViewholder(view);
            return viewholder;
        }

        @Override
        public void onBindViewHolder(FactViewholder factViewholder, int i) {
            Fact fact = facts.get(i);
            factViewholder.titleView.setText(fact.getTitle());
            factViewholder.descriptionView.setText(fact.getDescription());

            String imageUrl = fact.getImageHref();
            if (TextUtils.isEmpty(imageUrl)) {
                // Need hile imageView if no Image
                factViewholder.imageView.setVisibility(View.GONE);
            } else {
                factViewholder.imageView.setVisibility(View.VISIBLE);
                // Use google Glide to load and cache URL image
                Glide.with(factViewholder.imageView.getContext())
                        .load(fact.getImageHref())
                        .fitCenter()
//                        .placeholder() //可以指定一个占位图片
//                        .error() // 可以指定一个网络下载出错时的图片
                        .into(factViewholder.imageView);
            }

        }

        @Override
        public int getItemCount() {
            return facts.size();
        }
    }

    /**
     * ViewHolder for list item
     */
    private static class FactViewholder extends RecyclerView.ViewHolder {

        public TextView titleView;
        public TextView descriptionView;
        public ImageView imageView;

        public FactViewholder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.titleView);
            descriptionView = (TextView) itemView.findViewById(R.id.descriptionView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
