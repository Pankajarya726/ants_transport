package com.ants.driverpartner.everywhere.fragment.account


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ants.driverpartner.everywhere.Constant
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.activity.Home.Homeview
import com.ants.driverpartner.everywhere.activity.base.BaseMainFragment
import com.ants.driverpartner.everywhere.activity.driverDetails.DriverListActivity
import com.ants.driverpartner.everywhere.activity.notification.NotificationActivity
import com.ants.driverpartner.everywhere.activity.profile.ProfileActivity
import com.ants.driverpartner.everywhere.activity.resetPassword.ResetPasswordActivity
import com.ants.driverpartner.everywhere.activity.vehicleDetails.VehilcleListActivity
import com.ants.driverpartner.everywhere.activity.webView.WebViewActivity
import com.ants.driverpartner.everywhere.databinding.FragmentAccountBinding
import com.ants.driverpartner.everywhere.utils.DialogUtils
import com.ants.driverpartner.everywhere.utils.Utility
import com.squareup.picasso.Picasso
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AccountFragment(private var view: Homeview) : BaseMainFragment(),
    AccountSettingAdapter.SettingClickListner {


    lateinit var binding: FragmentAccountBinding
    internal var accountSettingList: MutableList<AccountSetting> = ArrayList()

    internal var aboutList: MutableList<AccountSetting> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        view.setHeaderTitle("Account Info")
        init()
        return binding.root
    }

    private fun init() {

        loadAccountSettingList()
        //loadAboutList()

        binding.tvEmail.text = Utility.getSharedPreferences(activity!!, Constant.EMAIL)
        binding.tvName.text = Utility.getSharedPreferences(activity!!, Constant.NAME)

        try {
            Picasso.with(activity)
                .load(Utility.getSharedPreferences(activity!!, Constant.PROFILE_IMAGE_URL))
                .into(binding.imgProfile)
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, e.localizedMessage)


        }


        binding.tvLogout.setOnClickListener(View.OnClickListener {

            logout()

        })

    }


    override fun onSettingClikc(title: String) {

        when (title) {

            getString(R.string.account_information) -> {
                val intent = Intent(activity!!, ProfileActivity::class.java)
                startActivity(intent)
            }


            getString(R.string.driver_information) -> {
                val intent = Intent(activity!!, DriverListActivity::class.java)

                startActivity(intent)
            }

            getString(R.string.vehicle_information) -> {
                val intent = Intent(activity!!, VehilcleListActivity::class.java)
                startActivity(intent)
            }

            getString(R.string.reset_password) -> resetPassword()

//            getString(R.string.contact_ants)  -> view.replaceFragment(Constant.CONTACT_US, 8)

            getString(R.string.notification_setting) -> {
                val intent = Intent(activity, NotificationActivity::class.java)
                startActivity(intent)
            }

            getString(R.string.abount_ants) -> {

                var title = getString(R.string.abount_ants)
                var page_id = 4

                val intent = Intent(activity!!, WebViewActivity::class.java)
                intent.putExtra(Constant.WEB_VIEW_TITLE, title)
                intent.putExtra(Constant.WEB_VIEW_PAGE_ID, page_id)

                startActivity(intent)
            }

            getString(R.string.contact_ants) -> {


                view.changeFragment(5)

            }


            getString(R.string.faq) -> {


                var title = "FAQ"
                var page_id = 6

                val intent = Intent(activity!!, WebViewActivity::class.java)
                intent.putExtra(Constant.WEB_VIEW_TITLE, title)
                intent.putExtra(Constant.WEB_VIEW_PAGE_ID, page_id)

                startActivity(intent)
            }

            getString(R.string.term_condition) -> {

                var title = "Terms and Conditions"
                var page_id = 2

                val intent = Intent(activity!!, WebViewActivity::class.java)
                intent.putExtra(Constant.WEB_VIEW_TITLE, title)
                intent.putExtra(Constant.WEB_VIEW_PAGE_ID, page_id)

                startActivity(intent)

            }

            getString(R.string.privacy_policy) -> {

                var title = "Privacy Policy"
                var page_id = 3
                val intent = Intent(activity!!, WebViewActivity::class.java)
                intent.putExtra(Constant.WEB_VIEW_TITLE, title)
                intent.putExtra(Constant.WEB_VIEW_PAGE_ID, page_id)
                startActivity(intent)


            }

        }


    }

    private fun resetPassword() {

        val intent = Intent(activity!!, ResetPasswordActivity::class.java)
        intent.putExtra(Constant.FORGOT_PASSWORD, false)
        intent.putExtra(Constant.EMAIL, Utility.getSharedPreferences(activity!!, Constant.EMAIL))
        startActivity(intent)

    }


//    private fun loadAboutList() {
//
//
//        val about_array = getResources().getStringArray(R.array.about_array)
//        val img_array_about =
//            getResources().obtainTypedArray(R.array.img_array_about)
//
//        for (i in about_array.indices) {
//            val accountSetting = AccountSetting()
//            accountSetting.settingName = about_array[i]
//            accountSetting.settingImgae = img_array_about.getResourceId(i, -1)
//            aboutList.add(accountSetting)
//        }
//
//        val adapter = activity?.let { AccountSettingAdapter(aboutList, it, this) }
//        binding.rvAbout.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
//        binding.rvAbout.hasFixedSize()
//        binding.rvAbout.adapter = adapter
//        if (adapter != null) {
//            adapter.notifyDataSetChanged()
//        }
//
//    }


    private fun loadAccountSettingList() {


        val account_setting_array = getResources().getStringArray(R.array.account_setting_array)
        val img_array_account =
            getResources().obtainTypedArray(R.array.img_array_account)

        for (i in account_setting_array.indices) {
            val accountSetting = AccountSetting()



            if(Constant.DRIVER==Utility.getSharedPreferences(activity!!,Constant.ACCOUNT_TYPE)){
                accountSetting.settingName = account_setting_array[i]
                accountSetting.settingImgae = img_array_account.getResourceId(i, -1)

                if(accountSetting.settingName!=activity!!.getString(R.string.driver_information)){
                    accountSettingList.add(accountSetting)
                }

            }else{
                accountSetting.settingName = account_setting_array[i]
                accountSetting.settingImgae = img_array_account.getResourceId(i, -1)
                accountSettingList.add(accountSetting)
            }


        }


        val adapter = activity?.let { AccountSettingAdapter(accountSettingList, it, this) }
        binding.rvAccountSetting.layoutManager = LinearLayoutManager(activity)
        binding.rvAccountSetting.hasFixedSize()
        binding.rvAccountSetting.adapter = adapter
        if (adapter != null) {
            adapter.notifyDataSetChanged()
        }


    }


    override fun validateError(message: String) {
        DialogUtils.showSuccessDialog(activity!!, message)
    }

    fun logout() {

        DialogUtils.showLogoutDialog(
            activity!!,
            "Are you sure, you want to logout?",
            object : DialogUtils.CustomDialogClick {
                override fun onOkClick() {
                    view.logout()

                }
            })

    }

    override fun onResume() {
        super.onResume()

        try {
            Picasso.with(activity)
                .load(Utility.getSharedPreferences(activity!!, Constant.PROFILE_IMAGE_URL))
                .into(binding.imgProfile)
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, e.localizedMessage)


        }

    }

}
