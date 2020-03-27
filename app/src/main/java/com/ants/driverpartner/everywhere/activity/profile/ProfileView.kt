package com.ants.driverpartner.everywhere.activity.profile

import android.content.Context
import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.activity.profile.model.GetProfileResponse
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse

interface ProfileView:BaseMainView {
    fun onImageUploadSuccess(responseData: UploadImageResponse)
    fun getContext():Context
    fun onGetProfile(data: List<GetProfileResponse.Data>)
}