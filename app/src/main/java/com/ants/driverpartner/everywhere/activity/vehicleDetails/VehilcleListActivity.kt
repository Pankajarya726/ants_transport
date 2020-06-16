package com.ants.driverpartner.everywhere.activity.vehicleDetails

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.ownerRegistration.vehicleInformation.VehicleActivity
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivityVehilcleListBinding
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.gson.Gson
import java.util.*

class VehilcleListActivity : BaseMainActivity(), VehicleListView,
    VehicleListAdapter.VehicleClickListner {


    lateinit var binding: ActivityVehilcleListBinding

    private var presenter: VehicleListPresenter? = null
    private var vehicleList = ArrayList<GetVehicleListResponse.Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehilcle_list)
        presenter = VehicleListPresenter(this, this)


        init()


    }


    fun init() {

        presenter!!.callVehicleListApi()

        var type = Utility.getSharedPreferences(this, Constant.ACCOUNT_TYPE)

        if (type.equals(Constant.DRIVER)) {
            binding.imgAdd.visibility = View.GONE
        }


        binding.back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        binding.imgAdd.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, VehicleActivity::class.java)
            intent.putExtra(Constant.ADDING_VEHICLE, Constant.ADDING_VEHICLE)
            intent.putExtra(Constant.PROFILE_TYPE, Constant.OWNER)
            startActivity(intent)
            this.finish()
        })

    }

    override fun onGetVehicleList(responseData: GetVehicleListResponse) {

        if (responseData.data.isNotEmpty()) {

            vehicleList.clear()

            for (data in responseData.data) {
                vehicleList.add(data)
            }

            binding.rvVehicleList.layoutManager = LinearLayoutManager(this)
            binding.rvVehicleList.hasFixedSize()
            var adapter = VehicleListAdapter(vehicleList, this, this)
            binding.rvVehicleList.adapter = adapter
            adapter.notifyDataSetChanged()


        }

    }


    override fun onViewClick(data: GetVehicleListResponse.Data) {
        var jsonObject: String

        jsonObject = Gson().toJson(data)

        val intent = Intent(this, VeiwVehicleDetailActivity::class.java)
        intent.putExtra(Constant.VEHICLE_DETAIL, jsonObject)

        startActivity(intent)


    }

    override fun onDeleteClick(data: GetVehicleListResponse.Data, position: Int) {
//        presenter!!.deleteVehicle()


    }

    override fun validateError(message: String) {


        com.ants.driverpartner.everywhere.utils.DialogUtils.showSuccessDialog(this, message)

    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onStop()
    }
}
