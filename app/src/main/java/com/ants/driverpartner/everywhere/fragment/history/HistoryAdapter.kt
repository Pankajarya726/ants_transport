package com.ants.driverpartner.everywhere.fragment.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.databinding.RowHistoryBookingBinding
import com.ants.driverpartner.everywhere.fragment.history.model.GetHistroyBookingResponse

class HistoryAdapter(
    historyBookingList: ArrayList<GetHistroyBookingResponse.Data>,
    context: Context,
    lsitener: HistoryListener
) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    private var historyList = historyBookingList
    private var ctx = context
    private var listener = lsitener
    lateinit var rowHistoryBinding: RowHistoryBookingBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        rowHistoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(ctx),
            R.layout.row_history_booking,
            parent,
            false
        )
        return ViewHolder(rowHistoryBinding)

    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = historyList[position]
        holder.binding.tvName.text = data.packageDetail.senderName
        holder.binding.tvTime.text = data.packageDetail.senderDate + " " + data.packageDetail.time
        holder.binding.tvVehicleType.text = data.packageDetail.vehicleName
        holder.binding.tvBookingId.text = data.bookingId.toString()
        holder.binding.tvPickup.text = data.packageDetail.senderAddressLine1
        holder.binding.tvDrop.text = data.packageDetail.receiverAddressLine1
        holder.binding.tvStatus.text = data.isCancelledDriver.toString()
        holder.binding.tvCost.text =
            data.packageDetail.finalAmount.toString() + " " + data.packageDetail.currency


        holder.binding.btnDetail.setOnClickListener(View.OnClickListener {
            listener.onViewClick(data)

        })

    }


    class ViewHolder(binding: RowHistoryBookingBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding = binding
    }

    interface HistoryListener{
        fun onViewClick(data:GetHistroyBookingResponse.Data)
    }
}