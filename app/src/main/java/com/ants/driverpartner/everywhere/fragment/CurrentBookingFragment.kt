package com.ants.driverpartner.everywhere.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.Home.Homeview
import com.ants.driverpartner.everywhere.databinding.FragmentCurrentBookingBinding
import com.ants.driverpartner.everywhere.databinding.FragmentScheduleBinding

class CurrentBookingFragment(private var view:Homeview) : Fragment() {


    lateinit var binding: FragmentCurrentBookingBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_current_booking,container,false)

        view.setHeaderTitle(getString(R.string.current_fragment))

        return  binding.root

    }



}
