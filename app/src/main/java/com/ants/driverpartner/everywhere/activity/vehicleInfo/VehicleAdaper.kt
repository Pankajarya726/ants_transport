//package com.ants.driverpartner.everywhere.activity.vehicleInfo
//
//import android.content.Context
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.ants.driverpartner.everywhere.R
//import com.ants.driverpartner.everywhere.activity.vehicleInfo.model.VehicleCategory
//import com.ants.driverpartner.everywhere.databinding.RowItemBinding
//
//class VehicleAdaper(context: Context, itemList : List<VehicleCategory.Data>, listner: ItemClick): RecyclerView.Adapter<VehicleAdaper.ViewHolder>() {
//
//
//    var context = context
//    var itemList = itemList
//    lateinit var binding: RowItemBinding
//    var listnert = listner
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
//            R.layout.row_item,parent,false)
//       // var view = LayoutInflater.from(context).inflate(R.layout.row_item,parent,false)
//
//        return ViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int {
//       return itemList.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        var data  = itemList.get(position)
//
//        holder?.rowItemBinding.tvItem.text = data.name
//        holder?.rowItemBinding.tvItem.setOnClickListener(View.OnClickListener {
//
//            Log.e(javaClass.simpleName,data.name+"\t\t"+data.id)
//            listnert.onSelect(data)
//        })
//
//
//    }
//
//    class ViewHolder(rowItemBinding: RowItemBinding) : RecyclerView.ViewHolder(rowItemBinding.root) {
//       val rowItemBinding = rowItemBinding
//
//
//    }
//
//    interface ItemClick{
//      fun onSelect(data: VehicleCategory.Data)
//    }
//}