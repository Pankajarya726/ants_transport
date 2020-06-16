package com.ants.driverpartner.everywhere.activity.packageDetailActivity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.databinding.ActivityPackageDetailBinding
import com.ants.driverpartner.everywhere.fragment.newBooking.model.GetNewBookingResponse
import com.google.gson.Gson
import com.google.gson.JsonObject

class PackageDetailActivity : AppCompatActivity() {


    lateinit var binding: ActivityPackageDetailBinding

    private var from = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_package_detail)

        var packageDetail = intent.getStringExtra(Constant.PACKAGE_DETAIL)

        from = intent.getIntExtra(Constant.FROM,0)


        var jsonObject = Gson().fromJson(packageDetail, JsonObject::class.java)

        var data: GetNewBookingResponse.Data =
            Gson().fromJson(jsonObject, GetNewBookingResponse.Data::class.java)

        init(data)

    }

    fun init(data: GetNewBookingResponse.Data) {



        binding.tvSenderName.text = data.packageDetail.senderName
        binding.tvSenderNumber.text = data.packageDetail.senderPhone
        binding.tvSenderCollectionFloor.text = data.packageDetail.senderFloor.toString()

        binding.tvPickupAddress.text = data.packageDetail.senderAddressLine1
        binding.tvDropAddress.text = data.packageDetail.receiverAddressLine1


        if (data.packageDetail.senderLift == 0)
            binding.tvSenderLiftElevator.text = "No"
        else
            binding.tvSenderLiftElevator.text = "Yes"

        if (data.packageDetail.senderSupplyForkift == 0)
            binding.tvSenderForklift.text = "No"
        else
            binding.tvSenderForklift.text = "Yes"



        binding.tvReceiverName.text = data.packageDetail.receiverName
        binding.tvReceiverNumber.text = data.packageDetail.receiverPhone
        binding.tvReceiverFloor.text = data.packageDetail.receiverFloor


        if (data.packageDetail.receiverLift == 0)
            binding.tvReceiverLiftElevator.text = "No"
        else
            binding.tvReceiverLiftElevator.text = "Yes"

        if (data.packageDetail.receiverForkift == 0)
            binding.tvReceiverForklift.text = "No"
        else
            binding.tvReceiverForklift.text = "Yes"


        binding.tvLoadType.text = data.packageDetail.loadType


        try {
            if (data.packageDetail.itemDetail.size > 0) {

                var itemDetail = ArrayList<GetNewBookingResponse.Data.PackageDetail.ItemDetail>()
                itemDetail.addAll(data.packageDetail.itemDetail)

                var adapter = PackageAdapter(itemDetail, this)
                binding.rvItemDetails.layoutManager = LinearLayoutManager(this)
                binding.rvItemDetails.hasFixedSize()
                binding.rvItemDetails.adapter = adapter


            }
        } catch (e: Exception) {

        }


       /* binding.tvPackageType.text = data.packageDetail.itemDetail[0].whatLikeDelivered

        binding.tvNumberUnits.text = data.packageDetail.itemDetail[0].noUnits.toString()

        binding.tvProvideLoader.text = data.packageDetail.itemDetail[0].loaders

        if (data.packageDetail.itemDetail[0].isHazardeousMaterial == 0)
            binding.tvHazardousMaterial.text = "No"
        else
            binding.tvHazardousMaterial.text = "Yes"

        if (data.packageDetail.itemDetail[0].aditonalInsurance == 0)
            binding.tvInsurance.text = "No"
        else
            binding.tvInsurance.text = "Yes"

        binding.tvPackaging.text = data.packageDetail.itemDetail[0].packagingType


        if (data.packageDetail.itemDetail[0].itemsStckable == 0)
            binding.tvStackable.text = "No"
        else
            binding.tvStackable.text = "Yes"


        if (data.packageDetail.itemDetail[0].collectionServices == 0)
            binding.tvCollectionService.text = "No"
        else
            binding.tvCollectionService.text = "Yes"


        if (data.packageDetail.itemDetail[0].collectionLiftGateService == 0)
            binding.tvLiftServiceCollection.text = "No"
        else
            binding.tvLiftServiceCollection.text = "Yes"

        if (data.packageDetail.itemDetail[0].deliveryServices == 0)
            binding.tvDeliveryService.text = "No"
        else
            binding.tvDeliveryService.text = "Yes"

        if (data.packageDetail.itemDetail[0].deliveryLiftGateService == 0)
            binding.tvLiftServiceDelivery.text = "No"
        else
            binding.tvLiftServiceDelivery.text = "Yes"

        if (data.packageDetail.itemDetail[0].deliveryWeightFacilityCollection == 0)
            binding.tvWeightFacilityDelivery.text = "No"
        else
            binding.tvWeightFacilityDelivery.text = "Yes"

        if (data.packageDetail.itemDetail[0].deliveryAppointmentRequired == 0)
            binding.tvDeliveryAppointment.text = "No"
        else
            binding.tvDeliveryAppointment.text = "Yes"

        if (data.packageDetail.itemDetail[0].callBeforeDelivery == 0)
            binding.tvCallBeforeDelivery.text = "No"
        else
            binding.tvCallBeforeDelivery.text = "Yes"
*/

        binding.imgBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
