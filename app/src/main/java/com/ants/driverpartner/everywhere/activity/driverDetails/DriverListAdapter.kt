package com.ants.driverpartner.everywhere.activity.driverDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.driverDetails.model.GetDriverListResponse
import com.ants.driverpartner.everywhere.databinding.RowDriverListBinding
import com.squareup.picasso.Picasso

class DriverListAdapter(
    driverList: ArrayList<GetDriverListResponse.Data>,
    context: Context,
    listener: DriverListener
) : RecyclerView.Adapter<DriverListAdapter.ViewHolder>() {


    private var driverList = driverList
    private var ctx = context
    var listener = listener

    lateinit var binding: RowDriverListBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        binding = DataBindingUtil.inflate(
            LayoutInflater.from(ctx),
            R.layout.row_driver_list,
            parent,
            false
        )
        return ViewHolder(binding)


    }

    override fun getItemCount(): Int {
        return driverList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = driverList[position]

        try {
            Picasso.with(ctx).load(data.profileImage).into(holder.itemRowBinding.imgDriver)

        } catch (e: Exception) {
        }
        holder.itemRowBinding.tvEmail.text = data.email
        holder.itemRowBinding.tvName.text = data.name
        holder.itemRowBinding.tvMobile.text = data.mobile


        holder.itemRowBinding.btnView.setOnClickListener(View.OnClickListener {

            listener.onViewClick(data)

        })
        holder.itemRowBinding.btnDelete.setOnClickListener(View.OnClickListener {
            listener.onDeleteClick(data, position)
        })


    }


    class ViewHolder(itemRowBinding: RowDriverListBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {


        var itemRowBinding = itemRowBinding

    }


    interface DriverListener {


        fun onViewClick(data: GetDriverListResponse.Data)
        fun onDeleteClick(data: GetDriverListResponse.Data, index: Int)

    }
}