package com.ants.driverpartner.everywhere.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.databinding.FragmentNewBookingBinding


class NewBooking : Fragment() {

    lateinit var binding: FragmentNewBookingBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_booking,container,false)

        return binding.root
    }



    companion object {


        fun newInstance() =
            NewBooking()
            }

}
