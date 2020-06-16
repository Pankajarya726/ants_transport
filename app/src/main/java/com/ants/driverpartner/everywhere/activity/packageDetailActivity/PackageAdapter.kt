package com.ants.driverpartner.everywhere.activity.packageDetailActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.databinding.RowItemDetailBinding
import com.ants.driverpartner.everywhere.fragment.newBooking.model.GetNewBookingResponse

class PackageAdapter(
    itemDetail: ArrayList<GetNewBookingResponse.Data.PackageDetail.ItemDetail>,
    context: Context
) :
    RecyclerView.Adapter<PackageAdapter.ViewHolder>() {

    lateinit var rowItemBinding: RowItemDetailBinding

    private var itemList = itemDetail
    private var context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        rowItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_item_detail,
            parent,
            false
        )


        return ViewHolder(rowItemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        var itemDetail = itemList[position]


        holder.binding.tvPackageType.text = itemDetail.packagingType

        holder.binding.tvNumberUnits.text = itemDetail.noUnits.toString()

        holder.binding.tvProvideLoader.text = itemDetail.loaders

        if (itemDetail.isHazardeousMaterial == 0)
            holder.binding.tvHazardousMaterial.text = "No"
        else
            holder.binding.tvHazardousMaterial.text = "Yes"

        if (itemDetail.aditonalInsurance == 0)
            holder.binding.tvInsurance.text = "No"
        else
            holder.binding.tvInsurance.text = "Yes"

        holder.binding.tvPackaging.text = itemDetail.packagingType


        if (itemDetail.itemsStckable == 0)
            holder.binding.tvStackable.text = "No"
        else
            holder.binding.tvStackable.text = "Yes"


        if (itemDetail.collectionServices == 0)
            holder.binding.tvCollectionService.text = "Outdoor"
        else
            holder.binding.tvCollectionService.text = "Indoor"


        if (itemDetail.collectionLiftGateService == 0)
            holder.binding.tvLiftServiceCollection.text = "No"
        else
            holder.binding.tvLiftServiceCollection.text = "Yes"

        if (itemDetail.deliveryServices == 0)
            holder.binding.tvDeliveryService.text = "Outdoor"
        else
            holder.binding.tvDeliveryService.text = "Indoor"

        if (itemDetail.deliveryLiftGateService == 0)
            holder.binding.tvLiftServiceDelivery.text = "No"
        else
            holder.binding.tvLiftServiceDelivery.text = "Yes"

        if (itemDetail.deliveryWeightFacilityCollection == 0)
            holder.binding.tvWeightFacilityDelivery.text = "No"
        else
            holder.binding.tvWeightFacilityDelivery.text = "Yes"

        if (itemDetail.deliveryAppointmentRequired == 0)
            holder.binding.tvDeliveryAppointment.text = "No"
        else
            holder.binding.tvDeliveryAppointment.text = "Yes"

        if (itemDetail.callBeforeDelivery == 0)
            holder.binding.tvCallBeforeDelivery.text = "No"
        else
            holder.binding.tvCallBeforeDelivery.text = "Yes"
    }


    class ViewHolder(binding: RowItemDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding = binding

    }

}