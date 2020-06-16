package com.ants.driverpartner.everywhere.fragment.newBooking

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.ants.driverpartner.everywhere.databinding.FragmentNewBookingBinding
import com.ants.driverpartner.everywhere.fragment.newBooking.model.BookingResponse
import com.ants.driverpartner.everywhere.fragment.newBooking.model.GetNewBookingResponse
import com.ants.driverpartner.everywhere.utils.DialogUtils
import com.ants.driverpartner.everywhere.utils.Utility
import com.google.gson.Gson


class NewBooking(private var view: Homeview) : BaseMainFragment(), NewBookingView, NewBookingAdapter.NewBookingListener {

    lateinit var binding: FragmentNewBookingBinding
    private var presenter: NewBookingPresenter? = null
    private var bookingList = ArrayList<GetNewBookingResponse.Data>()
    private var newBookingAdapter: NewBookingAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_booking, container, false)
        presenter = NewBookingPresenter(this, activity!!)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    fun init() {
        binding.rvNewBooking.visibility = View.GONE
        binding.rvNewBooking.visibility = View.GONE
        Log.e(javaClass.simpleName, "get Current Booking")
        presenter!!.getCurrentBooking()
    }


    override fun onGetNewBooking(responseData: GetNewBookingResponse) {


        if (responseData.data.isNotEmpty()) {

            for (data in responseData.data) {
                bookingList.add(data)
            }

            binding.rvNewBooking.visibility = View.VISIBLE
            binding.rlNewBooking.visibility = View.GONE

            newBookingAdapter = NewBookingAdapter(bookingList, activity!!, this)
            binding.rvNewBooking.layoutManager = LinearLayoutManager(context)
            binding.rvNewBooking.hasFixedSize()
            binding.rvNewBooking.adapter = newBookingAdapter

        } else {

            binding.rvNewBooking.visibility = View.GONE
            binding.rlNewBooking.visibility = View.VISIBLE
        }


    }


    override fun onAccepted(data: GetNewBookingResponse.Data, position: Int) {

        presenter!!.callAcceptBookingApi(data.bookingId)


    }

    override fun onDecline(data: GetNewBookingResponse.Data, position: Int) {
        presenter!!.callDeclineBookingApi(data.bookingId, position)


    }

    override fun onViewClivk(data: GetNewBookingResponse.Data, position: Int) {

        var packageDetail: String
        packageDetail = Gson().toJson(data)

        val intent = Intent(activity!!, PackageDetailActivity::class.java)

        intent.putExtra(Constant.PACKAGE_DETAIL, packageDetail)
        intent.putExtra(Constant.FROM, 1)
        startActivity(intent)


    }

    override fun onAcceptBooking(responseData: BookingResponse) {
        view.changeFragment(2)

    }

    override fun onDeclineBooking(responseData: BookingResponse, position: Int) {

        DialogUtils.showSuccessDialog(activity!!, responseData.message)
        newBookingAdapter!!.removeItems(position)


    }


    override fun validateError(message: String) {
        DialogUtils.showSuccessDialog(activity!!, message)
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter!!.onStop()
    }
    override fun onResume() {
        super.onResume()
        if(Utility.getSharedPreferences(activity!!,"booking_complete").equals("booking_complete")){
            Utility.setSharedPreference(activity!!, "booking_complete","not_complete")
            view.changeFragment(3)
        }


    }



}
