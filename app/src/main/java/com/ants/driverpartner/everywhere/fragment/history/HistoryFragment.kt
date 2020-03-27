package com.ants.driverpartner.everywhere.fragment.history


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ants.driverpartner.everywhere.Constant

import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.Home.Homeview
import com.ants.driverpartner.everywhere.activity.base.BaseMainFragment
import com.ants.driverpartner.everywhere.activity.packageDetailActivity.PackageDetailActivity
import com.ants.driverpartner.everywhere.databinding.FragmentHistoryBinding
import com.ants.driverpartner.everywhere.fragment.history.model.GetHistroyBookingResponse
import com.google.gson.Gson

class HistoryFragment(view:Homeview) : BaseMainFragment() ,HistoryView,HistoryAdapter.HistoryListener{

    lateinit var binding: FragmentHistoryBinding
    private var presenter : HistoryPresenter?=null
    private var historyBookinList = ArrayList<GetHistroyBookingResponse.Data>()

    var view = view

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_history,container,false)
        view.setHeaderTitle("Booking History")
        presenter= HistoryPresenter(this,activity!!)
        init()
        return binding.root
    }


    private fun init() {


        binding.rvHistory.visibility = View.GONE
        binding.rlHistoryBooking.visibility = View.GONE
        presenter!!.getBookingHistory()

    }


    override fun onGetBookingHistory(data: List<GetHistroyBookingResponse.Data>) {

        if (data.isNotEmpty()) {


            for (datas in data){
                historyBookinList.add(datas)
            }

            binding.rvHistory.visibility = View.VISIBLE
            binding.rlHistoryBooking.visibility = View.GONE


            val historyAdapter = context?.let { HistoryAdapter(historyBookinList, it, this) }
            binding.rvHistory.layoutManager = LinearLayoutManager(context)
            binding.rvHistory.hasFixedSize()
            binding.rvHistory.adapter = historyAdapter

        }else{

            binding.rvHistory.visibility = View.GONE
            binding.rlHistoryBooking.visibility = View.VISIBLE

        }


    }

    override fun onViewClick(data: GetHistroyBookingResponse.Data) {

        var packageDetail :String


        packageDetail = Gson().toJson(data)

        val intent = Intent(activity!!,PackageDetailActivity::class.java)

        intent.putExtra(Constant.PACKAGE_DETAIL,packageDetail)
        startActivity(intent)


    }
    override fun validateError(message: String) {


    }


}
