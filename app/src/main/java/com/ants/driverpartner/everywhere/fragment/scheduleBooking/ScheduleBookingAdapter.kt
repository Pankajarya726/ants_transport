package com.ants.driverpartner.everywhere.fragment.scheduleBooking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.databinding.RowScheduleBookingBinding
import com.ants.driverpartner.everywhere.fragment.scheduleBooking.model.ScheduleBookingResponse

class ScheduleBookingAdapter(context:Context, scheduleList: ArrayList<ScheduleBookingResponse.Data>, listener: ScheduleListener) : RecyclerView.Adapter<ScheduleBookingAdapter.ViewHolder>() {


    private var ctx = context
    private  var scheduleList = scheduleList
    private var listener =listener
    lateinit var scheduleBookingBinding: RowScheduleBookingBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScheduleBookingAdapter.ViewHolder {
        scheduleBookingBinding = DataBindingUtil.inflate(LayoutInflater.from(ctx), R.layout.row_schedule_booking,parent,false)


        return  ViewHolder(scheduleBookingBinding)

    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    override fun onBindViewHolder(holder: ScheduleBookingAdapter.ViewHolder, position: Int) {
        val  data = scheduleList[position]
        holder.binding.tvName.text = data.packageDetail.senderName
        holder.binding.tvTime.text = data.packageDetail.senderDate + " " + data.packageDetail.time
        holder.binding.tvVehicleType.text = data.packageDetail.vehicleName
        holder.binding.tvBookingId.text = data.bookingId.toString()
        holder.binding.tvPickup.text = data.packageDetail.senderAddressLine1
        holder.binding.tvDrop.text = data.packageDetail.receiverAddressLine1
        holder.binding.tvStatus.text = data.isCancelledDriver.toString()
        holder.binding.tvCost.text =
            data.packageDetail.finalAmount.toString() + " " + data.packageDetail.currency


        holder.binding.cardScheduleBooking.setOnClickListener(View.OnClickListener {
            listener.onViewClick(data)

        })
    }



    class ViewHolder(binding:RowScheduleBookingBinding):RecyclerView.ViewHolder(binding.root){
        var binding = binding
    }


    interface ScheduleListener{
        fun onViewClick(data:ScheduleBookingResponse.Data)
    }

}
