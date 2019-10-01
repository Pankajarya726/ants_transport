package com.ants.driverpartner.everywhere.activity.documents

import com.ants.driverpartner.everywhere.activity.base.BasePresenter

class DocumentPresenter(view: DocumentView) : BasePresenter<DocumentView>() {

    init {
        attachView(view)
    }
}