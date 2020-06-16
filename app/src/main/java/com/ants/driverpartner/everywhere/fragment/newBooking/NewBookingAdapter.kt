package com.ants.driverpartner.everywhere.fragment.newBooking

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.databinding.RowNewBookingBinding
import com.ants.driverpartner.everywhere.fragment.newBooking.model.GetNewBookingResponse
import com.squareup.picasso.Picasso

class NewBookingAdapter(
    bookingList: ArrayList<GetNewBookingResponse.Data>,
    context: Context,
    listener: NewBookingListener
) :
    RecyclerView.Adapter<NewBookingAdapter.ViewHolder>() {

    private var ctx = context;
    private var bookingList = bookingList
    private var listener = listener
    lateinit var binding: RowNewBookingBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(ctx),
            R.layout.row_new_booking,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = bookingList[position]

        try {
            Picasso.with(ctx).load(data.packageDetail.custImage).into(holder.itemRowBinding.ivImage)

        } catch (e: Exception) {

        }

        holder.itemRowBinding.tvPirce.text =
            data.packageDetail.finalAmount.toString() + " " + data.packageDetail.currency
        holder.itemRowBinding.tvName.text = data.packageDetail.senderName
        holder.itemRowBinding.tvPickup.text = data.packageDetail.senderAddressLine1
        holder.itemRowBinding.tvDrop.text = data.packageDetail.receiverAddressLine1
        holder.itemRowBinding.tvWeight.text =
            data.packageDetail.totalWeight.toString() + " " + data.packageDetail.weightUnit
        holder.itemRowBinding.tvTime.text = data.packageDetail.time.toString()
        holder.itemRowBinding.tvCarrier.text = data.packageDetail.vehicleName
        holder.itemRowBinding.tvDate.text = data.packageDetail.senderDate
        holder.itemRowBinding.tvDistance.text =
            data.packageDetail.distance.toString() + " " + data.packageDetail.distanceUnit

        holder.itemRowBinding.btnAccept.setOnClickListener(View.OnClickListener {
            listener.onAccepted(data, position)
        })

        holder.itemRowBinding.btnDecline.setOnClickListener(View.OnClickListener {
            listener.onDecline(data, position)
        })

        holder.itemRowBinding.btnOpen.setOnClickListener(View.OnClickListener {
            listener.onViewClivk(data, position)
        })
    }

    class ViewHolder(itemRowBinding: RowNewBookingBinding) : RecyclerView.ViewHolder(itemRowBinding.root) {
        var itemRowBinding = itemRowBinding
    }

    fun removeItems(position: Int) {
        this.bookingList.removeAt(position)

        notifyDataSetChanged()
    }

    interface NewBookingListener {
        fun onAccepted(
            data: GetNewBookingResponse.Data,
            position: Int
        )

        fun onDecline(
            data: GetNewBookingResponse.Data,
            position: Int
        )

        fun onViewClivk(
            data: GetNewBookingResponse.Data,
            position: Int
        )
    }
}