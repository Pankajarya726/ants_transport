package com.ants.driverpartner.everywhere.activity.Home

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.afollestad.materialdialogs.MaterialDialog
import com.an.customfontview.CustomTextView
import com.ants.driverpartner.everywhere.R
import com.ants.driverpartner.everywhere.databinding.ActivityHomeBinding
import com.ants.driverpartner.everywhere.fragment.*
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var drawer: DrawerLayout? = null
    private var fragmentManager: FragmentManager? = null
    lateinit var binding:ActivityHomeBinding
    private var imageView: ImageView? = null
    private var toolbar: Toolbar? = null
    private val currentFragment = -1
    private var backPressed = false
    private var previous_fragment = "fragment"
    private var isActivityRefreshed = false
    private var navigationFragment: NavigationFragment? = null
    private var newBookingFragment: NewBooking? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar)

        fragmentManager = supportFragmentManager

        init()
        imageView = findViewById(R.id.toolbar_icon_navigation)

        imageView!!.setOnClickListener(View.OnClickListener {
            openCloseDrawer()
        })


    }

    fun init() {
        fragmentManager = supportFragmentManager
        toolbar = findViewById(R.id.layout_toolbar)
        setToolbarTitle("New Booking")
        toolbar!!.navigationIcon =null
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawerLayout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, null,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(0)
        populateNavigationView("0")
    }


    internal fun replaceFragment(position: Int) {

        if (currentFragment == position && position != 0)
            return

        var fragment: Fragment? = null
        val fragmentPosition = position.toString() + ""
        var title: String? = null

        when (position) {
            0 -> {
                newBookingFragment = NewBooking.newInstance()
                fragment = newBookingFragment
                title = "New Booking"
            }

            1 -> {

                fragment = CurrentBookingFragment.getInstance()
                title = "Current Booking"
                isActivityRefreshed = true
            }

            2 -> {
                fragment = ScheduleFragment.getInstance()
                title = "Schedule Booking"
                isActivityRefreshed = true
            }

            3 -> {
                fragment = HistoryFragment.getInstance()
                title = "Booking History"
                isActivityRefreshed = true
            }

            4 -> {
                fragment = AccountFragment.getInstance()
                title = "Account Info"
                isActivityRefreshed = true
            }
//
//            5 -> {
//                //                Toast.makeText(getAppContext(), "Coming soon...", Toast.LENGTH_SHORT).show();
//                fragment = FAQsFragment.newInstance()
//                title = "FAQs"
//                isActivityRefreshed = true
//            }
//
//            6 -> {
//                //                Toast.makeText(getAppContext(), "Coming soon...", Toast.LENGTH_SHORT).show();
//                fragment = TermsAndConditionFragment.newInstance()
//                title = "Terms & Condition"
//                isActivityRefreshed = true
//            }

            7 -> {
                /**
                 * Logout
                 */
                fragment = null
                openCloseDrawer()


                AlertDialog.Builder(this)
                    .setIcon(R.drawable.ants_icon)
                    .setCancelable(true)
                    .setNegativeButton(getString(R.string.no), null)
                    .setTitle(R.string.app_name)
                    .show()
            }
        }// onResume();
        //onLogout();

        if (fragment != null && fragmentPosition != null) {
            replaceFragment(fragment, fragmentPosition, title!!)
            //mvpPresenter.updateDriverStatus("0");
        } else {
            closeNavigationDrawer()
        }

    }
    fun replaceFragment(fragment: Fragment, fragmentPosition: String, title: String) {
        setToolbarTitle(title)
        closeNavigationDrawer()
        fragmentPullPush(fragmentPosition, fragment)
    }



    fun setToolbarTitle(title: String) {
       var textView =  binding.layoutToolbar.findViewById(R.id.toolbar_title) as CustomTextView
        textView.text = title

    }

    fun populateNavigationView(isSelectorChange: String) {
        val bundle = Bundle()
        bundle.putString("isSelectorChange", isSelectorChange)
        navigationFragment = NavigationFragment()
        navigationFragment!!.arguments = bundle

        fragmentManager!!
            .beginTransaction()
            .replace(R.id.flContainerNavigationMenu, navigationFragment!!, "Navigation").commit()
    }
    private fun fragmentPullPush(fragmentPosition: String, fragment: Fragment) {
        backPressed = false
        var isFragmentFound = true

        Log.e(javaClass.simpleName, "fragmentPosition==> $fragmentPosition")
        Log.e(javaClass.simpleName, "flag count==> " + fragmentManager!!.getBackStackEntryCount())

        val transaction = fragmentManager!!.beginTransaction()

        for (entry in 0 until fragmentManager!!.getBackStackEntryCount()) {
            if (fragmentManager!!.getBackStackEntryAt(entry).name.equals(fragmentPosition)) {

                //Log.view(TAG,"Fragment==> "+fragmentManager.getBackStackEntryAt(entry).getName());

                fragmentManager!!.popBackStack(
                    fragmentPosition,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                isFragmentFound = false

                try {
                    navigationFragment!!.changeRowSelector(
                        Integer.parseInt(fragmentManager!!.getBackStackEntryAt(entry).name.toString())
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                break
            }
        }
        if (isFragmentFound && !fragmentPosition.equals("0", ignoreCase = true))
            transaction.add(
                R.id.frm_container,
                fragment
            ).addToBackStack(previous_fragment).commit()

        if (fragmentPosition.equals("0", ignoreCase = true)) {
            for (i in 0 until fragmentManager!!.getBackStackEntryCount()) {
                fragmentManager!!.popBackStack()
            }
            transaction.replace(R.id.frm_container, fragment).commit()

            if (isActivityRefreshed) {
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }


        }

        previous_fragment = fragmentPosition


    }

    fun closeNavigationDrawer() {
        if (drawer!!.isDrawerOpen(Gravity.LEFT)) {
            drawer!!.closeDrawer(Gravity.LEFT)
        }

    }
    private fun openCloseDrawer() {
        if (drawer!!.isDrawerOpen(Gravity.LEFT))
            drawer!!.closeDrawer(Gravity.LEFT)
        else
            drawer!!.openDrawer(Gravity.LEFT)

    }

}
