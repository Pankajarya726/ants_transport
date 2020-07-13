package com.ants.driverpartner.everywhere.activity.notification

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.base.BaseMainActivity
import com.ants.driverpartner.everywhere.databinding.ActivityNotificationBinding
import com.ants.driverpartner.everywhere.utils.DialogUtils

class NotificationActivity : BaseMainActivity(), NotificationView,
    NotificationAdapter.NotificaionClickListener {


    lateinit var binding: ActivityNotificationBinding

    var layoutManager: LinearLayoutManager? = null

    private var presenter: NotificationPresenter? = null

    private var adapter: NotificationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        presenter = NotificationPresenter(this, this)

        init()

    }

    private fun init() {

        doApiCall()
//        binding.swipeRefresh.setOnRefreshListener(this)
        binding.ivBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })


    }

    private fun doApiCall() {

        presenter!!.getNotification()

    }

    override fun validateError(message: String) {

        DialogUtils.showSuccessDialog(this, message)
    }

    override fun onGetNotification(responseData: NotificationResponse) {

        var notificatonList = ArrayList<NotificationResponse.Data.Notification>()


        if (responseData.data.notificationList.isNotEmpty()) {


            binding.rvNotification.visibility = View.VISIBLE
            binding.layoutNotification.visibility = View.GONE

            for (i in responseData.data.notificationList) {
                notificatonList.add(i)

            }

            layoutManager = LinearLayoutManager(this)
            binding.rvNotification.setHasFixedSize(true)
            binding.rvNotification.setLayoutManager(layoutManager)

            var adapter = NotificationAdapter(notificatonList, this, this)
            binding.rvNotification.setAdapter(adapter)
        } else {
            binding.rvNotification.visibility = View.GONE
            binding.layoutNotification.visibility = View.VISIBLE
        }


    }

    override fun removeNotification(position: Int, item: NotificationResponse.Data.Notification) {

        presenter!!.deleteNotification(item.id, position)


    }

    override fun onRemoveNotification(message: String, position: Int) {
        validateError(message)
    }

    override fun onFailure(message: String) {

        validateError(message)
        binding.rvNotification.visibility = View.GONE
        binding.layoutNotification.visibility = View.VISIBLE

    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
