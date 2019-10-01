package com.ants.driverpartner.everywhere.activity.vehicleInfo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.login.LoginActivity
import com.ants.driverpartner.everywhere.databinding.ActivityVehicleInfoBinding

class VehicleInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityVehicleInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_info)

        binding.tvSignup.setOnClickListener(View.OnClickListener { v->
            val intent = Intent(applicationContext,LoginActivity::class.java)
            startActivity(intent)
            finish()
        })

    }
}
