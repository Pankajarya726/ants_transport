//package com.ants.driverpartner.everywhere.activity.base
//
//import android.os.Bundle
//import com.ants.driverpartner.everywhere.base.BaseMainActivity
//
//abstract class MvpActivity<P : BasePresenter<*>> : BaseMainActivity() {
//    protected var mvpPresenter: P? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        mvpPresenter = createPresenter()
//        super.onCreate(savedInstanceState)
//    }
//
//    protected abstract fun createPresenter(): P
//
//    override fun onDestroy() {
//        super.onDestroy()
//        if (mvpPresenter != null) {
//            mvpPresenter!!.detachView()
//        }
//    }
//
//}
