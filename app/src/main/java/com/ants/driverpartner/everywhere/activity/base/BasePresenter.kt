package com.ants.driverpartner.everywhere.activity.base


import com.ants.driverpartner.everywhere.api.ApiClient
import com.ants.driverpartner.everywhere.api.ApiStores
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription


open class BasePresenter<V> {
    var mvpView: V? = null
    protected lateinit var apiStores: ApiStores
    private var mCompositeSubscription: CompositeSubscription? = null

    fun attachView(mvpView: V) {
        this.mvpView = mvpView
        apiStores = ApiClient.retrofit().create(ApiStores::class.java)
    }

    fun detachView() {
        this.mvpView = null
        onUnsubscribe()
    }

    //RXjava unregisters to avoid memory leaks
    fun onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription!!.hasSubscriptions()) {
            mCompositeSubscription!!.unsubscribe()
        }
    }

    fun addSubscription(observable: Observable<*>, subscriber: Subscriber<*>) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = CompositeSubscription()
        }
        mCompositeSubscription!!.add(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }


}


