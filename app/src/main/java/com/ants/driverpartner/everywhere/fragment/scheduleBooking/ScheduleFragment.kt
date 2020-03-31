package com.ants.driverpartner.everywhere.fragment.scheduleBooking


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.Home.Homeview
import com.ants.driverpartner.everywhere.activity.base.BaseMainFragment
import com.ants.driverpartner.everywhere.activity.packageDetailActivity.PackageDetailActivity
import com.ants.driverpartner.everywhere.databinding.FragmentScheduleBinding
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.model.ChangeBookingStatusResponse
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.model.ScheduleBookingResponse
import com.ants.driverpartner.everywhere.utils.DialogUtils
import com.google.gson.Gson

/**
 * A simple [Fragment] subclass.
 */
class ScheduleFragment(private var view: Homeview) : BaseMainFragment(), ScheduleView,
    ScheduleBookingAdapter.ScheduleListener {

    lateinit var binding: FragmentScheduleBinding
    private var presenter: SchedulePresenter? = null
    private var scheduleList = ArrayList<ScheduleBookingResponse.Data>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)

        presenter = SchedulePresenter(this, activity!!)
        view.setHeaderTitle("Schedule booking")
        init()
        return binding.root

    }

    fun init() {

        binding.rlScheduleBooking.visibility = View.GONE
        binding.rvScheduleBooking.visibility = View.GONE
        presenter!!.getScheculeBooking()


    }

    override fun onViewClick(data: ScheduleBookingResponse.Data) {

        var packageDetail: String


        packageDetail = Gson().toJson(data)

        val intent = Intent(activity!!, PackageDetailActivity::class.java)

        intent.putExtra(Constant.PACKAGE_DETAIL, packageDetail)
        startActivity(intent)
    }


    override fun onStartClick(data: ScheduleBookingResponse.Data) {


        presenter!!.changeBookingStatus(data.bookingId)


    }


    override fun onStatusChange(responseData: ChangeBookingStatusResponse) {


        com.ants.driverpartner.everywhere.utils.DialogUtils.showCustomAlertDialog(
            activity!!,
            "Booking Started",
            object : com.ants.driverpartner.everywhere.utils.DialogUtils.CustomDialogClick {
                override fun onOkClick() {
                    view.changeFragment(1)
                }
            })

        com.ants.driverpartner.everywhere.utils.DialogUtils.showSuccessDialog(
            activity!!,
            responseData.message
        )
    }


    override fun onFailure(message: String) {
        DialogUtils.showSuccessDialog(activity!!,message)
    }
    override fun onGetScheduleBooking(responseData: ScheduleBookingResponse) {
        if (responseData.data.isNotEmpty()) {

            for (data in responseData.data) {
                scheduleList.add(data)
            }


            binding.rlScheduleBooking.visibility = View.GONE
            binding.rvScheduleBooking.visibility = View.VISIBLE
            val scheduleBookingAdapter = ScheduleBookingAdapter(activity!!, scheduleList, this)
            binding.rvScheduleBooking.layoutManager = LinearLayoutManager(context)
            binding.rvScheduleBooking.hasFixedSize()
            binding.rvScheduleBooking.adapter = scheduleBookingAdapter
        } else {
            binding.rlScheduleBooking.visibility = View.VISIBLE
            binding.rvScheduleBooking.visibility = View.GONE
        }

    }

    override fun validateError(message: String) {
        com.ants.driverpartner.everywhere.utils.DialogUtils.showSuccessDialog(activity!!, message)
        binding.rlScheduleBooking.visibility = View.VISIBLE
        binding.rvScheduleBooking.visibility = View.GONE
    }


}
