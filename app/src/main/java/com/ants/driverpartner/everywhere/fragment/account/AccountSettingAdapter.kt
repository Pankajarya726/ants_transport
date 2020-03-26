package com.ants.driverpartner.everywhere.fragment.account

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.databinding.RowAccountSettingBinding

class AccountSettingAdapter(
    accountsettingList: List<AccountSetting>,
    ctx: Context,
    listner: SettingClickListner
) :
    RecyclerView.Adapter<AccountSettingAdapter.ViewHolder>() {


    lateinit var binding: RowAccountSettingBinding
    var context = ctx
    var accountsettingList = accountsettingList
    val listner = listner

    interface SettingClickListner {
        fun onSettingClikc(title: String)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            com.ants.driverpartner.everywhere.R.layout.row_account_setting,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return accountsettingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val setting = accountsettingList.get(position)



        holder.itemRowBinding.imgFlag.setImageResource(setting.settingImgae)
        holder.itemRowBinding.tvSettingName.text = setting.settingName

        if (setting.settingName.equals(context.resources.getString(R.string.notification_setting), false)) {
            holder.itemRowBinding.viewLine.visibility = View.INVISIBLE
        }


        if (setting.settingName.equals(context.resources.getString(R.string.support), false)) {
            holder.itemRowBinding.viewLine.visibility = View.INVISIBLE
        }
        if (setting.settingName.equals(context.resources.getString(R.string.privacy_policy), false)) {
            holder.itemRowBinding.viewLine.visibility = View.INVISIBLE
        }

        holder.itemRowBinding.settingLayout.setOnClickListener(View.OnClickListener { v ->
            listner.onSettingClikc(setting.settingName)
        })


    }

    class ViewHolder(itemRowBinding: RowAccountSettingBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        val itemRowBinding = itemRowBinding
//        fun bind(obj: AccountSetting) {
//            itemRowBinding.setVariable(com.ants.everywhere.BR.accountSetting, obj)
//
//            itemRowBinding.executePendingBindings()
//        }


    }


}