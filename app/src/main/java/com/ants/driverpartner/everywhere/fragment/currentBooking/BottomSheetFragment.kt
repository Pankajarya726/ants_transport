package com.ants.driverpartner.everywhere.fragment.currentBooking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.databinding.BottomSheetBinding
import com.ants.driverpartner.everywhere.fragment.currentBooking.model.GetCurrentBookingRespone
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class BottomSheetFragment(
    customInterface: CustomInterface,
    var packageDetail: GetCurrentBookingRespone.Data
) : BottomSheetDialogFragment(), View.OnClickListener {

    var customInterface = customInterface

    private var data = packageDetail


    lateinit var binding: BottomSheetBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet, container, false)
        val view = binding.root

        init()
        return view
    }

    fun init() {
        binding.imgDrop.setOnClickListener(this)
        binding.imgCall.setOnClickListener(this)
        binding.btnArrived.setOnClickListener(this)
        binding.btnOpen.setOnClickListener(this)



        Picasso.with(activity!!).load(data.packageDetail.custImage).into(binding.ivImage)


        binding.tvVehicleNo.text = data.packageDetail.vehicleRegistrationNumber

        binding.tvName.text = data.packageDetail.senderName
        binding.tvPickup.text = data.packageDetail.senderAddressLine1
        binding.tvDrop.text = data.packageDetail.receiverAddressLine1
        binding.tvWeight.text =
            data.packageDetail.totalWeight.toString() + " " + data.packageDetail.weightUnit
        binding.tvCarrier.text = data.packageDetail.vehicleName
        binding.tvTime.text = data.packageDetail.time
        binding.tvDate.text = data.packageDetail.senderDate
        binding.tvDistance.text =
            data.packageDetail.distance.toString() + " " + data.packageDetail.distanceUnit
    }


    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.img_drop -> {
                customInterface.onCancle()
            }

            R.id.btn_open -> {
                customInterface.onOpenclick()
            }

            R.id.btn_arrived -> {
                customInterface.onArrivedClick()
            }


            R.id.img_call -> {
                customInterface.onCallClick()
            }


        }
    }


}// Required empty public constructor


