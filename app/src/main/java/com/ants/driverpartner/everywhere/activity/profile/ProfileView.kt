package com.ants.driverpartner.everywhere.activity.profile

import android.content.Context
import android.content.SearchRecentSuggestionsProvider
import com.ants.driverpartner.everywhere.activity.base.BaseMainView
import com.ants.driverpartner.everywhere.activity.profile.model.GetProfileResponse
import com.ants.driverpartner.everywhere.activity.profile.model.UpdateProfileResponse
import com.ants.driverpartner.everywhere.activity.signup.model.UploadImageResponse

interface ProfileView:BaseMainView {
    fun onImageUploadSuccess(responseData: UploadImageResponse)
    fun getContext():Context
    fun onGetProfile(data: List<GetProfileResponse.Data>)


    fun getName():String
    fun getEmail():String
    fun getMobile():String
    fun getPostalAddress():String
    fun getResidentialAddress():String
    fun onUpdateProfile(responseData: UpdateProfileResponse)
}