package com.exercise.simplelistdemo.mvp.presenters;

import com.exercise.simplelistdemo.mvp.views.MvpView;

/**
 * Created by lan on 15/7/17.
 */
public interface MvpPresenter<V extends MvpView> {

    /**
     * Bind presenter with MvpView
     * @param view
     */
    public void attachView(V view);

    /**
     * unBind
     * @param retainInstance
     */
    public void detachView(boolean retainInstance);
}
