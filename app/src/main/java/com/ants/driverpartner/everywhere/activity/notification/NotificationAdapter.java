//package com.ants.driverpartner.everywhere.activity.notification;
//
//class NotificationAdapter(notificationList: ArrayList<NotificationResponse.Data.Notification>, context: Context,listner:NotificaionClickListener) :
//    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
//
//    private val VIEW_TYPE_LOADING = 0
//    private val VIEW_TYPE_NORMAL = 1
//    private var isLoaderVisible = false
//    var notificationList = notificationList
//
//    var listener  = listner
//    private var context = context
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//
//        return ViewHolder(
//            LayoutInflater.from(context).inflate(
//                R.layout.row_notification,
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun getItemCount(): Int {
//        return notificationList.size
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//
//        val item = notificationList.get(position)
//
//
//        Log.e(javaClass.simpleName, "\n\n\n" + item.subject + "\n\n\n")
//
//        holder.textViewTitle.setText(item.subject)
//        holder.textViewDescription.setText(item.message)
//        holder.textViewDate.setText(item.createdAt)
//        holder.imageClose.setOnClickListener(View.OnClickListener {
//
//            removeItems(position)
//            //listener.removeNotification(position,item)
//        })
//        // holder.onBind(position);
//
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (isLoaderVisible) {
//            if (position == notificationList.size - 1) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
//        } else {
//            VIEW_TYPE_NORMAL
//        }
//    }
//
//    fun addItems(notificationList: ArrayList<NotificationResponse.Data.Notification>) {
//        this.notificationList.addAll(notificationList)
//
//        Log.e(javaClass.simpleName, "\n\n" + notificationList.size)
//
//
//        notifyDataSetChanged()
//    }
//
//    fun removeItems(position: Int) {
//        this.notificationList.removeAt(position)
//
//        notifyDataSetChanged()
//    }
//
//
//    fun clear() {
//        notificationList.clear()
//        notifyDataSetChanged()
//    }
//
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        var textViewTitle: TextView = itemView.findViewById(R.id.tv_name)
//
//        var textViewDescription: TextView = itemView.findViewById(R.id.tv_notification_text)
//        var textViewDate: TextView = itemView.findViewById(R.id.tv_date)
//        var imageClose: ImageView = itemView.findViewById(R.id.iv_close)
//
//
//    }
//
//
//    interface NotificaionClickListener{
//        fun removeNotification(
//            position: Int,
//            item: NotificationResponse.Data.Notification
//        )
//    }
//
//
//
//}