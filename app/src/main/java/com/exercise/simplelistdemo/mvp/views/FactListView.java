package com.exercise.simplelistdemo.mvp.views;

import com.exercise.simplelistdemo.mvp.model.Fact;

import java.util.List;

/**
 * Created by lan on 15/7/17.
 */
public interface FactListView extends MvpView {
    /**
     * show loading view
     */
    void showLoading();

    /**
     * hide loading view when finish load or exception
     */
    void hideLoading();

    /**
     * show error message
     * @param msg
     */
    void showError(String msg);

    /**
     * show list item
     * @param list
     */
    void showResult(List<Fact> list);

    /**
     * update action bar title
     * @param title
     */
    void showTitle(String title);
}
