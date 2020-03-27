package com.ants.driverpartner.everywhere.activity.driverDetails

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.driverDetails.model.GetDriverListResponse
import com.ants.driverpartner.everywhere.activity.ownerRegistration.DriverDocument.DriverDocActivity
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivityDriverListBinding
import com.google.gson.Gson

class DriverListActivity : BaseMainActivity(),DriverListView ,DriverListAdapter.DriverListener{

    lateinit var binding:  ActivityDriverListBinding

    private var presenter : DriverListPresenter?=null

    private var driverList = ArrayList<GetDriverListResponse.Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= DataBindingUtil
           .setContentView(this,R.layout.activity_driver_list)

        presenter = DriverListPresenter(this, this)


        init()

    }
    fun init() {

        presenter!!.callDriverListApi()

        binding.back.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        binding.imgAdd.setOnClickListener(View.OnClickListener {
            addDriver()
        })

    }

    private fun addDriver() {

        val intent = Intent(this,DriverDocActivity::class.java)
        intent.putExtra(Constant.ADDING_DRIVER,Constant.ADDING_DRIVER)
        startActivity(intent)
    }

    override fun onGetDriverList(responseData: GetDriverListResponse) {

        if(responseData.data.isNotEmpty()){

            driverList.clear()

            for (data in responseData.data){
                driverList.add(data)
            }

            binding.rvDriverList.layoutManager = LinearLayoutManager(this)
            binding.rvDriverList.hasFixedSize()
            var adapter = DriverListAdapter(driverList,this,this)
            binding.rvDriverList.adapter = adapter
            adapter.notifyDataSetChanged()
        }

    }


    override fun onViewClick(data: GetDriverListResponse.Data) {
        var jsonObject: String

        jsonObject = Gson().toJson(data)

        val intent = Intent(this, ViewDriverDetailActivity::class.java)
        intent.putExtra(Constant.DRIVER_DETAIL, jsonObject)

        startActivity(intent)



    }

    override fun onDeleteClick(data: GetDriverListResponse.Data, position: Int) {
//        presenter!!.deleteVehicle()


    }
    override fun validateError(message: String) {


        com.ants.driverpartner.everywhere.utils.DialogUtils.showSuccessDialog(this,message)

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
