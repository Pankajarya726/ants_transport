package com.ants.driverpartner.everywhere.activity.vehicleDetails

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.databinding.ActivityVeiweVehicleDetailBinding
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso

class VeiwVehicleDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityVeiweVehicleDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_veiwe_vehicle_detail)

        var data = intent.getStringExtra(Constant.VEHICLE_DETAIL)

        var jsonObject = Gson().fromJson(data, JsonObject::class.java)

        var data1: GetVehicleListResponse.Data =
            Gson().fromJson(jsonObject, GetVehicleListResponse.Data::class.java)

        initView(data1)


        binding.back.setOnClickListener(View.OnClickListener {

            onBackPressed()
        })

    }


    fun initView(data: GetVehicleListResponse.Data) {

        try {
            Picasso.with(this).load(data.vehicleFrontImage).into(binding.imgVehicle)

        } catch (e: Exception) {

        }

        binding.tvVehicleType.text = data.vehicleType
        binding.tvRegDate.text = data.registrationDate
        binding.tvTare.text = data.tare
        binding.tvVehicleMass.text = data.grossVehMass
        binding.tvRegNo.text = data.registrationNumber
        binding.tvIdentificationNumber.text = data.vehicleIdentificationNumber

        if (data.name.isNotEmpty())
            binding.tvDriverName.text = data.name
        else
            binding.tvDriverName.text = "This Vehicle Not assign to any driver"

    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
