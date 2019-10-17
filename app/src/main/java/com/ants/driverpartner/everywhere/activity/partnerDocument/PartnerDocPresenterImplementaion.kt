package com.ants.driverpartner.everywhere.activity.partnerDocument

import android.content.Context
import io.reactivex.disposables.Disposable

class PartnerDocPresenterImplementaion(private var mainView: PartnerDocPresenter.PartnerDocView, context: Context) : PartnerDocPresenter.PartnerDocMainPresenter {
    var context: Context? = context
    private var disposable: Disposable? =null
    override fun onStop() {
        if (disposable != null) {
            disposable!!.dispose()
        }
    }

}