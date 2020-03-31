package com.ants.driverpartner.everywhere.activity.notification

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.databinding.RowNotificationBinding

class NotificationAdapter(
    notificationList: ArrayList<NotificationResponse.Data.Notification>,
    context: Context,
    listner: NotificaionClickListener
) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {


    var notificationList = notificationList

    var listener = listner
    private var context = context

    lateinit var notificationBinding: RowNotificationBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.ViewHolder {

        notificationBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_notification,
            parent,
            false
        )


        return ViewHolder(notificationBinding)

    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        val item = notificationList.get(position)


        Log.e(javaClass.simpleName, "\n\n\n" + item.subject + "\n\n\n")

        holder.binding.tvName.setText(item.subject)
        holder.binding.tvNotificationText.setText(item.message)
        holder.binding.tvDate.setText(item.createdAt)
        holder.binding.ivClose.setOnClickListener(View.OnClickListener {

            listener.removeNotification(position, item)

        })


    }

    fun removeItem(position: Int) {

        notificationList.removeAt(position)
        notifyDataSetChanged()

    }


    class ViewHolder(itemRowBinding: RowNotificationBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        var binding = itemRowBinding

    }

    interface NotificaionClickListener {
        fun removeNotification(
            position: Int,
            item: NotificationResponse.Data.Notification
        )
    }
}