package com.ants.driverpartner.everywhere.activity.vehicleDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.databinding.RowVehicleListBinding
import com.squareup.picasso.Picasso

class VehicleListAdapter(vehicleList: ArrayList<GetVehicleListResponse.Data>,context: Context, listner: VehicleClickListner) :
    RecyclerView.Adapter<VehicleListAdapter.ViewHolder>() {

    lateinit var binding: RowVehicleListBinding
    private var vehicleList = vehicleList
    private var ctx = context
    private  var listner = listner


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       binding = DataBindingUtil.inflate(LayoutInflater.from(ctx),R.layout.row_vehicle_list,parent,false)
        return  ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return vehicleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val data = vehicleList[position]


        holder.itemRowBinding.tvVehicleType.text = data.vehicleType
        holder.itemRowBinding.tvVehicleModel.text = data.model
        holder.itemRowBinding.tvVehicleNo.text = data.vehicleIdentificationNumber

        try {
            Picasso.with(ctx).load(data.vehicleFrontImage).into(holder.itemRowBinding.imgVehicle)
        }catch (e:Exception){
           holder.itemRowBinding.imgVehicle.setImageResource(R.drawable.ants)
        }

        holder.itemRowBinding.btnView.setOnClickListener(View.OnClickListener {

            listner.onViewClick(data)
        })

        holder.itemRowBinding.btnDelete.setOnClickListener(View.OnClickListener {
            listner.onDeleteClick(data,position)
        })
    }


    class ViewHolder(itemRowBinding: RowVehicleListBinding):RecyclerView.ViewHolder(itemRowBinding.root) {

         val itemRowBinding = itemRowBinding

    }


    interface VehicleClickListner{
        fun onViewClick(data: GetVehicleListResponse.Data)
        fun onDeleteClick(
            data: GetVehicleListResponse.Data,
            position: Int
        )
    }
}