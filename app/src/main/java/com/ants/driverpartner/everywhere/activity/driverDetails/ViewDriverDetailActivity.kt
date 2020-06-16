package com.ants.driverpartner.everywhere.activity.driverDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.driverDetails.model.GetDriverListResponse
import com.ants.driverpartner.everywhere.activity.vehicleDetails.GetVehicleListResponse
import com.ants.driverpartner.everywhere.databinding.ActivityVehilcleListBinding
import com.ants.driverpartner.everywhere.databinding.ActivityViewDriverDetailBinding
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import java.lang.Exception

class ViewDriverDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityViewDriverDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_driver_detail)

        var data = intent.getStringExtra(Constant.DRIVER_DETAIL)

        var jsonObject = Gson().fromJson(data, JsonObject::class.java)

        var data1: GetDriverListResponse.Data =
            Gson().fromJson(jsonObject, GetDriverListResponse.Data::class.java)

        initView(data1)


        binding.back.setOnClickListener(View.OnClickListener {

            onBackPressed()
        })
    }

    fun initView(data: GetDriverListResponse.Data) {

        try {
            Picasso.with(this).load(data.profileImage).into(binding.imgProfile)

        }catch (e:Exception){

        }
        binding.edtName.setText(data.name.toString())
        binding.edtEmail.setText ( data.email)
        binding.edtMobile.setText(data.mobile)
        binding.edtPostalAddress.setText(data.postalAddress)
        binding.edtResidentialAddress.setText(data.residentialAddress)


    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
