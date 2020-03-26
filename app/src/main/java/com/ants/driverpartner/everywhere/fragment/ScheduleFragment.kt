package com.ants.driverpartner.everywhere.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.Home.Homeview
import com.ants.driverpartner.everywhere.databinding.FragmentScheduleBinding

/**
 * A simple [Fragment] subclass.
 */
class ScheduleFragment(private var view: Homeview) : Fragment() {

    lateinit var binding : FragmentScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =DataBindingUtil.inflate(inflater, R.layout.fragment_schedule,container, false)

        view.setHeaderTitle("Schedule booking")
        return  binding.root

    }



}
