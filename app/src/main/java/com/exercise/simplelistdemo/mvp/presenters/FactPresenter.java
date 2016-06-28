package com.exercise.simplelistdemo.mvp.presenters;

import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.load.data.LocalUriFetcher;
import com.exercise.simplelistdemo.mvp.model.Country;
import com.exercise.simplelistdemo.mvp.model.Fact;
import com.exercise.simplelistdemo.mvp.model.FactRestService;
import com.exercise.simplelistdemo.mvp.views.FactListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit.RestAdapter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by lan on 15/7/17.
 */
public class FactPresenter implements MvpPresenter<FactListView> {
    private final static String TAG = "FactPresenter";

    private FactListView view;
    private List<Fact> facts;

    public FactPresenter() {
        facts = new ArrayList<Fact>();
    }

    /**
     * asynchronous function to start load Fact list
     *
     */
    public void startLoadFacts() {
        if (view == null) {
            Log.w(TAG, "[startLoadFacts] please attach view first.");
            return;
        }
        // show loading progress view
        view.showLoading();
        // User Retrofit to get JSON object
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(FactRestService.SERVICE_ENDPOINT).build();
        FactRestService restService = restAdapter.create(FactRestService.class);
        restService.getCountry()
                .map(new Func1<Country, Country>() {
                    @Override
                    public Country call(Country country) {
                        /**
                         * Use Object stream of RxJava to filter null list item
                         */
                        List<Fact> list = country.getRows();
                        if (list != null) {
                            Iterator<Fact> iterator = list.iterator();
                            while (iterator.hasNext()) {
                                Fact fact = iterator.next();
                                if (TextUtils.isEmpty(fact.getTitle())) {
                                    iterator.remove();
                                }
                            }
                        }
                        return country;
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Country>() {
                    @Override
                    public void onCompleted() {
                        // finish all work
                        view.hideLoading();
                        view.showResult(facts);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // has some exceptions
                        view.hideLoading();
                        view.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(Country country) {
                        // get the parse result
                        List<Fact> list = country.getRows();
                        facts.clear(); // 先清掉之前的
                        facts.addAll(list);
                        // update action bar title
                        view.showTitle(country.getTitle());
                    }
                });
    }

    @Override
    public void attachView(FactListView view) {
        this.view = view;
    }

    @Override
    public void detachView(boolean retainInstance) {
        // TODO
    }

}
