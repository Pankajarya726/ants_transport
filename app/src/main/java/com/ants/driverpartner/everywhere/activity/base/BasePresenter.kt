//package com.ants.driverpartner.everywhere.activity.base
//
//
//import com.ants.driverpartner.everywhere.api.ApiClients
//import com.ants.driverpartner.everywhere.api.ApiStores
//import rx.Observable
//import rx.Subscriber
//import rx.android.schedulers.AndroidSchedulers
//import rx.schedulers.Schedulers
//import rx.subscriptions.CompositeSubscription
//import java.util.concurrent.TimeUnit
//
//
//open class BasePresenter<V> {
//    var mvpView: V? = null
//    protected lateinit var apiStores: ApiStores
//    private var mCompositeSubscription: CompositeSubscription? = null
//
//    fun attachView(mvpView: V) {
//        this.mvpView = mvpView
//        apiStores = ApiClients.retrofit().create(ApiStores::class.java)
//    }
//
//    fun detachView() {
//        this.mvpView = null
//        onUnsubscribe()
//    }
//
//    //RXjava unregisters to avoid memory leaks
//    fun onUnsubscribe() {
//        if (mCompositeSubscription != null && mCompositeSubscription!!.hasSubscriptions()) {
//            mCompositeSubscription!!.unsubscribe()
//        }
//    }
//
//    fun addSubscription(observable: Observable<Any>, subscriber: Subscriber<Any>) {
//        if (mCompositeSubscription == null) {
//            mCompositeSubscription = CompositeSubscription()
//        }
//        mCompositeSubscription!!.add(
//            observable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber)
//        )
//    }
//
//    fun addSubscriptionInterval(
//        observable: Observable<Any>,
//        subscriber: Subscriber<Any>
//    ) {
//        if (mCompositeSubscription == null) {
//            mCompositeSubscription = CompositeSubscription()
//        }
//
//
//        mCompositeSubscription!!.add(Observable.interval(0, 5, TimeUnit.SECONDS)
//            .subscribeOn(Schedulers.io())
//            .flatMap { observable }
//            .repeat()
//            .subscribe(subscriber)
//
//        )
//
//    }
//
//}
//
//
