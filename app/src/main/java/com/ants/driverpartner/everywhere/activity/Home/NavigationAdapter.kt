package com.ants.driverpartner.everywhere.activity.Home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.utils.Utility


import com.bumptech.glide.Glide


import java.util.ArrayList


/**
 * Created by Hasnain on 3-Apr-18.
 */

class NavigationAdapter(private val listener: INavigation) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val navigationDatas: MutableList<NavigationData>
    private var mContext: Context? = null
    private var last_position = 0



    interface INavigation {
        fun onItemClick(position: Int)
    }

    init {
        navigationDatas = ArrayList<NavigationData>()
    }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        if (viewType == TYPE_ITEM) {
            val itemView =
                LayoutInflater.from(mContext).inflate(R.layout.row_navigation, parent, false)
            return ViewHolderItem(itemView)

        } else  {
            val itemView =
                LayoutInflater.from(mContext).inflate(R.layout.navigation_header, parent, false)
            return ViewHolderHeader(itemView)

        }

    }
    override fun getItemCount(): Int {

        Log.e(javaClass.simpleName,"\n\n"+navigationDatas.size)

        return navigationDatas.size + 1
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var position = position
        if (holder is ViewHolderItem) {
            position = position - 1
            (holder as ViewHolderItem).bind(navigationDatas[position], position)

        } else if (holder is ViewHolderHeader) {
            (holder as ViewHolderHeader).bind()
        }


    }

   override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_HEADER else TYPE_ITEM

    }


    internal inner class ViewHolderItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNavigationName: TextView
        private val ivNavigation: ImageView
        private val rl_row: RelativeLayout

        init {
            tvNavigationName = itemView.findViewById(R.id.tvNavigationName)
            ivNavigation = itemView.findViewById(R.id.ivNavigation)
            rl_row = itemView.findViewById(R.id.rl_row)
        }

        fun bind(navigationData: NavigationData, position: Int) {

            tvNavigationName.setText(navigationData.name)

            ivNavigation.setImageResource(navigationData.drawableId)

            ivNavigation.tag = position
            tvNavigationName.setOnClickListener(View.OnClickListener { listener.onItemClick(position) })

            ivNavigation.setOnClickListener { listener.onItemClick(position) }

        }
    }


    fun refreshAdapter(data: ArrayList<NavigationData>) {
        navigationDatas.clear()
        navigationDatas.addAll(data)
        notifyDataSetChanged()
    }


    fun setSelected(position: Int) {
        for (i in navigationDatas.indices) {
            if (i != 7) {
                if (i == position) {
                    navigationDatas[i].isSelected= true
                    last_position = position
                } else {
                    navigationDatas[i].isSelected  = false
                }

            } else {
                navigationDatas[last_position].isSelected = true
            }
        }

        notifyDataSetChanged()
    }


    internal inner class ViewHolderHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iv_profile_image: ImageView
        private val tv_profile_name: TextView
        private val tv_mobile: TextView

        init {
            iv_profile_image = itemView.findViewById(R.id.iv_profile_image)
            tv_mobile = itemView.findViewById(R.id.tv_mobile)
            tv_profile_name = itemView.findViewById(R.id.tv_profile_name)
        }

        fun bind() {


           Log.e(javaClass.simpleName,Utility.getSharedPreferences(mContext!!, Constant.NAME))
            Log.e(javaClass.simpleName,Utility.getSharedPreferences(mContext!!, Constant.MOBILE))
            Log.e(javaClass.simpleName,Utility.getSharedPreferences(mContext!!, Constant.PROFILE_IMAGE_URL))


            tv_profile_name.text = Utility.getSharedPreferences(mContext!!, Constant.NAME)
            tv_mobile.text = Utility.getSharedPreferences(mContext!!, Constant.MOBILE)
            if (!Utility.getSharedPreferences(mContext!!, Constant.NAME).equals("",false)
            ) {
                Glide.with(mContext!!)
                    .load(Utility.getSharedPreferences(mContext!!, Constant.PROFILE_IMAGE_URL))
                    .into(iv_profile_image)
            }
        }
    }

    companion object {
        private val TYPE_HEADER = 0
        private val TYPE_ITEM = 1
    }

}
